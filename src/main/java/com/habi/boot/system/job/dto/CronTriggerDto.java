package com.habi.boot.system.job.dto;

import javax.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

public class CronTriggerDto {
    @Id
    private String schedName;
    @Id
    private String triggerName;
    @Id
    private String triggerGroup;
    @NotEmpty
    private String cronExpression;
    private String timeZoneId;

    public CronTriggerDto() {
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

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = StringUtils.trim(cronExpression);
    }

    public String getTimeZoneId() {
        return this.timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = StringUtils.trim(timeZoneId);
    }
}

