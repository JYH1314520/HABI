package com.habi.boot.system.auth.service.impl;



import com.habi.boot.system.auth.entity.SysUserFunctionEntity;
import com.habi.boot.system.auth.mapper.SysUserFunctionMapper;
import com.habi.boot.system.auth.service.ISysUserFunctionService;
import com.habi.boot.system.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserFunctionServiceImpl extends BaseServiceImpl<SysUserFunctionEntity> implements ISysUserFunctionService {
    @Autowired
    SysUserFunctionMapper sysUserFunctionMapper;

    public List<SysUserFunctionEntity> findByUserName(String userName){
       return sysUserFunctionMapper.findByUserName(userName);
    }

}
