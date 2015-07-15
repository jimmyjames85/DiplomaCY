package edu.iastate.cs309.r16.diplomacy.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iastate.cs309.r16.diplomacy.games.GameManager;

public class ProcessTurnJob implements Job 
{
	private static final Logger LOG = LoggerFactory.getLogger(HelloJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{	
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		long gameId = (Long) dataMap.get("gameId");
		GameManager.proccessTurn(gameId);
		LOG.debug("gameId:" + gameId + " " + context.getFireTime());

	}

}