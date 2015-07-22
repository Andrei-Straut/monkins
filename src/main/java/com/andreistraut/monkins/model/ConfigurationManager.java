package com.andreistraut.monkins.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ConfigurationManager {

    private static ConfigurationManager _instance;
    private final String MONKINS_FOLDER = System.getProperty("user.home") + System.getProperty("file.separator") + ".monkins";
    private final String MONKINS_CONFIG_FILE = MONKINS_FOLDER + System.getProperty("file.separator") + "config.json";

    private String JSON_API_SUFFIX = "/api/json?depth=1";
    private String JSON_API_TEST_REPORT_SUFFIX = "/lastCompletedBuild/testReport/api/json"
	    + "?tree=duration,empty,failCount,skipCount,totalCount,passCount";

    private int numberOfColumns = 3;
    private int defaultJobDisplayHeight = 150;
    private int pollingIntervalMs = 60000;
    private int settingsPollingIntervalMs = 60000;
    private int failCountBeforeCancel = 20;

    private boolean displayDetailsForSuccessfulJobs = false;
    private boolean displayDetailsForUnstableJobs = true;
    private boolean displayDetailsForFailedJobs = true;

    private CopyOnWriteArrayList<PollingJob> pollingJobs = new CopyOnWriteArrayList<PollingJob>();

    private ConfigurationManager() throws IOException {
	String settings = this.getSettingsText(this.MONKINS_CONFIG_FILE);
	JsonObject settingsJson = this.getSettingsJson(settings);
	this.fromJson(settingsJson);
    }

    public static ConfigurationManager getInstance() throws IOException {
	if (_instance == null) {
	    _instance = new ConfigurationManager();
	    Logger.getLogger(ConfigurationManager.class.getName()).log(
		    Level.INFO, "CofigurationManager instantiated");
	}

	return _instance;
    }

    public ConfigurationManager fromJson(JsonObject settingsJson) throws IOException {

	if (settingsJson.has("jsonApiSuffix")) {
	    this.JSON_API_SUFFIX = settingsJson.get("jsonApiSuffix").getAsString();
	}

	if (settingsJson.has("jsonApiTestReportSuffix")) {
	    this.JSON_API_TEST_REPORT_SUFFIX = settingsJson.get("jsonApiTestReportSuffix").getAsString();
	}

	if (settingsJson.has("numberOfColumns")) {
	    this.numberOfColumns = settingsJson.get("numberOfColumns").getAsInt();
	}

	if (settingsJson.has("defaultJobDisplayHeight")) {
	    this.defaultJobDisplayHeight = settingsJson.get("defaultJobDisplayHeight").getAsInt();
	}

	if (settingsJson.has("pollingIntervalMs")) {
	    this.pollingIntervalMs = settingsJson.get("pollingIntervalMs").getAsInt();
	}

	if (settingsJson.has("settingsPollingIntervalMs")) {
	    this.settingsPollingIntervalMs = settingsJson.get("settingsPollingIntervalMs").getAsInt();
	}

	if (settingsJson.has("failCountBeforeCancel")) {
	    this.failCountBeforeCancel = settingsJson.get("failCountBeforeCancel").getAsInt();
	}

	if (settingsJson.has("displayDetailsForSuccessfulJobs")) {
	    this.displayDetailsForSuccessfulJobs = settingsJson.get("displayDetailsForSuccessfulJobs").getAsBoolean();
	}

	if (settingsJson.has("displayDetailsForUnstableJobs")) {
	    this.displayDetailsForUnstableJobs = settingsJson.get("displayDetailsForUnstableJobs").getAsBoolean();
	}

	if (settingsJson.has("displayDetailsForFailedJobs")) {
	    this.displayDetailsForFailedJobs = settingsJson.get("displayDetailsForFailedJobs").getAsBoolean();
	}

	if (settingsJson.has("urls")) {
	    JsonArray urls = settingsJson.get("urls").getAsJsonArray();

	    if (this.pollingJobs == null) {
		this.pollingJobs = new CopyOnWriteArrayList<PollingJob>();
	    }

	    if (urls.size() > 0 && !this.pollingJobs.isEmpty()) {
		this.pollingJobs.clear();
	    }

	    for (int i = 0; i < urls.size(); i++) {
		JsonObject jobJson = urls.get(i).getAsJsonObject();

		if (!jobJson.has("name") || !jobJson.has("url") || !jobJson.has("order")) {
		    Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING,
			    "Polling job setting incomplete {0}",
			    jobJson.toString());
		    continue;
		}

		PollingJob pollingJob = new PollingJob(
			jobJson.get("name").getAsString(),
			jobJson.get("url").getAsString(),
			jobJson.get("order").getAsInt());

		DisplayJob job = new DisplayJob(pollingJob.getName(), pollingJob.getUrl(), pollingJob.getOrder());
		pollingJob.setAssociatedJob(job);

		this.pollingJobs.add(pollingJob);
	    }
	}

	return this;
    }

    public boolean updateSettings(JsonObject settingsJson) throws IOException {
	this.fromJson(settingsJson);
	this.saveSettingsFile(MONKINS_CONFIG_FILE);

	Logger.getLogger(ConfigurationManager.class.getName()).log(Level.INFO,
		"Settings successfully updated {0}",
		settingsJson);

	return true;
    }

    public JsonObject toJson() throws IOException {
	return this.toJson(true);
    }

    public JsonObject toJson(boolean reloadFromDisk) throws IOException {
	if (reloadFromDisk) {
	    String settings = this.getSettingsText(this.MONKINS_CONFIG_FILE);
	    JsonObject settingsJson = this.getSettingsJson(settings);
	    this.fromJson(settingsJson);
	}

	JsonObject configJson = new JsonObject();

	configJson.addProperty("jsonApiSuffix", this.JSON_API_SUFFIX);
	configJson.addProperty("jsonApiTestReportSuffix", this.JSON_API_TEST_REPORT_SUFFIX);
	configJson.addProperty("numberOfColumns", this.numberOfColumns);
	configJson.addProperty("defaultJobDisplayHeight", this.defaultJobDisplayHeight);
	configJson.addProperty("pollingIntervalMs", this.pollingIntervalMs);
	configJson.addProperty("settingsPollingIntervalMs", this.settingsPollingIntervalMs);
	configJson.addProperty("failCountBeforeCancel", this.failCountBeforeCancel);
	configJson.addProperty("displayDetailsForSuccessfulJobs", this.displayDetailsForSuccessfulJobs);
	configJson.addProperty("displayDetailsForUnstableJobs", this.displayDetailsForUnstableJobs);
	configJson.addProperty("displayDetailsForFailedJobs", this.displayDetailsForFailedJobs);

	JsonArray urls = new JsonArray();
	for (PollingJob job : this.pollingJobs) {
	    urls.add(job.toJson(false));
	}

	configJson.add("urls", urls);

	return configJson;
    }

    private JsonObject getSettingsJson(String settingsText) throws IOException {
	try {
	    JsonObject settingsJson = (new JsonParser()).parse(settingsText).getAsJsonObject();

	    Logger.getLogger(ConfigurationManager.class.getName()).log(Level.INFO,
		    "Settings successfully converted to JSON {0}",
		    settingsText);
	    return settingsJson;
	} catch (Exception e) {
	    Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, 
		    "Settings could not be converted to JSON", e);
	    throw e;
	}
    }

    private String getSettingsText(String configFilePath) throws IOException {
	File configFile = new File(configFilePath);

	if (configFile.exists()) {
	    BufferedReader reader = null;
	    try {
		reader = new BufferedReader(new FileReader(configFile));
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
		    builder.append(line);
		    builder.append(System.lineSeparator());
		    line = reader.readLine();
		}
		
		if(builder.toString().trim().replace(" ", "").isEmpty()) {
		    throw new IOException("Empty settings file");
		}

		Logger.getLogger(ConfigurationManager.class.getName()).log(Level.INFO,
			"Settings file at location {0} successfully opened and read",
			configFile.getAbsolutePath());

		Logger.getLogger(ConfigurationManager.class.getName()).log(Level.INFO,
			builder.toString());

		return builder.toString();
	    } catch (IOException e) {
		Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING,
			"Settings file at given path {0} does not exist, is a directory, or is not readable",
			configFile.getAbsolutePath());
	    } finally {
		if (reader != null) {
		    reader.close();
		}
	    }
	} else {
	    configFile.getParentFile().mkdirs();
	    configFile.createNewFile();
	    this.saveSettingsFile(configFilePath);
	}

	return null;
    }

    private void saveSettingsFile(String configFilePath) throws IOException {
	File configFile = new File(configFilePath);

	configFile.getParentFile().mkdirs();

	if (configFile.delete()) {
	    try {
		configFile.createNewFile();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String contents = gson.toJson(this.toJson(false));

		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"));
		writer.write(contents);
		writer.close();

		java.util.logging.Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING,
			"Settings saved at path {0}", configFilePath);
	    } catch (IOException e) {
		java.util.logging.Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING,
			"Settings could not be written to file at path {0} due to error: {1}",
			new Object[]{configFilePath, e.getMessage()});
	    }
	}
    }

    public String getMONKINS_FOLDER() {
	return MONKINS_FOLDER;
    }

    public String getMONKINS_CONFIG_FILE() {
	return MONKINS_CONFIG_FILE;
    }

    public String getJSON_API_SUFFIX() {
	return JSON_API_SUFFIX;
    }

    public String getJSON_API_TEST_REPORT_SUFFIX() {
	return JSON_API_TEST_REPORT_SUFFIX;
    }

    public int getNumberOfColumns() {
	return numberOfColumns;
    }

    public int getDefaultJobDisplayHeight() {
	return defaultJobDisplayHeight;
    }

    public int getPollingIntervalMs() {
	return pollingIntervalMs;
    }

    public int getSettingsPollingIntervalMs() {
	return settingsPollingIntervalMs;
    }

    public int getFailCountBeforeCancel() {
	return failCountBeforeCancel;
    }

    public void setFailCountBeforeCancel(int failCountBeforeCancel) {
	this.failCountBeforeCancel = failCountBeforeCancel;
    }

    public boolean isDisplayDetailsForSuccessfulJobs() {
	return displayDetailsForSuccessfulJobs;
    }

    public boolean isDisplayDetailsForUnstableJobs() {
	return displayDetailsForUnstableJobs;
    }

    public boolean isDisplayDetailsForFailedJobs() {
	return displayDetailsForFailedJobs;
    }

    public CopyOnWriteArrayList<PollingJob> getPollingJobs() {
	return pollingJobs;
    }
}
