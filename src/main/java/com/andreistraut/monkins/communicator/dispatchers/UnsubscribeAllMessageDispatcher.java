package com.andreistraut.monkins.communicator.dispatchers;

import com.andreistraut.monkins.communicator.MessageRequest;
import com.andreistraut.monkins.communicator.MessageResponse;
import com.andreistraut.monkins.communicator.MessageType;
import com.andreistraut.monkins.communicator.PollingService;
import com.andreistraut.monkins.communicator.WebSocketService;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.Session;

public class UnsubscribeAllMessageDispatcher extends MessageDispatcher {

    private final WebSocketService service;
    private PollingService pollingService;
    private Session session;
    private final MessageType type;

    public UnsubscribeAllMessageDispatcher(WebSocketService service, PollingService pollingService,
	    Session session, MessageType type) {

	super(service, session, type);
	this.service = service;
	this.pollingService = pollingService;
	this.session = session;
	this.type = type;
    }

    @Override
    boolean setRequest(MessageRequest request) throws Exception {
	if (request == null) {
	    throw new Exception("Request invalid, missing data");
	}

	this.request = request;

	return true;
    }

    @Override
    void setParameters(ArrayList<Object> parameters) throws Exception {
    }

    @Override
    boolean process() throws Exception {
	if (this.service.getSessions() != null && !this.service.getSessions().isEmpty()) {
	    for (Session clientSession : this.service.getSessions()) {
		unsubscribeClient(clientSession);
	    }
	}

	this.service.getSessions().clear();
	this.pollingService.stop();

	respond(session, new MessageResponse(request.getCallbackId(), true, new JsonObject()));

	return true;
    }

    private void unsubscribeClient(Session clientSession) throws IOException {
	if (this.service.getSessions() != null && !this.service.getSessions().isEmpty()) {
	    if (this.service.getSessions().contains(clientSession)) {
		clientSession.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Close requested"));
		this.service.getSessions().remove(clientSession);

		Logger.getLogger(WebSocketService.class
			.getName()).log(Level.INFO, "{0}: {1}",
				new Object[]{clientSession.getId(), "Client unsubscribed"});

	    } else {
		Logger.getLogger(WebSocketService.class
			.getName()).log(Level.INFO, "{0}: {1}",
				new Object[]{clientSession.getId(), "Client session not found in subscribers list"});
	    }
	}

	if (this.service.getSessions() != null && this.service.getSessions().isEmpty()
		&& this.pollingService != null) {
	    this.pollingService.stop();

	    Logger.getLogger(WebSocketService.class.getName()).log(
		    Level.INFO, "Polling service stopped");
	}
    }

    @Override
    void respond(Session session, MessageResponse response) {
	service.respond(session, response);
    }

    @Override
    void respondAll(MessageResponse response) {
	service.respondAll(response);
    }

}
