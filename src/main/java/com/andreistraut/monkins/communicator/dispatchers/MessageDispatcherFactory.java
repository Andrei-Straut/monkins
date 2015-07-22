package com.andreistraut.monkins.communicator.dispatchers;

import com.andreistraut.monkins.communicator.WebSocketService;
import com.andreistraut.monkins.communicator.MessageRequest;
import com.andreistraut.monkins.communicator.MessageType;
import com.andreistraut.monkins.communicator.PollingService;
import java.util.ArrayList;
import javax.websocket.Session;

public class MessageDispatcherFactory {

    private final WebSocketService service;
    private PollingService pollingService;
    private Session session;
    private ArrayList<Session> sessions;

    public MessageDispatcherFactory(WebSocketService service, Session session) {
	this.service = service;
	this.session = session;
	this.pollingService = null;
    }
    
    public MessageDispatcherFactory(WebSocketService service, Session session, PollingService pollingService) {
	this.service = service;
	this.session = session;
	this.pollingService = pollingService;
    }

    public MessageDispatcher getDispatcher(MessageType messageType) throws Exception {
	if (messageType == null) {
	    throw new NullPointerException("Message Type cannot be null");
	}

	MessageDispatcher dispatcher;

	switch (messageType) {
	    case GETSETTINGS: {
		dispatcher = new GetSettingsMessageDispatcher(service, session, messageType);
		break;
	    }
	    case UPDATESETTINGS: {
		dispatcher = new UpdateSettingsMessageDispatcher(service, pollingService, session, messageType);
		break;
	    }
	    case GETJOBSLIST: {
		dispatcher = new GetJobListMessageDispatcher(service, session, messageType);
		break;
	    }
	    case SUBSCRIBE: {
		dispatcher = new SubscribeMessageDispatcher(service, pollingService, session, messageType);
		break;
	    }
	    case UNSUBSCRIBE: {
		dispatcher = new UnsubscribeMessageDispatcher(service, pollingService, session, messageType);
		break;
	    }
	    case UNSUBSCRIBEALL: {
		dispatcher = new UnsubscribeAllMessageDispatcher(service, pollingService, session, messageType);
		break;
	    }
	    case UNKNOWN:
	    default: {
		throw new Exception("Message Type Unknown");
	    }
	}

	return dispatcher;
    }

    public void initDispatcherRequest(MessageDispatcher dispatcher, MessageRequest request) throws Exception {
	dispatcher.setRequest(request);
    }

    public void process(MessageDispatcher dispatcher) throws Exception {
	dispatcher.process();
    }

    public PollingService getPollingService() {
	return pollingService;
    }

    public void setPollingService(PollingService pollingService) {
	this.pollingService = pollingService;
    }

    public ArrayList<Session> getSessions() {
	return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
	this.sessions = sessions;
    }
}
