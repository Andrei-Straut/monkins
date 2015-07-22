package com.andreistraut.monkins.communicator.dispatchers;

import com.andreistraut.monkins.communicator.WebSocketService;
import com.andreistraut.monkins.communicator.MessageRequest;
import com.andreistraut.monkins.communicator.MessageResponse;
import com.andreistraut.monkins.communicator.MessageType;
import java.util.ArrayList;
import javax.websocket.Session;

public abstract class MessageDispatcher {

    protected MessageRequest request;
    protected ArrayList<Object> parameters;
    protected boolean sendUpdates = true;

    public MessageDispatcher(WebSocketService service, Session session, MessageType type) {
    }

    public MessageDispatcher(WebSocketService service, ArrayList<Session> sessions, MessageType type) {
    }

    /**
     * Enable or disable communications from server. Setting this parameter to False
     * will cause the server to go silent. Introduced mainly to help with testing
     * 
     * @param sendUpdates True to enable communication from server, false otherwise
     */
    public void setSendUpdates(boolean sendUpdates) {
        this.sendUpdates = sendUpdates;
    }

    /**
     * Validates the MessageRequest given to this class for processing, and sets
     * it as a property if valid
     *
     * @param request The MessageRequest to process
     *
     * @return True if request is valid, false otherwise, Exception for
     * malformed request
     * @throws java.lang.Exception
     */
    abstract boolean setRequest(MessageRequest request) throws Exception;

    /**
     * If there are additional parameters the class needs to do its job, initializes
     * them here
     *
     * @param parameters Additional parameters / preconditions the dispatcher
     * may need
     * @throws java.lang.Exception
     */
    abstract void setParameters(ArrayList<Object> parameters) throws Exception;

    abstract boolean process() throws Exception;

    abstract void respond(Session session, MessageResponse response);
    abstract void respondAll(MessageResponse response);
}
