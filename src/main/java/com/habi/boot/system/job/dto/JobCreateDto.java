package com.habi.boot.system.job.dto;

import java.util.Date;
import java.util.List;

import com.habi.boot.system.base.annotation.Children;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

public class JobCreateDto extends TriggerDto {
    @NotEmpty
    private String jobClassName;
    private String cronExpression;
    private Date start;
    private Date end;
    private String repeatCount;
    private String repeatInterval;
    @Children
    private List<JobData> jobDatas;

    public JobCreateDto() {
    }

    public Date getStart() {
        return this.start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return this.end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getJobClassName() {
        return this.jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = StringUtils.trim(jobClassName);
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getRepeatCount() {
        return this.repeatCount;
    }

    public void setRepeatCount(String repeatCount) {
        this.repeatCount = repeatCount;
    }

    public String getRepeatInterval() {
        return this.repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public List<JobData> getJobDatas() {
        return this.jobDatas;
    }

    public void setJobDatas(List<JobData> jobDatas) {
        this.jobDatas = jobDatas;
    }
}

