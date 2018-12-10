package com.habi.boot.system.logs.service.impl;



import com.habi.boot.system.base.impl.BaseServiceImpl;
import com.habi.boot.system.logs.entity.SysLog;
import com.habi.boot.system.logs.mapper.SysLogMapper;
import com.habi.boot.system.logs.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLog> implements ISysLogService {
    @Autowired
    SysLogMapper sysLogMapper;

}
