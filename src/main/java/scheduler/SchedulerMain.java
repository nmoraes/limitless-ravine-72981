package scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import db.DatabaseRabbit;
import db.DatabaseReadiness;
import db.Util;

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
	                .withSchedule(repeatSecondlyForever(120))
	                .build();

	        scheduler.scheduleJob(jobDetail, trigger);
	    }

	public static class HelloJob implements Job {

		@Override
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
			logger.info("* Executing Rabbit Job.");
			DatabaseRabbit db = DatabaseRabbit.getInstance();
			DatabaseReadiness dbr = DatabaseReadiness.getInstance();		
						
			try {
				
				String html = dbr.selectHTML();
				List<String> idsToDelete = db.select(html);
				
				if(idsToDelete.isEmpty()){
					logger.info("* No data found.");

				}else{
				
					db.delete(idsToDelete);
				}
				
				logger.info("* Executed susscefully.");
			} catch (Exception e1) {
				logger.info("* Rabbit Job failed" + e1.getMessage());
			}

		}
	}
	}