package com.habi.boot.system.job.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.job.dto.JobRunningInfoDto;
import com.habi.boot.system.job.mapper.JobRunningInfoDtoMapper;
import com.habi.boot.system.job.service.IJobRunningInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class JobRunningInfoServiceImpl implements IJobRunningInfoService {
    @Autowired
    private JobRunningInfoDtoMapper jobRunningInfoDtoMapper;

    public JobRunningInfoServiceImpl() {
    }

    public List<JobRunningInfoDto> queryJobRunningInfo(IRequest request, JobRunningInfoDto example, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return this.jobRunningInfoDtoMapper.select(example);
    }

    public void createJobRunningInfo(JobRunningInfoDto jobCreateDto) {
        this.jobRunningInfoDtoMapper.insertSelective(jobCreateDto);
    }

    public void delete(JobRunningInfoDto jobCreateDto) {
        this.jobRunningInfoDtoMapper.deleteByNameGroup(jobCreateDto);
    }
}

