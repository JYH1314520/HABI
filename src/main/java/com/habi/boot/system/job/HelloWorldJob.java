package com.habi.boot.system.job;


import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class HelloWorldJob extends AbstractJob {
    private static Logger log = LoggerFactory.getLogger(HelloWorldJob.class);

    public HelloWorldJob() {
    }

    public void safeExecute(JobExecutionContext context) {
        JobDetail detail = context.getJobDetail();
        JobKey key = detail.getKey();
        TriggerKey triggerKey = context.getTrigger().getKey();
        String msg = "############# Hello World! - . jobKey:" + key + ", triggerKey:" + triggerKey + ", execTime:" + new Date();
        if (log.isInfoEnabled()) {
            log.info(msg);
        }

        try {
            Thread.sleep(20000L);
        } catch (InterruptedException var7) {
            var7.printStackTrace();
        }

        log.info("#############");
    }

    public boolean isRefireImmediatelyWhenException() {
        return false;
    }
}