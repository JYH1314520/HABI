package com.habi.boot.system.job.mapper;

import com.habi.boot.system.base.mapper.Mapper;
import com.habi.boot.system.job.dto.TriggerDto;

import java.util.List;

public interface TriggerMapper extends Mapper<TriggerDto> {
    TriggerDto selectByPrimaryKey(TriggerDto var1);

    List<TriggerDto> selectTriggers(TriggerDto var1);
}
