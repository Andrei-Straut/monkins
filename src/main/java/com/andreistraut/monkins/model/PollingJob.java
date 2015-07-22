package com.andreistraut.monkins.model;

import com.andreistraut.monkins.communicator.MessageResponse;
import com.andreistraut.monkins.communicator.WebSocketService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PollingJob {

    private boolean isPolling = false;
    private boolean isCancelled = false;
    private boolean canPoll = true;

    private final int TIMEOUT_MILLIS = 30000;

    private String url;
    private String name;
    private int order;

    private DisplayJob associatedJob;

    private String status;
    private int failCount = 0;
    private int detailsFailCount = 0;

    public PollingJob() {
    }

    public PollingJob(String name, String url, int order) {
	this.name = name;
	this.url = url;
	this.order = order;
    }

    public PollingJob(JsonObject pollingJobJson) {
	this.name = pollingJobJson.get("name").getAsString();
	this.url = pollingJobJson.get("url").getAsString();
	this.order = pollingJobJson.get("order").getAsInt();
    }

    public JsonObject toJson() {
	return this.toJson(true);
    }

    public JsonObject toJson(boolean includeChildrenAndInfo) {
	JsonObject object = new JsonObject();

	object.addProperty("name", this.name);
	object.addProperty("url", this.url);
	object.addProperty("order", this.order);

	if (includeChildrenAndInfo) {
	    object.addProperty("isCancelled", this.isCancelled);
	    object.addProperty("status", this.status);
	    object.addProperty("failCount", this.failCount);
	    object.add("associatedJob", this.associatedJob.toJson());
	}

	return object;
    }

    public PollingJob fromJson(JsonObject object) {
	this.name = object.get("name").getAsString();
	this.url = object.get("url").getAsString();
	this.order = object.get("order").getAsInt();

	return this;
    }

    private JsonObject retrieve(String url) throws ProtocolException, MalformedURLException, IOException {

	Logger.getLogger(ConfigurationManager.class.getName()).log(Level.FINER,
		"Retrieving info from url: {0}",
		url);

	URL target = new URL(url);
	HttpURLConnection request = (HttpURLConnection) target.openConnection();
	request.setConnectTimeout(TIMEOUT_MILLIS);
	request.setReadTimeout(TIMEOUT_MILLIS);
	request.setRequestMethod("GET");
	request.connect();

	Logger.getLogger(ConfigurationManager.class.getName()).log(Level.FINER,
		"Connection successful");

	JsonParser jp = new JsonParser();
	JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	JsonObject info = root.getAsJsonObject();

	Logger.getLogger(ConfigurationManager.class.getName()).log(Level.FINER,
		"Info parsed: {0}",
		info.toString());

	return info;
    }

    public void update() throws IOException {

	Logger.getLogger(PollingJob.class.getName()).log(Level.FINER,
		"Updating info for job {0}",
		new Object[]{this.name});

	if (!isPolling && !isCancelled && canPoll) {
	    this.isPolling = true;
	    this.canPoll = false;

	    if (getFailCount() < ConfigurationManager.getInstance().getFailCountBeforeCancel()
		    && ConfigurationManager.getInstance().getFailCountBeforeCancel() > 0) {

		try {
		    Logger.getLogger(PollingJob.class.getName()).log(Level.INFO,
			    "Retrieving info from url {0}",
			    new Object[]{this.url});

		    JsonObject jobJson = this.retrieve(this.url + ConfigurationManager.getInstance().getJSON_API_SUFFIX());
		    this.associatedJob.updateInfo(jobJson);

		    if (getDetailsFailCount() < ConfigurationManager.getInstance().getFailCountBeforeCancel()
			    && ConfigurationManager.getInstance().getFailCountBeforeCancel() > 0) {
			try {
			    JsonObject jobCompletedBuildTestReportJson = this.retrieve(this.url + ConfigurationManager.getInstance().getJSON_API_TEST_REPORT_SUFFIX());
			    this.associatedJob.updateInfo(jobCompletedBuildTestReportJson);
			} catch (Exception e) {
			    Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING,
				    "Could not update build / test result details for job {0}: {1}",
				    new Object[]{this.name, e.getMessage()});
			    this.increaseDetailsFailCount();
			}
		    } else if (getDetailsFailCount() == ConfigurationManager.getInstance().getFailCountBeforeCancel()) {
			Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING,
				"Polling job details with url {0} cancelled due to errors. No further requests will be made",
				new Object[]{this.url + ConfigurationManager.getInstance().getJSON_API_TEST_REPORT_SUFFIX()});
			this.increaseDetailsFailCount();
		    }

		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    String contents = gson.toJson(this.associatedJob.toJson());

		    if (this.associatedJob.hasChanged()) {
			Logger.getLogger(PollingJob.class.getName()).log(Level.INFO, "Info for job {0} updated", this.name);

			MessageResponse response = new MessageResponse(0, 200, false, "UPDATEJOB", this.toJson());
			WebSocketService.respondAll(response);
			this.associatedJob.setHasChanged(false);
		    }
		} catch (FileNotFoundException ex) {
		    Logger.getLogger(PollingJob.class.getName()).log(Level.SEVERE, "Error retrieving info for polling job", ex);

		    this.status = "Job URL not found, please re-check settings";
		    this.increaseFailCount();

		    MessageResponse response = new MessageResponse(0, 500, false, "ERROR", this.toJson());
		    WebSocketService.respondAll(response);

		} catch (Exception ex) {
		    Logger.getLogger(PollingJob.class.getName()).log(Level.SEVERE, "Error retrieving info for polling job", ex);

		    this.status = ex.getClass().toString() + ": " + ex.getMessage();
		    this.increaseFailCount();

		    MessageResponse response = new MessageResponse(0, 500, false, "ERROR", this.toJson());
		    WebSocketService.respondAll(response);

		} finally {
		    this.isPolling = false;
		    this.canPoll = true;
		}

	    } else if (!this.isCancelled) {

		this.isCancelled = true;
		Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING,
			"Polling job with url {0} cancelled due to errors. No further requests will be made. Last error was {1}",
			new Object[]{this.url, this.status});

		MessageResponse response = new MessageResponse(0, 500, false, "JOB_CANCELED", this.toJson());
		WebSocketService.respondAll(response);

		ConfigurationManager.getInstance().getPollingJobs().remove(this);
	    }

	}

	Logger.getLogger(PollingJob.class.getName()).log(Level.FINER,
		"Job {0} update done",
		new Object[]{this.name});
    }

    public String getUrl() {
	return url;
    }

    public String getName() {
	return name;
    }

    public int getOrder() {
	return order;
    }

    public boolean isCancelled() {
	return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
	this.isCancelled = isCancelled;
    }

    public boolean isPolling() {
	return isPolling;
    }

    public boolean canPoll() {
	return canPoll;
    }

    public void setCanPoll(boolean canPoll) {
	this.canPoll = canPoll;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public int getFailCount() {
	return failCount;
    }

    public void increaseFailCount() {
	this.failCount = failCount + 1;
    }

    public int getDetailsFailCount() {
	return detailsFailCount;
    }

    public void increaseDetailsFailCount() {
	this.detailsFailCount = detailsFailCount + 1;
    }

    public DisplayJob getAssociatedJob() {
	return associatedJob;
    }

    public void setAssociatedJob(DisplayJob associatedJob) {
	this.associatedJob = associatedJob;
    }
}
