package com.habi.boot.system.job.mapper;

import com.habi.boot.system.base.mapper.Mapper;
import com.habi.boot.system.job.dto.JobRunningInfoDto;

public interface JobRunningInfoDtoMapper extends Mapper<JobRunningInfoDto> {
    void deleteByNameGroup(JobRunningInfoDto var1);
}
