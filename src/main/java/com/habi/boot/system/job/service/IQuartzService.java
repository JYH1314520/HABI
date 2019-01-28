package com.habi.boot.system.job.service;

import java.util.List;
import java.util.Map;

import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.job.dto.*;
import com.habi.boot.system.job.exception.JobException;
import org.quartz.SchedulerException;

public interface IQuartzService {
    List<TriggerDto> getTriggers(IRequest var1, TriggerDto var2, int var3, int var4);

    CronTriggerDto getCronTrigger(String var1, String var2) throws SchedulerException;

    SimpleTriggerDto getSimpleTrigger(String var1, String var2) throws SchedulerException;

    List<JobDetailDto> getJobDetails(IRequest var1, JobDetailDto var2, int var3, int var4);

    /** @deprecated */
    Map<String, Object> schedulerInformation() throws SchedulerException;

    /** @deprecated */
    List<SchedulerDto> selectSchedulers(SchedulerDto var1, int var2, int var3);

    void createJob(JobCreateDto var1) throws ClassNotFoundException, SchedulerException, JobException;

    void deleteJob(String var1, String var2) throws SchedulerException;

    /** @deprecated */
    Map<String, Object> start() throws SchedulerException;

    /** @deprecated */
    Map<String, Object> standby() throws SchedulerException;

    /** @deprecated */
    Map<String, Object> pauseAll() throws SchedulerException;

    /** @deprecated */
    Map<String, Object> resumeAll() throws SchedulerException;

    void pauseJobs(List<JobDetailDto> var1) throws SchedulerException;

    void resumeJobs(List<JobDetailDto> var1) throws SchedulerException;

    void deleteJobs(List<JobDetailDto> var1) throws SchedulerException;

    void pauseTriggers(List<TriggerDto> var1) throws SchedulerException;

    void resumeTriggers(List<TriggerDto> var1) throws SchedulerException;

    List<JobInfoDetailDto> getJobInfoDetails(IRequest var1, JobDetailDto var2, int var3, int var4);
}

