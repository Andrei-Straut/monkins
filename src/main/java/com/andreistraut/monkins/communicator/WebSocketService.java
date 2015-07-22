package com.andreistraut.monkins.communicator;

import com.andreistraut.monkins.communicator.dispatchers.MessageDispatcher;
import com.andreistraut.monkins.communicator.dispatchers.MessageDispatcherFactory;
import com.andreistraut.monkins.communicator.dispatchers.UnsubscribeMessageDispatcher;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
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
    private PollingService pollingService;

    @OnOpen
    public void onOpen(Session session) throws IOException {
	Logger.getLogger(WebSocketService.class.getName()).log(
		Level.INFO, "{0}: {1}",
		new Object[]{session.getId(), "Connection opened"});

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
	} catch (NullPointerException e) {
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

	MessageDispatcherFactory dispatcherFactory = new MessageDispatcherFactory(this, session, pollingService);
	dispatcherFactory.setSessions(sessions);
	
	MessageDispatcher dispatcher = dispatcherFactory.getDispatcher(request.getType());
	dispatcherFactory.initDispatcherRequest(dispatcher, request);
	dispatcherFactory.process(dispatcher);
    }

    @OnClose
    public void onClose(Session session) throws IOException {

	Logger.getLogger(WebSocketService.class
		.getName()).log(
			Level.INFO, "{0}: {1}",
			new Object[]{session.getId(), "Connection closed"});

	MessageDispatcherFactory dispatcherFactory = new MessageDispatcherFactory(this, session, pollingService);
	dispatcherFactory.setSessions(sessions);
	UnsubscribeMessageDispatcher unsubscriber = new UnsubscribeMessageDispatcher(this, pollingService, session, MessageType.UNSUBSCRIBE);
	try {
	    dispatcherFactory.process(unsubscriber);
	    Logger.getLogger(WebSocketService.class.getName()).log(Level.SEVERE, "Session with id {0} unsubscribed. Open sessions left: {1}", 
		    new Object[] {session.getId(), sessions.size()});
	} catch (Exception ex) {
	    Logger.getLogger(WebSocketService.class.getName()).log(Level.SEVERE, "Error occurred unsubscribing client on close event", ex);
	}
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
	if (sessions == null || sessions.isEmpty()) {
	    return;
	}

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

    public static ArrayList<Session> getSessions() {
	return sessions;
    }

    public static void initSessions() {
	sessions = new ArrayList<Session>();
    }
}
