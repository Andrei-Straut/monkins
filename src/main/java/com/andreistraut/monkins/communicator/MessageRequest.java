
package com.andreistraut.monkins.communicator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MessageRequest {

    private int callbackId;
    private MessageType type;
    private JsonObject data;

    public MessageRequest(String request) throws JsonSyntaxException {
	JsonObject requestJson = (new JsonParser()).parse(request).getAsJsonObject();
	this.fromJson(requestJson);
    }

    public MessageRequest(JsonObject requestJson) {
	this.fromJson(requestJson);
    }

    private void fromJson(JsonObject json) {
	
	//Validate and explain why the validation failed	
	if(json == null || json.isJsonNull()) {
	    throw new NullPointerException("Request cannot be empty");
	}
	
	if(!json.has("callback_id")) {
	    throw new NullPointerException("Request must contain a callback_id");
	}
	
	if(!json.has("type")) {
	    throw new NullPointerException("Request must contain a message type");
	}
	
	if(!json.has("data")) {
	    throw new NullPointerException("Request must contain a request data (even if empty)");
	}
	
	this.callbackId = json.get("callback_id").getAsInt();
	
	try {
	    this.type = MessageType.valueOf(json.get("type").getAsString().trim().toUpperCase());
	} catch(IllegalArgumentException e) {
	    this.type = MessageType.UNKNOWN;
	}
	
	this.data = json.get("data").getAsJsonObject();
    }

    public int getCallbackId() {
	return callbackId;
    }

    public MessageType getType() {
	return this.type;
    }

    public JsonObject getData() {
	return data;
    }

    public String toJsonString() {
	return this.toJson().toString();
    }

    public JsonObject toJson() {
	JsonObject request = new JsonObject();

	request.addProperty("callback_id", this.callbackId);
	request.addProperty("type", this.type.toString());
	request.add("data", this.data);

	return request;
    }
}
