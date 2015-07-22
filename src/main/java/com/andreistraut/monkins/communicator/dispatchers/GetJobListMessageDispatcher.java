package com.andreistraut.monkins.communicator.dispatchers;

import com.andreistraut.monkins.communicator.MessageRequest;
import com.andreistraut.monkins.communicator.MessageResponse;
import com.andreistraut.monkins.communicator.MessageType;
import com.andreistraut.monkins.communicator.WebSocketService;
import com.andreistraut.monkins.model.ConfigurationManager;
import com.andreistraut.monkins.model.PollingJob;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.websocket.Session;

public class GetJobListMessageDispatcher extends MessageDispatcher {

    private final WebSocketService service;
    private final Session session;
    private final MessageType type;

    public GetJobListMessageDispatcher(WebSocketService service, Session session, MessageType type) {
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
	MessageResponse response;
	
	try {
	    CopyOnWriteArrayList<PollingJob> jobs = ConfigurationManager.getInstance().getPollingJobs();
	    JsonArray jobsJson = new JsonArray();

	    for (PollingJob job : jobs) {
		jobsJson.add(job.toJson());
	    }

	    JsonObject responseData = new JsonObject();
	    responseData.add("jobs", jobsJson);

	    response = new MessageResponse(request.getCallbackId(), 200, true, "JobList", responseData);
	    respond(session, response);
	    
	    return true;
	} catch (Exception e) {
	    response = new MessageResponse(request.getCallbackId(), 200, true, "Error retrieving jobs" + e.getMessage());
	    respond(session, response);
	    
	    return false;
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
