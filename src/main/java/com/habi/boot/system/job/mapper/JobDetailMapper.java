package com.habi.boot.system.job.mapper;

import com.habi.boot.system.job.dto.JobDetailDto;
import com.habi.boot.system.job.dto.JobInfoDetailDto;

import java.util.List;

public interface JobDetailMapper {
    JobDetailDto selectByPrimaryKey(JobDetailDto var1);

    List<JobDetailDto> selectJobDetails(JobDetailDto var1);

    List<JobInfoDetailDto> selectJobInfoDetails(JobDetailDto var1);
}

