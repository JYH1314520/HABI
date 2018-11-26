package com.habi.boot.system.auth.service;

import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.base.IBaseService;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.ProxySelf;

import java.util.List;

public interface ISysUserService extends IBaseService<SysUserEntity>, ProxySelf<ISysUserService> {

    public SysUserEntity selectByUserName(String userName);
}
