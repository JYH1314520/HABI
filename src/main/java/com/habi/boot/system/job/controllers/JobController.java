package com.habi.boot.system.job.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.ResponseData;
import com.habi.boot.system.job.dto.JobCreateDto;
import com.habi.boot.system.job.dto.JobDetailDto;
import com.habi.boot.system.job.dto.SchedulerDto;
import com.habi.boot.system.job.dto.TriggerDto;
import com.habi.boot.system.job.exception.FieldRequiredException;
import com.habi.boot.system.job.exception.JobException;
import com.habi.boot.system.job.service.IQuartzService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/job", "/api/job"})
public class JobController extends BaseController {
    @Autowired
    private IQuartzService quartzService;

    public JobController() {
    }

    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST}
    )
    public ResponseData createJob(@RequestBody JobCreateDto jobCreateDto, BindingResult result, HttpServletRequest request) throws SchedulerException, JobException, ClassNotFoundException, FieldRequiredException {
        jobCreateDto.setTriggerGroup(jobCreateDto.getJobGroup());
        jobCreateDto.setTriggerName(jobCreateDto.getJobName() + "_trigger");
        this.getValidator().validate(jobCreateDto, result);
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage("");
            return rd;
        } else {
            this.quartzService.createJob(jobCreateDto);
            return new ResponseData();
        }
    }

    @RequestMapping({"/pause"})
    public ResponseData pauseJobs(@RequestBody List<JobDetailDto> list) throws SchedulerException {
        this.quartzService.pauseJobs(list);
        return new ResponseData();
    }

    @RequestMapping({"/resume"})
    public ResponseData resumeJobs(@RequestBody List<JobDetailDto> list) throws SchedulerException {
        this.quartzService.resumeJobs(list);
        return new ResponseData();
    }

    @RequestMapping({"/deletejob"})
    public ResponseData deleteJobs(@RequestBody List<JobDetailDto> list) throws SchedulerException {
        this.quartzService.deleteJobs(list);
        return new ResponseData();
    }

    @RequestMapping({"/pausetrigger"})
    public ResponseData pauseTrigger(@RequestBody List<TriggerDto> list) throws SchedulerException {
        this.quartzService.pauseTriggers(list);
        return new ResponseData();
    }

    @RequestMapping({"/resumetrigger"})
    public ResponseData resumeTrigger(@RequestBody List<TriggerDto> list) throws SchedulerException {
        this.quartzService.resumeTriggers(list);
        return new ResponseData();
    }

    @RequestMapping({"/query"})
    public ResponseData queryJobs(@ModelAttribute JobDetailDto example, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize, HttpServletRequest request) throws SchedulerException {
        return this.qj(example, page, pagesize, request);
    }

    @RequestMapping({"/queryInfo"})
    public ResponseData query(@RequestBody JobDetailDto example, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize, HttpServletRequest request) throws SchedulerException {
        return this.qj(example, page, pagesize, request);
    }

    private ResponseData qj(JobDetailDto example, int page, int pagesize, HttpServletRequest request) {
        IRequest requestCtx = this.createRequestContext(request);
        return new ResponseData(this.quartzService.getJobInfoDetails(requestCtx, example, page, pagesize));
    }

    @RequestMapping({"/trigger"})
    public ResponseData queryTrigger(@RequestParam(required = true) String triggerName, @RequestParam(required = true) String triggerGroup, @RequestParam(required = true) String triggerType) throws SchedulerException {
        if ("CRON".equalsIgnoreCase(triggerType)) {
            return new ResponseData(Arrays.asList(this.quartzService.getCronTrigger(triggerName, triggerGroup)));
        } else {
            return "SIMPLE".equalsIgnoreCase(triggerType) ? new ResponseData(Arrays.asList(this.quartzService.getSimpleTrigger(triggerName, triggerGroup))) : new ResponseData();
        }
    }

    @RequestMapping({"/trigger/query"})
    public ResponseData queryTriggers(@ModelAttribute TriggerDto example, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize, HttpServletRequest request) throws SchedulerException {
        IRequest requestCtx = this.createRequestContext(request);
        return new ResponseData(this.quartzService.getTriggers(requestCtx, example, page, pagesize));
    }

    /** @deprecated */
    @RequestMapping({"/scheduler/query"})
    public ResponseData querySchedulers(@ModelAttribute SchedulerDto example, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) throws SchedulerException {
        return new ResponseData(this.quartzService.selectSchedulers(example, page, pagesize));
    }

    /** @deprecated */
    @RequestMapping({"/scheduler/info"})
    public ResponseData schedulerInformation() throws SchedulerException {
        Map<String, Object> infoMap = this.quartzService.schedulerInformation();
        ResponseData responseData = new ResponseData();
        responseData.setRows(Arrays.asList(infoMap));
        return responseData;
    }

    /** @deprecated */
    @RequestMapping({"/scheduler/start"})
    public ResponseData startScheduler() throws SchedulerException {
        return new ResponseData(Arrays.asList(this.quartzService.start()));
    }

    /** @deprecated */
    @RequestMapping({"/scheduler/standby"})
    public ResponseData standbyScheduler() throws SchedulerException {
        return new ResponseData(Arrays.asList(this.quartzService.standby()));
    }

    /** @deprecated */
    @RequestMapping({"/scheduler/pauseall"})
    public ResponseData schedulerPauseAll() throws SchedulerException {
        return new ResponseData(Arrays.asList(this.quartzService.pauseAll()));
    }

    /** @deprecated */
    @RequestMapping({"/scheduler/resumeall"})
    public ResponseData schedulerResumeAll() throws SchedulerException {
        return new ResponseData(Arrays.asList(this.quartzService.resumeAll()));
    }
}

