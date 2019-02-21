package com.habi.boot.system.auth.mapper;



import com.habi.boot.system.auth.entity.SysRoleFunctionEntity;
import com.habi.boot.system.base.mapper.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SysRoleFunctionMapper extends Mapper<SysRoleFunctionEntity> {
     List<SysRoleFunctionEntity> findByRoleCode(List<String> roleCodes) ;
}
