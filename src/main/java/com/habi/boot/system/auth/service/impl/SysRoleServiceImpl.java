package com.habi.boot.system.auth.service.impl;


import com.habi.boot.system.auth.entity.SysRoleEntity;
import com.habi.boot.system.auth.mapper.SysRoleMapper;
import com.habi.boot.system.auth.service.ISysRoleService;
import com.habi.boot.system.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleEntity> implements ISysRoleService {
    @Autowired
    SysRoleMapper sysRoleMapper;

}
