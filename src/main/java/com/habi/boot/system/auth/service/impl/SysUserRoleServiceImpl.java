package com.habi.boot.system.auth.service.impl;


import com.habi.boot.system.auth.entity.SysUserRoleEntity;
import com.habi.boot.system.auth.mapper.SysUserRoleMapper;
import com.habi.boot.system.auth.service.ISysUserRoleService;
import com.habi.boot.system.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleEntity> implements ISysUserRoleService {
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

}
