package com.andreistraut.monkins.communicator;

import com.andreistraut.monkins.model.ConfigurationManager;
import com.andreistraut.monkins.model.PollingJob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PollingService {

    private Timer timer;
    private ArrayList<TimerTask> tasks;

    public PollingService() throws IOException {
    }

    public void notifyUpdateConfig() throws Exception {
	this.stop();
	this.start();
    }

    public void start() throws Exception {

	if (this.tasks != null && !this.tasks.isEmpty() && this.timer != null) {
	    for (PollingJob job : ConfigurationManager.getInstance().getPollingJobs()) {
		MessageResponse response = new MessageResponse(0, 200, false, "UPDATE", job.toJson());
		WebSocketService.respondAll(response);
	    }

	    return;
	}

	if (ConfigurationManager.getInstance().getPollingJobs() == null || ConfigurationManager.getInstance().getPollingJobs().isEmpty()) {
	    throw new Exception("No polling jobs found");
	}

	Logger.getLogger(ConfigurationManager.class.getName()).log(Level.INFO,
		"Starting polling service");

	for (PollingJob job : ConfigurationManager.getInstance().getPollingJobs()) {
	    job.setIsCancelled(false);
	    job.setCanPoll(true);
	}

	if (this.tasks == null) {
	    this.tasks = new ArrayList<TimerTask>();
	}

	for (final PollingJob job : ConfigurationManager.getInstance().getPollingJobs()) {
	    this.tasks.add(new TimerTask() {
		@Override
		public void run() {
		    try {
			job.update();
		    } catch (IOException ex) {
			Logger.getLogger(PollingService.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	    });
	}

	this.timer = new Timer();

	for (TimerTask task : this.tasks) {
	    this.timer.scheduleAtFixedRate(task,
		    1000, 10 * 1000);
	}
    }

    public void stop() throws IOException {
	Logger.getLogger(ConfigurationManager.class
		.getName()).log(Level.INFO,
			"Stopping polling service");

	for (PollingJob job : ConfigurationManager.getInstance().getPollingJobs()) {
	    job.setIsCancelled(true);
	    job.setCanPoll(false);
	}

	if (this.tasks != null && !this.tasks.isEmpty()) {
	    for (TimerTask task : this.tasks) {
		if (task != null) {
		    task.cancel();
		}
	    }
	    
	    this.tasks.clear();
	}

	if (this.timer != null) {
	    this.timer.cancel();
	    this.timer = null;
	}
    }
}
