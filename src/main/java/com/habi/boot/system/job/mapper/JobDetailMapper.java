package com.habi.boot.system.job.mapper;

import com.habi.boot.system.job.dto.JobDetailDto;
import com.habi.boot.system.job.dto.JobInfoDetailDto;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface JobDetailMapper {
    JobDetailDto selectByMorePrimaryKey(JobDetailDto var1);

    List<JobDetailDto> selectJobDetails(JobDetailDto var1);

    List<JobInfoDetailDto> selectJobInfoDetails(JobDetailDto var1);
}

