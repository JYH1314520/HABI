package com.habi.boot.system.job.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.habi.boot.system.base.BaseEntity;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(
        name = "SYS_JOB_RUNNING_INFO"
)
public class JobRunningInfoDto extends BaseEntity {
    private static final long serialVersionUID = -6714732735643965630L;
    @Id
    @GeneratedValue(
            generator = "IDENTITY"
    )
    private Long jobRunningInfoId;

    private String jobName;

    private String jobGroup;
    private String jobResult;

    private String jobStatus;
    private String jobStatusMessage;
    private String triggerName;
    private String triggerGroup;
    private String executionSummary;
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
    private String ipAddress;

    public JobRunningInfoDto() {
    }

    public Long getJobRunningInfoId() {
        return this.jobRunningInfoId;
    }

    public void setJobRunningInfoId(Long jobRunningInfoId) {
        this.jobRunningInfoId = jobRunningInfoId;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobGroup() {
        return this.jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getJobResult() {
        return this.jobResult;
    }

    public void setJobResult(String jobResult) {
        this.jobResult = jobResult == null ? null : jobResult.trim();
    }

    public String getJobStatus() {
        return this.jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus == null ? null : jobStatus.trim();
    }

    public String getJobStatusMessage() {
        return this.jobStatusMessage;
    }

    public void setJobStatusMessage(String jobStatusMessage) {
        this.jobStatusMessage = jobStatusMessage == null ? null : jobStatusMessage.trim();
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName == null ? null : triggerName.trim();
    }

    public String getTriggerGroup() {
        return this.triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup == null ? null : triggerGroup.trim();
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
        this.fireInstanceId = fireInstanceId == null ? null : fireInstanceId.trim();
    }

    public String getSchedulerInstanceId() {
        return this.schedulerInstanceId;
    }

    public void setSchedulerInstanceId(String schedulerInstanceId) {
        this.schedulerInstanceId = schedulerInstanceId == null ? null : schedulerInstanceId.trim();
    }

    public Date getScheduledFireTime() {
        return this.scheduledFireTime;
    }

    public void setScheduledFireTime(Date scheduledFireTime) {
        this.scheduledFireTime = scheduledFireTime;
    }

    public String getExecutionSummary() {
        return this.executionSummary;
    }

    public void setExecutionSummary(String executionSummary) {
        this.executionSummary = executionSummary;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
