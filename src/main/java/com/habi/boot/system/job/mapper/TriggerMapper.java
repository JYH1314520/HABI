package com.habi.boot.system.job.mapper;

import com.habi.boot.system.base.mapper.Mapper;
import com.habi.boot.system.job.dto.TriggerDto;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TriggerMapper extends Mapper<TriggerDto> {
    TriggerDto selectByMorePrimaryKey(TriggerDto var1);

    List<TriggerDto> selectTriggers(TriggerDto var1);
}
