package com.habi.boot.system.auth.mapper;


import com.habi.boot.system.auth.entity.SysUserFunctionEntity;
import com.habi.boot.system.base.mapper.Mapper;

import java.util.List;
@org.apache.ibatis.annotations.Mapper
public interface SysUserFunctionMapper extends Mapper<SysUserFunctionEntity> {
    public List<SysUserFunctionEntity> findByUserName(String userName);
}
