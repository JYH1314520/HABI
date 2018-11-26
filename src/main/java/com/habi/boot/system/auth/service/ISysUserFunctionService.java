package com.habi.boot.system.auth.service;

import com.habi.boot.system.auth.entity.SysUserFunctionEntity;
import com.habi.boot.system.base.IBaseService;
import com.habi.boot.system.base.ProxySelf;

import java.util.List;


public interface ISysUserFunctionService extends IBaseService<SysUserFunctionEntity>, ProxySelf<ISysUserFunctionService> {
    public List<SysUserFunctionEntity> findByUserName(String userName);
}
