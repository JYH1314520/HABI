package com.habi.boot.system.auth.service;

import com.habi.boot.system.auth.entity.SysRoleFunctionEntity;
import com.habi.boot.system.base.IBaseService;
import com.habi.boot.system.base.ProxySelf;

import java.util.List;


public interface ISysRoleFunctionService extends IBaseService<SysRoleFunctionEntity>, ProxySelf<ISysRoleFunctionService> {
    public List<SysRoleFunctionEntity> findByRoleCode(List<String> roleCodes);
}
