package com.habi.boot.system.job.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class JobInfoDetailDto extends JobDetailDto {
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date startTime;
    private Date endTime;
    private String triggerType;
    private String cronExpression;
    private int repeatCount;
    private long repeatInterval;
    private String jobResult;
    private String jobStatus;
    private String jobStatusMessage;
    private String triggerName;
    private String triggerGroup;
    private int triggerPriority;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date previousFireTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date fireTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date nextFireTime;
    private Long refireCount;
    private String fireInstanceId;
    private String schedulerInstanceId;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date scheduledFireTime;
    private String runningState;

    public JobInfoDetailDto() {
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return this.repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getTriggerType() {
        return this.triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobResult() {
        return this.jobResult;
    }

    public void setJobResult(String jobResult) {
        this.jobResult = jobResult;
    }

    public String getJobStatus() {
        return this.jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobStatusMessage() {
        return this.jobStatusMessage;
    }

    public void setJobStatusMessage(String jobStatusMessage) {
        this.jobStatusMessage = jobStatusMessage;
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return this.triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public Date getPreviousFireTime() {
        return this.previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public Date getFireTime() {
        return this.fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public Date getNextFireTime() {
        return this.nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Long getRefireCount() {
        return this.refireCount;
    }

    public void setRefireCount(Long refireCount) {
        this.refireCount = refireCount;
    }

    public String getFireInstanceId() {
        return this.fireInstanceId;
    }

    public void setFireInstanceId(String fireInstanceId) {
        this.fireInstanceId = fireInstanceId;
    }

    public String getSchedulerInstanceId() {
        return this.schedulerInstanceId;
    }

    public void setSchedulerInstanceId(String schedulerInstanceId) {
        this.schedulerInstanceId = schedulerInstanceId;
    }

    public Date getScheduledFireTime() {
        return this.scheduledFireTime;
    }

    public void setScheduledFireTime(Date scheduledFireTime) {
        this.scheduledFireTime = scheduledFireTime;
    }

    public String getRunningState() {
        return this.runningState;
    }

    public void setRunningState(String runningState) {
        this.runningState = runningState;
    }

    public int getTriggerPriority() {
        return this.triggerPriority;
    }

    public void setTriggerPriority(int triggerPriority) {
        this.triggerPriority = triggerPriority;
    }
}

