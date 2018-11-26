package com.habi.boot.system.auth.service.impl;

import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.auth.mapper.SysUserMapper;
import com.habi.boot.system.auth.service.ISysUserService;
import com.habi.boot.system.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserEntity> implements ISysUserService {
    @Autowired
    SysUserMapper sysUserMapper;
    public SysUserEntity selectByUserName(String userName){
      return   sysUserMapper.selectByUserName(userName);
    }
}
