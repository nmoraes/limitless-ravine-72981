package scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
public class SchedulerMain {

	 final static Logger logger = LoggerFactory.getLogger(SchedulerMain.class);

	    public static void main(String[] args) throws Exception {
	        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

	        scheduler.start();

	        JobDetail jobDetail = newJob(HelloJob.class).build();

	        Trigger trigger = newTrigger()
	                .startNow()
	                .withSchedule(repeatSecondlyForever(3600))
	                .build();

	        scheduler.scheduleJob(jobDetail, trigger);
	    }

	    public static class HelloJob implements Job {

	        @Override
	        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	            logger.info("HelloJob executed");
	        }
	    }
	}