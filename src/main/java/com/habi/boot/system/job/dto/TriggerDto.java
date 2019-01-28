package com.habi.boot.system.job.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

public class TriggerDto {
    @Id
    private String schedName;
    @Id
    @NotEmpty
    private String triggerName;
    @Id
    @NotEmpty
    private String triggerGroup;
    @NotEmpty
    private String jobName;
    @NotEmpty
    private String jobGroup;
    private String description;
    private Long nextFireTime;
    private Long prevFireTime;
    private Integer priority;
    private String triggerState;
    @NotEmpty
    private String triggerType;
    private Long startTime;
    private Long endTime;
    private String calendarName;
    private Integer misfireInstr;
    @JsonIgnore
    private byte[] jobData;

    public TriggerDto() {
    }

    public String getSchedName() {
        return this.schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
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

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return this.jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = StringUtils.trim(jobGroup);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNextFireTime() {
        return this.nextFireTime;
    }

    public void setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Long getPrevFireTime() {
        return this.prevFireTime;
    }

    public void setPrevFireTime(Long prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTriggerState() {
        return this.triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = StringUtils.trim(triggerState);
    }

    public String getTriggerType() {
        return this.triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = StringUtils.trim(triggerType);
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = StringUtils.trim(calendarName);
    }

    public Integer getMisfireInstr() {
        return this.misfireInstr;
    }

    public void setMisfireInstr(Integer misfireInstr) {
        this.misfireInstr = misfireInstr;
    }

    public byte[] getJobData() {
        return this.jobData;
    }

    public void setJobData(byte[] jobData) {
        this.jobData = jobData;
    }
}

