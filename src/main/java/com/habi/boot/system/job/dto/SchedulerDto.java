package com.habi.boot.system.job.dto;

public class SchedulerDto {
    private String schedName;
    private String instanceName;
    private Long lastCheckinTime;
    private Long checkinInterval;

    public SchedulerDto() {
    }

    public String getSchedName() {
        return this.schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Long getLastCheckinTime() {
        return this.lastCheckinTime;
    }

    public void setLastCheckinTime(Long lastCheckinTime) {
        this.lastCheckinTime = lastCheckinTime;
    }

    public Long getCheckinInterval() {
        return this.checkinInterval;
    }

    public void setCheckinInterval(Long checkinInterval) {
        this.checkinInterval = checkinInterval;
    }
}

