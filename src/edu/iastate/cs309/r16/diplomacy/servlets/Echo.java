package edu.iastate.cs309.r16.diplomacy.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iastate.cs309.r16.diplomacy.jobs.HelloJob;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.*;

public class Echo extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(Echo.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String gameId = request.getParameter("gameId");
		String whatDo = request.getParameter("todo");
		
		LOG.debug("got " + gameId);
		
		SchedulerFactory sf = new StdSchedulerFactory();
		if("trigger".equals(whatDo))
		{
			LOG.debug("triggering " + gameId);
			try {
				Trigger t = sf.getScheduler().getTrigger(new TriggerKey("trigger" + gameId,"group1"));
				Trigger s = t.getTriggerBuilder().build();
				sf.getScheduler().triggerJob(t.getJobKey());
				LOG.debug(t.toString());
				LOG.debug(s.toString());
				sf.getScheduler().rescheduleJob(t.getKey(), s);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("next".equals(whatDo))
		{
			LOG.debug("nexting " + gameId);
			try {
				Scheduler scheduler = sf.getScheduler();
				Date d = scheduler.getTrigger(new TriggerKey("trigger" + gameId,"group1")).getNextFireTime();
				LOG.debug("date returned:" + d.toString());
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("destroy".equals(whatDo))
		{
			LOG.debug("destroying " + gameId);
			try {
				Scheduler scheduler = sf.getScheduler();
				scheduler.unscheduleJob(new TriggerKey("trigger" + gameId,"group1"));
				scheduler.deleteJob(new JobKey("hello" + gameId, "group1"));
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("create".equals(whatDo))
		{
			
			LOG.debug("starting " + gameId);
			JobDetail job = newJob(HelloJob.class)
				    .withIdentity("hello" + gameId , "group1")
				    .usingJobData("gameId", gameId)
				    .build();
			
			Trigger trigger = newTrigger()
				    .withIdentity("trigger" + gameId, "group1")
				    //.startAt(myTimeToStartFiring)  // if a start time is not given (if this line were omitted), "now" is implied
				    //.withSchedule(cronSchedule("*/10 * * * * ?"))
				    .withSchedule(simpleSchedule()
				        .withIntervalInSeconds(10).repeatForever())
				        //.withRepeatCount(10)) // note that 10 repeats will give a total of 11 firings
				    //.forJob(job) // identify job with handle to its JobDetail itself                   
				    .build();
			try {
				sf.getScheduler().scheduleJob(job , trigger);
			} catch (SchedulerException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
