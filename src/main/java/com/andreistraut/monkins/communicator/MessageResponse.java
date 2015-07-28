package com.andreistraut.monkins.communicator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletResponse;

public class MessageResponse {

    private int callbackId;
    private int status = HttpServletResponse.SC_OK;
    private String description = "Ok";
    private boolean isEnded = true;
    private JsonElement data = new JsonObject();

    public MessageResponse(int callbackId) {
	this.callbackId = callbackId;
    }

    public MessageResponse(int callbackId, boolean isEnded, JsonElement data) {
	this(callbackId);
	this.data = data;
	this.isEnded = isEnded;
    }

    public MessageResponse(int callbackId, int status, boolean isEnded, String description) {
	this(callbackId);
	this.status = status;
	this.isEnded = isEnded;
	this.description = description;
    }

    public MessageResponse(int callbackId, int status, boolean isEnded, String description, JsonElement data) {
	this(callbackId);
	this.status = status;
	this.isEnded = isEnded;
	this.description = description;
	this.data = data;
    }

    public int getCallbackId() {
	return callbackId;
    }

    public int getStatus() {
	return this.status;
    }

    public MessageResponse setStatus(int status) {
	this.status = status;
	return this;
    }

    public String getDescription() {
	return description;
    }

    public MessageResponse setDescription(String description) {
	this.description = description;
	return this;
    }

    public JsonElement getData() {
	return data;
    }

    public MessageResponse setData(JsonElement data) {
	this.data = data;
	return this;
    }

    public boolean isEnded() {
	return isEnded;
    }

    public MessageResponse setIsEnded(boolean isEnded) {
	this.isEnded = isEnded;
	return this;
    }

    public String toJsonString() {
	return this.toJson().toString();
    }

    public JsonObject toJson() {
	JsonObject response = new JsonObject();

	response.addProperty("callback_id", this.callbackId);
	response.addProperty("status", this.status);
	response.addProperty("isEnded", this.isEnded);
	response.addProperty("description", this.description);
	response.add("data", this.data);

	return response;
    }
}
