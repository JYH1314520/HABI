package com.habi.boot.system.auth.service.impl;


import com.habi.boot.system.auth.entity.SysRoleFunctionEntity;
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

   public List<SysRoleFunctionEntity> findByRoleCode(String roleCode) {
        return sysRoleFunctionMapper.findByRoleCode(roleCode);
    };

}
