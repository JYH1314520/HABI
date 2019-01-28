package com.habi.boot.system.job.dto;

import javax.persistence.Id;

public class SimpleTriggerDto {
    @Id
    private String schedName;
    @Id
    private String triggerName;
    @Id
    private String triggerGroup;
    private Long repeatCount;
    private Long repeatInterval;
    private Long timesTriggered;

    public SimpleTriggerDto() {
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

    public Long getRepeatCount() {
        return this.repeatCount;
    }

    public void setRepeatCount(Long repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Long getRepeatInterval() {
        return this.repeatInterval;
    }

    public void setRepeatInterval(Long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public Long getTimesTriggered() {
        return this.timesTriggered;
    }

    public void setTimesTriggered(Long timesTriggered) {
        this.timesTriggered = timesTriggered;
    }
}
