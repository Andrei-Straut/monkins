package com.andreistraut.monkins.communicator;

import com.andreistraut.monkins.model.ConfigurationManager;
import com.andreistraut.monkins.model.PollingJob;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint handling the communication between the client and the server.
 *
 * Is the unique link and method of communication between the client interface
 * and the server
 */
@ServerEndpoint("/controller")
public class WebSocketService {

    private static ArrayList<Session> sessions;
    private ConfigurationManager configManager;
    private PollingService pollingService;

    @OnOpen
    public void onOpen(Session session) throws IOException {
	Logger.getLogger(WebSocketService.class.getName()).log(
		Level.INFO, "{0}: {1}",
		new Object[]{session.getId(), "Connection opened"});

	if (this.configManager == null) {
	    this.configManager = ConfigurationManager.getInstance();
	    Logger.getLogger(WebSocketService.class.getName()).log(
		    Level.INFO, "ConfigurationManager retrieved");
	}

	if (this.pollingService == null) {
	    this.pollingService = new PollingService();
	    Logger.getLogger(WebSocketService.class.getName()).log(
		    Level.INFO, "PollingService instantiated");
	}
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
	Logger.getLogger(WebSocketService.class.getName()).log(
		Level.INFO, "{0}: {1}",
		new Object[]{session.getId(), message});

	MessageRequest request;
	MessageResponse response;

	try {
	    request = new MessageRequest(message);
	} catch (JsonSyntaxException e) {
	    Logger.getLogger(WebSocketService.class.getName()).log(Level.SEVERE,
		    "{0}: Could not parse JSON request: {1}", new Object[]{session.getId(), e});

	    response = new MessageResponse(0);
	    response
		    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Error occurred processing message request: " + message);
	    respond(session, response);
	    return;
	}

	switch (request.getType()) {
	    case GETJOBSLIST: {

		CopyOnWriteArrayList<PollingJob> jobs = configManager.getPollingJobs();
		JsonArray jobsJson = new JsonArray();

		for (PollingJob job : jobs) {
		    jobsJson.add(job.toJson());
		}

		JsonObject responseData = new JsonObject();
		responseData.add("jobs", jobsJson);

		response = new MessageResponse(request.getCallbackId(), 200, true, "JobList", responseData);
		respond(session, response);

		break;

	    }
	    case SUBSCRIBE: {
		if (sessions == null) {
		    sessions = new ArrayList<>();
		}

		if (!sessions.isEmpty() && sessions.contains(session)) {
		    sessions.remove(session);
		}

		sessions.add(session);
		Logger.getLogger(WebSocketService.class.getName()).log(
			Level.INFO, "{0}: {1}",
			new Object[]{session.getId(), "Added to subscribers list"});

		pollingService.start();

		respond(session, new MessageResponse(request.getCallbackId(), 200, false, "Subscribe", new JsonObject()));
		break;
	    }
	    case UNSUBSCRIBE: {
		unsubscribeClient(session);
		respond(session, new MessageResponse(request.getCallbackId(), 200, false, "Unsubscribe", new JsonObject()));

		break;
	    }
	    case UNSUBSCRIBEALL: {
		if (sessions != null && !sessions.isEmpty()) {
		    for (Session clientSession : sessions) {
			unsubscribeClient(clientSession);
		    }
		}

		sessions.clear();
		this.pollingService.stop();

		respond(session, new MessageResponse(request.getCallbackId(), true, new JsonObject()));

		break;
	    }
	    case GETSETTINGS: {
		respond(session, new MessageResponse(request.getCallbackId(), true, configManager.toJson()));

		break;
	    }
	    case UPDATESETTINGS: {
		respond(session, new MessageResponse(request.getCallbackId(), 200, true, "No-Op", null));
		
		break;
	    }
	    case UNKNOWN:
	    default: {

		Logger.getLogger(WebSocketService.class.getName()).log(Level.WARNING,
			"{0}: Message type unknown: {1}", new Object[]{session.getId(), message});

		response = new MessageResponse(request.getCallbackId());
		response
			.setStatus(HttpServletResponse.SC_BAD_REQUEST)
			.setIsEnded(true)
			.setDescription("Message type unknown");
		respond(session, response);
	    }
	}
    }

    @OnClose
    public void onClose(Session session) {

	Logger.getLogger(WebSocketService.class
		.getName()).log(
			Level.INFO, "{0}: {1}",
			new Object[]{session.getId(), "Connection closed"});

	unsubscribeClient(session);
    }

    public static void respond(Session session, MessageResponse response) {
	try {
	    session.getBasicRemote().sendText(response.toJsonString());

	} catch (IOException e) {
	    Logger.getLogger(WebSocketService.class
		    .getName()).log(Level.SEVERE,
			    "{0}: Error occurred responding to request: {1}",
			    new Object[]{session.getId(), e});
	}
    }

    public static void respondAll(MessageResponse response) {
	Logger.getLogger(WebSocketService.class
		.getName()).log(Level.FINER,
			"Number of subscribers: {0}",
			new Object[]{sessions.size()});

	for (Session session : sessions) {
	    Logger.getLogger(WebSocketService.class
		    .getName()).log(Level.INFO,
			    "{0}: Sending update: {1}",
			    new Object[]{session.getId(), response.toJsonString()});
	    try {
		session.getBasicRemote().sendText(response.toJsonString());

	    } catch (IOException e) {
		Logger.getLogger(WebSocketService.class
			.getName()).log(Level.SEVERE,
				"{0}: Error occurred responding to request: {1}",
				new Object[]{session.getId(), e});
	    }
	}
    }

    private void unsubscribeClient(Session session) {
	if (sessions != null && !sessions.isEmpty()) {
	    if (sessions.contains(session)) {
		sessions.remove(session);

		Logger.getLogger(WebSocketService.class
			.getName()).log(
				Level.INFO, "{0}: {1}",
				new Object[]{session.getId(), "Client unsubscribed"});

	    } else {
		Logger.getLogger(WebSocketService.class
			.getName()).log(
				Level.INFO, "{0}: {1}",
				new Object[]{session.getId(), "Client session not found in subscribers list"});
	    }
	}

	if (sessions != null && sessions.isEmpty()
		&& this.pollingService != null) {
	    this.pollingService.stop();

	    Logger.getLogger(WebSocketService.class.getName()).log(
		    Level.INFO, "Polling service stopped");
	}
    }
}
