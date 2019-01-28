package com.habi.boot.system.job.service;

import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.job.dto.JobRunningInfoDto;

import java.util.List;

public interface IJobRunningInfoService {
    List<JobRunningInfoDto> queryJobRunningInfo(IRequest var1, JobRunningInfoDto var2, int var3, int var4);

    void createJobRunningInfo(JobRunningInfoDto var1);

    void delete(JobRunningInfoDto var1);
}

