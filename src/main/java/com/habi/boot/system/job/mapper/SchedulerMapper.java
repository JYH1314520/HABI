package com.habi.boot.system.job.mapper;

import com.habi.boot.system.base.mapper.Mapper;
import com.habi.boot.system.job.dto.SchedulerDto;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SchedulerMapper  extends Mapper<SchedulerDto> {
    SchedulerDto selectByMorePrimaryKey(SchedulerDto var1);

    List<SchedulerDto> selectSchedulers(SchedulerDto var1);
}
