package com.habi.boot.system.auth.service.impl;


import com.habi.boot.system.auth.entity.SysFunctionEntity;
import com.habi.boot.system.auth.entity.SysRoleFunctionEntity;
import com.habi.boot.system.auth.mapper.SysFunctionMapper;
import com.habi.boot.system.auth.mapper.SysRoleFunctionMapper;
import com.habi.boot.system.auth.service.ISysRoleFunctionService;
import com.habi.boot.system.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleFunctionServiceImpl extends BaseServiceImpl<SysRoleFunctionEntity> implements ISysRoleFunctionService {
    @Autowired
    SysRoleFunctionMapper sysRoleFunctionMapper;
    @Autowired
    SysFunctionMapper sysFunctionMapper;


   public List<SysRoleFunctionEntity> findByRoleCode(String roleCode) {
        List<SysRoleFunctionEntity> list = sysRoleFunctionMapper.findByRoleCode(roleCode);
        for(SysRoleFunctionEntity sysRoleFunctionEntity :list){
            SysFunctionEntity sysFunctionEntity = sysFunctionMapper.selectBYfunctionId(sysRoleFunctionEntity.getFunctionId());
            sysRoleFunctionEntity.setSysFunction(sysFunctionEntity);
        }
        return list;
    }

}
