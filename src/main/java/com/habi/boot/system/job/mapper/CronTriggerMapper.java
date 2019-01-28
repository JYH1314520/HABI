package com.habi.boot.system.job.mapper;

import com.habi.boot.system.base.mapper.Mapper;
import com.habi.boot.system.job.dto.CronTriggerDto;

@org.apache.ibatis.annotations.Mapper
public interface CronTriggerMapper extends Mapper<CronTriggerDto> {
   public CronTriggerDto selectByMorePrimaryKey(CronTriggerDto var);
}
