package com.andreistraut.monkins.communicator.dispatchers;

import com.andreistraut.monkins.communicator.MessageRequest;
import com.andreistraut.monkins.communicator.MessageResponse;
import com.andreistraut.monkins.communicator.MessageType;
import com.andreistraut.monkins.communicator.PollingService;
import com.andreistraut.monkins.communicator.WebSocketService;
import com.andreistraut.monkins.model.ConfigurationManager;
import java.util.ArrayList;
import javax.websocket.Session;

public class UpdateSettingsMessageDispatcher extends MessageDispatcher {

    private final WebSocketService service;
    private PollingService pollingService;
    private final Session session;
    private final MessageType type;

    public UpdateSettingsMessageDispatcher(WebSocketService service, PollingService pollingService, Session updater, MessageType type) {
	super(service, updater, type);
	this.pollingService = pollingService;
	this.service = service;
	this.session = updater;
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
	try {
	    ConfigurationManager.getInstance().updateSettings(request.getData());
	    pollingService.notifyUpdateConfig();
	    
	    respond(session, new MessageResponse(request.getCallbackId(), true, ConfigurationManager.getInstance().toJson()));
	    respond(session, new MessageResponse(request.getCallbackId(), 200, true, "UPDATESETTINGS",
		    ConfigurationManager.getInstance().toJson()));
	    respondAll(new MessageResponse(request.getCallbackId(), 200, true, "UPDATESETTINGS",
		    ConfigurationManager.getInstance().toJson()));

	} catch (Exception e) {
	    respond(session, new MessageResponse(request.getCallbackId(), 500, true, "Error updating settings: " + e.getMessage()));
	}

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

    public PollingService getPollingService() {
	return pollingService;
    }

    public void setPollingService(PollingService pollingService) {
	this.pollingService = pollingService;
    }
}
