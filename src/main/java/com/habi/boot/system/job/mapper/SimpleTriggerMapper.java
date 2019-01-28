package com.habi.boot.system.job.mapper;

import com.habi.boot.system.base.mapper.Mapper;
import com.habi.boot.system.job.dto.SimpleTriggerDto;

@org.apache.ibatis.annotations.Mapper
public interface SimpleTriggerMapper extends Mapper<SimpleTriggerDto> {
    SimpleTriggerDto selectByMorePrimaryKey(SimpleTriggerDto var1);
}
