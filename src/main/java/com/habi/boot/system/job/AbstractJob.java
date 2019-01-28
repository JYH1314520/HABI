package com.habi.boot.system.job;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;

public abstract class AbstractJob implements Job, JobListener {
    public static final String JOB_RUNNING_INFO_ID = "JOB_RUNNING_INFO_ID";
    private String executionSummary;

    public AbstractJob() {
    }

    public final void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            this.safeExecute(context);
        } catch (Exception var6) {
            if (StringUtils.isEmpty(this.getExecutionSummary())) {
                this.setExecutionSummary(ExceptionUtils.getRootCauseMessage(var6));
            }

            JobExecutionException e2 = new JobExecutionException(var6);
            if (this.isRefireImmediatelyWhenException()) {
                e2.setRefireImmediately(true);
            } else {
                try {
                    context.getScheduler().pauseTrigger(context.getTrigger().getKey());
                } catch (SchedulerException var5) {
                    var5.printStackTrace();
                }
            }

            throw e2;
        }
    }

    public abstract void safeExecute(JobExecutionContext var1) throws Exception;

    protected boolean isRefireImmediatelyWhenException() {
        return false;
    }

    public String getExecutionSummary() {
        return this.executionSummary;
    }

    public void setExecutionSummary(String executionSummary) {
        this.executionSummary = executionSummary;
    }

    public String getName() {
        return null;
    }

    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
    }

    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
    }

    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
    }
}

