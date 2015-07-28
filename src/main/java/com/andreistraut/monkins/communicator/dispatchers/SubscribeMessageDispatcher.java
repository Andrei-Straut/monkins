package com.andreistraut.monkins.communicator.dispatchers;

import com.andreistraut.monkins.communicator.MessageRequest;
import com.andreistraut.monkins.communicator.MessageResponse;
import com.andreistraut.monkins.communicator.MessageType;
import com.andreistraut.monkins.communicator.PollingService;
import com.andreistraut.monkins.communicator.WebSocketService;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

public class SubscribeMessageDispatcher extends MessageDispatcher {

    private final WebSocketService service;
    private PollingService pollingService;
    private Session session;
    private final MessageType type;

    public SubscribeMessageDispatcher(WebSocketService service, PollingService pollingService, 
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
	if (this.service.getSessions() == null) {
	    this.service.initSessions();
	}

	if (!this.service.getSessions().isEmpty() && this.service.getSessions().contains(session)) {
	    this.service.getSessions().remove(session);
	}

	this.service.getSessions().add(session);
	Logger.getLogger(WebSocketService.class.getName()).log(
		Level.INFO, "{0}: {1}",
		new Object[]{session.getId(), "Added to subscribers list"});

	this.pollingService.start();

	respond(session, new MessageResponse(this.request.getCallbackId(), false, new JsonObject()));
	
	return true;
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
