package com.andreistraut.monkins.communicator;

import com.andreistraut.monkins.model.ConfigurationManager;
import com.andreistraut.monkins.model.PollingJob;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PollingService {

    private ConfigurationManager config;
    Timer timer;
    TimerTask task;

    public PollingService() throws IOException {
	this.config = ConfigurationManager.getInstance();
    }

    public void updateConfig(ConfigurationManager config) throws Exception {
	this.stop();
	this.config = config;
	this.start();
    }

    public void start() throws Exception {
	
	if(this.task != null && this.timer != null) {
	    return;
	}

	if (config.getPollingJobs() == null || config.getPollingJobs().isEmpty()) {
	    throw new Exception("No polling jobs found");
	}

	Logger.getLogger(ConfigurationManager.class.getName()).log(Level.INFO,
		"Starting polling service");

	for (PollingJob job : config.getPollingJobs()) {
	    job.setIsCancelled(false);
	    job.setCanPoll(true);
	}

	this.task = new TimerTask() {
	    @Override
	    public void run() {
		for (PollingJob job : config.getPollingJobs()) {
		    try {
			job.update();
		    } catch (IOException ex) {
			Logger.getLogger(PollingService.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	    }
	};
	
	this.timer = new Timer();
	this.timer.scheduleAtFixedRate(task, 1000, 10 * 1000);
    }

    public void stop() {
	Logger.getLogger(ConfigurationManager.class.getName()).log(Level.INFO,
		"Stopping polling service");

	for (PollingJob job : config.getPollingJobs()) {
	    job.setIsCancelled(true);
	    job.setCanPoll(false);
	}
	
	if(this.task != null) {
	    this.task.cancel();
	    this.task = null;
	}

	if (this.timer != null) {
	    this.timer.cancel();
	    this.timer = null;
	}
    }
}
