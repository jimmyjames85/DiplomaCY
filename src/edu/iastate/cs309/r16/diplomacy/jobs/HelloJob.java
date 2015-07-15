package edu.iastate.cs309.r16.diplomacy.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {
	
	private static final Logger LOG = LoggerFactory.getLogger(HelloJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String gameId = dataMap.getString("gameId");
		LOG.debug("gameId:" + gameId + " " + context.getFireTime());

	}

}
