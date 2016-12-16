package scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DatabaseRabbit;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;
public class SchedulerMain {

	 final static Logger logger = LoggerFactory.getLogger(SchedulerMain.class);

	    public static void main(String[] args) throws Exception {
	        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

	        scheduler.start();

	        JobDetail jobDetail = newJob(HelloJob.class).build();

	        Trigger trigger = newTrigger()
	                .startNow()
	                .withSchedule(repeatSecondlyForever(300))
	                .build();

	        scheduler.scheduleJob(jobDetail, trigger);
	    }

	    public static class HelloJob implements Job {

	        @Override
	        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	            logger.info("* Executing Rabbit Job");       
	            DatabaseRabbit db = DatabaseRabbit.getInstance();
	            try {
	            	List<String> idsToDelete = db.select();
	            	db.delete(idsToDelete);
	      	} catch (Exception e1) {
	      		// TODO Auto-generated catch block
	      		 logger.info("* Rabbit Job failed" + e1.getMessage());;
	      	}
	            
	            
	        }
	    }
	}