package com.andreistraut.monkins.communicator.dispatchers;

import com.andreistraut.monkins.communicator.MessageRequest;
import com.andreistraut.monkins.communicator.MessageResponse;
import com.andreistraut.monkins.communicator.MessageType;
import com.andreistraut.monkins.communicator.WebSocketService;
import com.andreistraut.monkins.model.ConfigurationManager;
import java.util.ArrayList;
import javax.websocket.Session;

public class GetSettingsMessageDispatcher extends MessageDispatcher {

    private final WebSocketService service;
    private final Session session;
    private final MessageType type;

    public GetSettingsMessageDispatcher(WebSocketService service, Session session, MessageType type) {
	super(service, session, type);
	this.service = service;
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
	respond(this.session, new MessageResponse(this.request.getCallbackId(), true, ConfigurationManager.getInstance().toJson(true)));
	
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
