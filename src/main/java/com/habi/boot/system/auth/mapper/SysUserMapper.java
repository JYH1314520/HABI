package com.habi.boot.system.auth.mapper;

import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.base.mapper.Mapper;

import java.util.List;
@org.apache.ibatis.annotations.Mapper
public interface SysUserMapper extends Mapper<SysUserEntity> {
   SysUserEntity selectByUserName(String userName);
}
