package com.andreistraut.monkins.model;

import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayJob {

    private final String url;
    private String name;
    private String displayName;
    private String monitorDisplayName;
    private int listOrder;

    private boolean buildable;
    private boolean isBuilding;
    private int nextBuildNumber;
    private String lastBuildResult;
    private String lastStableBuildTimeStamp;
    private String lastSuccessfulBuildTimeStamp;
    private String lastFailedBuildTimeStamp;

    private int lastTestFailCount;
    private int lastTestPassCount;
    private int lastTestSkipCount;
    private int lastTestTotalCount;

    private boolean hasChanged;

    public DisplayJob(String url) {
	this.url = url;
    }

    public DisplayJob(String monitorDisplayName, String url) {
	this(url);
	this.monitorDisplayName = monitorDisplayName;
    }

    public DisplayJob(String monitorDisplayName, String url, int listOrder) {
	this(monitorDisplayName, url);
	this.listOrder = listOrder;
    }

    public DisplayJob updateInfo(JsonObject info) {
	if (info.has("name")
		&& (this.name == null || !this.name.equals(info.get("name").getAsString()))) {

	    this.name = info.get("name").getAsString();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "name", this.name});
	}

	if (info.has("displayName")
		&& (this.displayName == null || !this.displayName.equals(info.get("displayName").getAsString()))) {

	    this.displayName = info.get("displayName").getAsString();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "displayName", this.displayName});
	}

	if (info.has("buildable") && this.buildable != info.get("buildable").getAsBoolean()) {
	    this.buildable = info.get("buildable").getAsBoolean();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "buildable", this.buildable});
	}

	if (info.has("nextBuildNumber") && this.nextBuildNumber != info.get("nextBuildNumber").getAsInt()) {
	    this.nextBuildNumber = info.get("nextBuildNumber").getAsInt();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "nextBuildNumber", this.nextBuildNumber});
	}

	if (info.has("lastBuild") && !info.get("lastBuild").isJsonNull()) {
	    JsonObject lastBuild = info.get("lastBuild").getAsJsonObject();
	    if (lastBuild.has("building") && this.isBuilding != lastBuild.get("building").getAsBoolean()) {
		this.isBuilding = lastBuild.get("building").getAsBoolean();
		this.hasChanged = true;

		Logger.getLogger(DisplayJob.class
			.getName()).log(
				Level.INFO, "Info changed for job: {0}, {1}: {2}",
				new Object[]{this.monitorDisplayName, "building", this.isBuilding});
	    }
	}

	if (info.has("lastCompletedBuild") && !info.get("lastCompletedBuild").isJsonNull()) {
	    JsonObject lastCompletedBuild = info.get("lastCompletedBuild").getAsJsonObject();
	    if (lastCompletedBuild.has("result") 
		    && (this.lastBuildResult == null || !this.lastBuildResult.equals(lastCompletedBuild.get("result").getAsString()))) {
		this.lastBuildResult = lastCompletedBuild.get("result").getAsString();
		this.hasChanged = true;

		Logger.getLogger(DisplayJob.class
			.getName()).log(
				Level.INFO, "Info changed for job: {0}, {1}: {2}",
				new Object[]{this.monitorDisplayName, "lastBuildResult", this.lastBuildResult});
	    }
	}

	if (info.has("lastStableBuild") && !info.get("lastStableBuild").isJsonNull()) {
	    JsonObject lastStableBuild = info.get("lastStableBuild").getAsJsonObject();
	    if (lastStableBuild.has("result")
		    && validateCompare(this.lastStableBuildTimeStamp, this.getDisplayDate(lastStableBuild.get("timestamp").getAsString()))) {
		
		this.lastStableBuildTimeStamp = this.getDisplayDate(lastStableBuild.get("timestamp").getAsString());
		this.hasChanged = true;

		Logger.getLogger(DisplayJob.class
			.getName()).log(
				Level.INFO, "Info changed for job: {0}, {1}: {2}",
				new Object[]{this.monitorDisplayName, "lastStableBuildTimeStamp", this.lastStableBuildTimeStamp});
	    }
	}

	if (info.has("lastSuccessfulBuild") && !info.get("lastSuccessfulBuild").isJsonNull()) {
	    JsonObject lastSuccessfulBuild = info.get("lastSuccessfulBuild").getAsJsonObject();
	    if (lastSuccessfulBuild.has("result")
		    && validateCompare(this.lastSuccessfulBuildTimeStamp, this.getDisplayDate(lastSuccessfulBuild.get("timestamp").getAsString()))) {
		
		this.lastSuccessfulBuildTimeStamp = this.getDisplayDate(lastSuccessfulBuild.get("timestamp").getAsString());
		this.hasChanged = true;

		Logger.getLogger(DisplayJob.class
			.getName()).log(
				Level.INFO, "Info changed for job: {0}, {1}: {2}",
				new Object[]{this.monitorDisplayName, "lastSuccessfulBuildTimeStamp", this.lastSuccessfulBuildTimeStamp});
	    }
	}

	if (info.has("lastFailedBuild") && !info.get("lastFailedBuild").isJsonNull()) {
	    JsonObject lastFailedBuild = info.get("lastFailedBuild").getAsJsonObject();
	    if (lastFailedBuild.has("result")
		    && validateCompare(this.lastFailedBuildTimeStamp, this.getDisplayDate(lastFailedBuild.get("timestamp").getAsString()))) {
		
		this.lastFailedBuildTimeStamp = this.getDisplayDate(lastFailedBuild.get("timestamp").getAsString());
		this.hasChanged = true;

		Logger.getLogger(DisplayJob.class
			.getName()).log(
				Level.INFO, "Info changed for job: {0}, {1}: {2}",
				new Object[]{this.monitorDisplayName, "lastFailedBuildTimeStamp", this.lastFailedBuildTimeStamp});
	    }
	}

	if (info.has("failCount") && this.lastTestFailCount != info.get("failCount").getAsInt()) {
	    this.lastTestFailCount = info.get("failCount").getAsInt();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "lastTestFailCount", this.lastTestFailCount});
	}

	if (info.has("passCount") && this.lastTestPassCount != info.get("passCount").getAsInt()) {
	    this.lastTestPassCount = info.get("passCount").getAsInt();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "lastTestPassCount", this.lastTestPassCount});
	}

	if (info.has("skipCount") && this.lastTestSkipCount != info.get("skipCount").getAsInt()) {
	    this.lastTestSkipCount = info.get("skipCount").getAsInt();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "lastTestSkipCount", this.lastTestSkipCount});
	}

	if (info.has("totalCount") && this.lastTestTotalCount != info.get("totalCount").getAsInt()) {
	    this.lastTestTotalCount = info.get("totalCount").getAsInt();
	    this.hasChanged = true;

	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.INFO, "Info changed for job: {0}, {1}: {2}",
			    new Object[]{this.monitorDisplayName, "lastTestTotalCount", this.lastTestTotalCount});
	}

	return this;
    }

    public JsonObject toJson() {
	JsonObject info = new JsonObject();

	info.addProperty("url", this.url);
	info.addProperty("name", this.name);
	info.addProperty("displayName", this.displayName);
	info.addProperty("monitorDisplayName", this.monitorDisplayName);
	info.addProperty("listOrder", this.listOrder);
	info.addProperty("buildable", this.buildable);
	info.addProperty("isBuilding", this.isBuilding);
	info.addProperty("nextBuildNumber", this.nextBuildNumber);
	info.addProperty("lastBuildResult", this.lastBuildResult);
	info.addProperty("lastStableBuildTimeStamp", this.lastStableBuildTimeStamp);
	info.addProperty("lastSuccessfulBuildTimeStamp", this.lastSuccessfulBuildTimeStamp);
	info.addProperty("lastFailedBuildTimeStamp", this.lastFailedBuildTimeStamp);
	info.addProperty("lastTestFailCount", this.lastTestFailCount);
	info.addProperty("lastTestPassCount", this.lastTestPassCount);
	info.addProperty("lastTestSkipCount", this.lastTestSkipCount);
	info.addProperty("lastTestTotalCount", this.lastTestTotalCount);
	info.addProperty("hasChanged", this.hasChanged);

	return info;
    }
    
    private boolean validateCompare(String currentTimestamp, String jsonTimestamp) {
	return (currentTimestamp == null && jsonTimestamp != null) || 
		(currentTimestamp != null && !currentTimestamp.equals(jsonTimestamp));
    }

    private String getDisplayDate(String timestamp) {
	try {
	    Date date = new java.util.Date(
		    Long.parseLong(timestamp));
	    SimpleDateFormat targetDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.ENGLISH);
	    return targetDateFormat.format(date);
	} catch (NumberFormatException e) {
	    Logger.getLogger(DisplayJob.class
		    .getName()).log(
			    Level.WARNING, "Could not parse timestamp for input: {0}",
			    new Object[]{timestamp});
	}
	
	return null;
    }

    public String getUrl() {
	return name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getMonitorDisplayName() {
	return monitorDisplayName;
    }

    public void setMonitorDisplayName(String monitorDisplayName) {
	this.monitorDisplayName = monitorDisplayName;
    }

    public int getListOrder() {
	return listOrder;
    }

    public void setListOrder(int listOrder) {
	this.listOrder = listOrder;
    }

    public boolean isBuildable() {
	return buildable;
    }

    public void setBuildable(boolean buildable) {
	this.buildable = buildable;
    }

    public boolean isBuilding() {
	return isBuilding;
    }

    public void setIsBuilding(boolean isBuilding) {
	this.isBuilding = isBuilding;
    }

    public int getNextBuildNumber() {
	return nextBuildNumber;
    }

    public void setNextBuildNumber(int nextBuildNumber) {
	this.nextBuildNumber = nextBuildNumber;
    }

    public String getLastBuildResult() {
	return lastBuildResult;
    }

    public void setLastBuildResult(String lastBuildResult) {
	this.lastBuildResult = lastBuildResult;
    }

    public String getLastStableBuildTimeStamp() {
	return lastStableBuildTimeStamp;
    }

    public void setLastStableBuildTimeStamp(String lastStableBuildTimeStamp) {
	this.lastStableBuildTimeStamp = lastStableBuildTimeStamp;
    }

    public String getLastSuccessfulBuildTimeStamp() {
	return lastSuccessfulBuildTimeStamp;
    }

    public void setLastSuccessfulBuildTimeStamp(String lastSuccessfulBuildTimeStamp) {
	this.lastSuccessfulBuildTimeStamp = lastSuccessfulBuildTimeStamp;
    }

    public String getLastFailedBuildTimeStamp() {
	return lastFailedBuildTimeStamp;
    }

    public void setLastFailedBuildTimeStamp(String lastFailedBuildTimeStamp) {
	this.lastFailedBuildTimeStamp = lastFailedBuildTimeStamp;
    }

    public int getLastTestFailCount() {
	return lastTestFailCount;
    }

    public void setLastTestFailCount(int lastTestFailCount) {
	this.lastTestFailCount = lastTestFailCount;
    }

    public int getLastTestPassCount() {
	return lastTestPassCount;
    }

    public void setLastTestPassCount(int lastTestPassCount) {
	this.lastTestPassCount = lastTestPassCount;
    }

    public int getLastTestSkipCount() {
	return lastTestSkipCount;
    }

    public void setLastTestSkipCount(int lastTestSkipCount) {
	this.lastTestSkipCount = lastTestSkipCount;
    }

    public int getLastTestTotalCount() {
	return lastTestTotalCount;
    }

    public void setLastTestTotalCount(int lastTestTotalCount) {
	this.lastTestTotalCount = lastTestTotalCount;
    }

    public boolean hasChanged() {
	return this.hasChanged;
    }

    public void setHasChanged(boolean hasChanged) {
	this.hasChanged = hasChanged;
    }
}
