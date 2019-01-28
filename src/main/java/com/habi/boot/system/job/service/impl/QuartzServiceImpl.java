package com.habi.boot.system.job.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.job.AbstractJob;
import com.habi.boot.system.job.dto.*;
import com.habi.boot.system.job.exception.FieldRequiredException;
import com.habi.boot.system.job.exception.JobException;
import com.habi.boot.system.job.mapper.*;
import com.habi.boot.system.job.service.IQuartzService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.Trigger.TriggerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class QuartzServiceImpl implements IQuartzService {
    private final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);
    @Autowired
    private JobDetailMapper jobDetailMapper;
    @Autowired
    private TriggerMapper triggerMapper;
    @Autowired
    private CronTriggerMapper cronTriggerMapper;
    @Autowired
    private SimpleTriggerMapper simpleTriggerMapper;
    @Autowired
    private SchedulerMapper schedulerMapper;
    @Autowired
    private Scheduler quartzScheduler;

    public QuartzServiceImpl() {
    }

    public List<TriggerDto> getTriggers(IRequest request, TriggerDto example, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return this.triggerMapper.selectTriggers(example);
    }

    public CronTriggerDto getCronTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        CronTriggerDto dto = new CronTriggerDto();
        dto.setSchedName(this.quartzScheduler.getSchedulerName());
        dto.setTriggerName(triggerName);
        dto.setTriggerGroup(triggerGroup);
        return this.cronTriggerMapper.selectByPrimaryKey(dto);
    }

    public SimpleTriggerDto getSimpleTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        SimpleTriggerDto dto = new SimpleTriggerDto();
        dto.setSchedName(this.quartzScheduler.getSchedulerName());
        dto.setTriggerName(triggerName);
        dto.setTriggerGroup(triggerGroup);
        return this.simpleTriggerMapper.selectByPrimaryKey(dto);
    }

    public List<JobInfoDetailDto> getJobInfoDetails(IRequest request, JobDetailDto example, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        List<JobInfoDetailDto> selectJobInfoDetails = this.jobDetailMapper.selectJobInfoDetails(example);
        Iterator var6 = selectJobInfoDetails.iterator();

        while(var6.hasNext()) {
            JobInfoDetailDto jobInfoDetailDto = (JobInfoDetailDto)var6.next();

            try {
                JobKey jobKey = new JobKey(jobInfoDetailDto.getJobName(), jobInfoDetailDto.getJobGroup());
                JobDetail jobDetail = this.quartzScheduler.getJobDetail(jobKey);
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                String[] keys = jobDataMap.getKeys();
                List<JobData> jobDatas = new ArrayList();
                String[] var13 = keys;
                int var14 = keys.length;

                for(int var15 = 0; var15 < var14; ++var15) {
                    String string = var13[var15];
                    JobData e = new JobData();
                    e.setName(string);
                    e.setValue(jobDataMap.getString(string));
                    jobDatas.add(e);
                }

                List<Trigger> triggers = (List<Trigger>) this.quartzScheduler.getTriggersOfJob(jobKey);
                if (triggers != null && !triggers.isEmpty()) {
                    Trigger trigger = (Trigger)triggers.get(0);
                    if (trigger instanceof SimpleTrigger) {
                        jobInfoDetailDto.setTriggerType("SIMPLE");
                        jobInfoDetailDto.setRepeatCount(((SimpleTrigger)trigger).getRepeatCount());
                        jobInfoDetailDto.setRepeatInterval(((SimpleTrigger)trigger).getRepeatInterval());
                    } else if (trigger instanceof CronTrigger) {
                        jobInfoDetailDto.setCronExpression(((CronTrigger)trigger).getCronExpression());
                        jobInfoDetailDto.setTriggerType("CRON");
                    }

                    jobInfoDetailDto.setTriggerName(trigger.getKey().getName());
                    jobInfoDetailDto.setTriggerGroup(trigger.getKey().getGroup());
                    jobInfoDetailDto.setTriggerPriority(trigger.getPriority());
                    jobInfoDetailDto.setStartTime(trigger.getStartTime());
                    jobInfoDetailDto.setPreviousFireTime(trigger.getPreviousFireTime());
                    jobInfoDetailDto.setNextFireTime(trigger.getNextFireTime());
                    jobInfoDetailDto.setEndTime(trigger.getEndTime());
                    jobInfoDetailDto.setJobDatas(jobDatas);
                    TriggerState ts = this.quartzScheduler.getTriggerState(trigger.getKey());
                    jobInfoDetailDto.setRunningState(ts.name());
                } else {
                    this.logger.error("job.error.has_no_trigger--" + jobKey.getGroup() + "." + jobKey.getName());
                }
            } catch (SchedulerException var18) {
                jobInfoDetailDto.setRunningState(TriggerState.ERROR.name());
                if (this.logger.isErrorEnabled()) {
                    this.logger.error(var18.getMessage(), var18);
                }
            }
        }

        return selectJobInfoDetails;
    }

    public List<JobDetailDto> getJobDetails(IRequest request, JobDetailDto example, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return this.jobDetailMapper.selectJobDetails(example);
    }

    public Map<String, Object> schedulerInformation() throws SchedulerException {
        Map<String, Object> infoMap = new HashMap();
        SchedulerMetaData metaData = this.quartzScheduler.getMetaData();
        if (metaData.getRunningSince() != null) {
            infoMap.put("runningSince", metaData.getRunningSince().getTime());
        }

        infoMap.put("numberOfJobsExecuted", metaData.getNumberOfJobsExecuted());
        infoMap.put("schedulerName", metaData.getSchedulerName());
        infoMap.put("schedulerInstanceId", metaData.getSchedulerInstanceId());
        infoMap.put("threadPoolSize", metaData.getThreadPoolSize());
        infoMap.put("version", metaData.getVersion());
        infoMap.put("inStandbyMode", metaData.isInStandbyMode());
        infoMap.put("jobStoreClustered", metaData.isJobStoreClustered());
        infoMap.put("jobStoreClass", metaData.getJobStoreClass());
        infoMap.put("jobStoreSupportsPersistence", metaData.isJobStoreSupportsPersistence());
        infoMap.put("started", metaData.isStarted());
        infoMap.put("shutdown", metaData.isShutdown());
        infoMap.put("schedulerRemote", metaData.isSchedulerRemote());
        return infoMap;
    }

    public List<SchedulerDto> selectSchedulers(SchedulerDto schedulerDto, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return this.schedulerMapper.selectSchedulers(schedulerDto);
    }

    public void createJob(JobCreateDto jobCreateDto) throws ClassNotFoundException, SchedulerException, JobException {
        if (StringUtils.isEmpty(jobCreateDto.getJobClassName())) {
            throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"jobClassName"}));
        } else if (StringUtils.isEmpty(jobCreateDto.getJobName())) {
            throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"jobName"}));
        } else if (StringUtils.isEmpty(jobCreateDto.getJobGroup())) {
            throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"jobGroup"}));
        } else if (StringUtils.isEmpty(jobCreateDto.getTriggerName())) {
            throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"triggerName"}));
        } else if (StringUtils.isEmpty(jobCreateDto.getTriggerGroup())) {
            throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"triggerGroup"}));
        } else if (StringUtils.isEmpty(jobCreateDto.getTriggerType())) {
            throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"triggerType"}));
        } else {
            String jobClassName = jobCreateDto.getJobClassName();
            boolean assignableFrom = false;
            Class forName = null;

            try {
                forName = Class.forName(jobClassName);
                assignableFrom = AbstractJob.class.isAssignableFrom(forName);
            } catch (ClassNotFoundException var14) {
                if (this.logger.isErrorEnabled()) {
                    this.logger.error(var14.getMessage(), var14);
                }
            }

            if (assignableFrom && forName != null) {
                JobBuilder jb = JobBuilder.newJob(forName).withIdentity(jobCreateDto.getJobName(), jobCreateDto.getJobGroup()).withDescription(jobCreateDto.getDescription());
                if (this.hasJobData(jobCreateDto)) {
                    JobDataMap data = new JobDataMap();
                    List<JobData> jobDatas = jobCreateDto.getJobDatas();
                    Iterator var8 = jobDatas.iterator();

                    while(var8.hasNext()) {
                        JobData jobData = (JobData)var8.next();
                        data.put(jobData.getName(), jobData.getValue());
                    }

                    jb = jb.usingJobData(data);
                }

                JobDetail jobDetail = jb.build();
                int triggerPriority = jobCreateDto.getPriority() == null ? 5 : jobCreateDto.getPriority();
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobCreateDto.getTriggerName(), jobCreateDto.getTriggerGroup()).withPriority(triggerPriority).forJob(jobDetail);
                if (jobCreateDto.getStartTime() != null && jobCreateDto.getStartTime() > 0L) {
                    triggerBuilder.startAt(new Date(jobCreateDto.getStartTime()));
                }

                if (jobCreateDto.getEndTime() != null && jobCreateDto.getEndTime() > 0L) {
                    triggerBuilder.endAt(new Date(jobCreateDto.getEndTime()));
                }

                ScheduleBuilder sche = null;
                if ("CRON".equalsIgnoreCase(jobCreateDto.getTriggerType())) {
                    if (StringUtils.isEmpty(jobCreateDto.getCronExpression())) {
                        throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"cronExpression"}));
                    }

                    sche = CronScheduleBuilder.cronSchedule(jobCreateDto.getCronExpression());
                } else if ("SIMPLE".equalsIgnoreCase(jobCreateDto.getTriggerType())) {
                    if (StringUtils.isEmpty(jobCreateDto.getRepeatInterval())) {
                        throw new RuntimeException(new FieldRequiredException("hap.validation.notempty", new Object[]{"repeatInterval"}));
                    }

                    int interval = Integer.parseInt(jobCreateDto.getRepeatInterval());
                    int count = 0;

                    try {
                        count = Integer.parseInt(jobCreateDto.getRepeatCount());
                    } catch (Throwable var13) {
                        ;
                    }

                    if (count < 1) {
                        sche = SimpleScheduleBuilder.repeatSecondlyForever(interval);
                    } else {
                        sche = SimpleScheduleBuilder.repeatSecondlyForTotalCount(count, interval);
                    }
                }

                Trigger trigger = triggerBuilder.withSchedule((ScheduleBuilder)sche).build();
                this.quartzScheduler.scheduleJob(jobDetail, trigger);
            } else {
                String name = AbstractJob.class.getName();
                throw new JobException("JOB_EXCEPTION", "job.error.invalid_job_class", new Object[]{jobClassName, name});
            }
        }
    }

    private boolean hasJobData(JobCreateDto jobCreateDto) {
        List<JobData> jobDatas = jobCreateDto.getJobDatas();
        return jobDatas != null && !jobDatas.isEmpty();
    }

    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        this.quartzScheduler.deleteJob(new JobKey(jobName, jobGroup));
    }

    public Map<String, Object> start() throws SchedulerException {
        this.quartzScheduler.start();
        return this.schedulerInformation();
    }

    public Map<String, Object> standby() throws SchedulerException {
        this.quartzScheduler.standby();
        return this.schedulerInformation();
    }

    public Map<String, Object> pauseAll() throws SchedulerException {
        this.quartzScheduler.pauseAll();
        return this.schedulerInformation();
    }

    public Map<String, Object> resumeAll() throws SchedulerException {
        this.quartzScheduler.resumeAll();
        return this.schedulerInformation();
    }

    public void pauseJobs(List<JobDetailDto> list) throws SchedulerException {
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            JobDetailDto job = (JobDetailDto)var2.next();
            this.quartzScheduler.pauseJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
        }

    }

    public void resumeJobs(List<JobDetailDto> list) throws SchedulerException {
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            JobDetailDto job = (JobDetailDto)var2.next();
            this.quartzScheduler.resumeJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
        }

    }

    public void deleteJobs(List<JobDetailDto> list) throws SchedulerException {
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            JobDetailDto job = (JobDetailDto)var2.next();
            this.quartzScheduler.deleteJob(JobKey.jobKey(job.getJobName(), job.getJobGroup()));
        }

    }

    public void pauseTriggers(List<TriggerDto> list) throws SchedulerException {
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            TriggerDto trigger = (TriggerDto)var2.next();
            this.quartzScheduler.pauseTrigger(TriggerKey.triggerKey(trigger.getTriggerName(), trigger.getTriggerGroup()));
        }

    }

    public void resumeTriggers(List<TriggerDto> list) throws SchedulerException {
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            TriggerDto trigger = (TriggerDto)var2.next();
            this.quartzScheduler.resumeTrigger(TriggerKey.triggerKey(trigger.getTriggerName(), trigger.getTriggerGroup()));
        }

    }
}

