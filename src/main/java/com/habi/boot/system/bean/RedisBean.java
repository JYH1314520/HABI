package com.habi.boot.system.bean;

import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.base.cache.impl.HashStringRedisCacheGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisBean {
    @Bean(name = "sysUserCache")
    public HashStringRedisCacheGroup functionCache(){
        String[] groupField= new String[1];
        String[] keyField=  new String[1];
        groupField[0] = "userType";
        keyField[0] = "userName";
        HashStringRedisCacheGroup functionCache = new HashStringRedisCacheGroup();
        functionCache.setName("function");
        functionCache.setGroupField(groupField);
        functionCache.setKeyField(keyField);
        functionCache.setLoadOnStartUp(true);
        functionCache.setType(SysUserEntity.class);
        functionCache.setSqlId("com.habi.boot.system.auth.mapper.SysUserMapper.selectUserForCache");
        return functionCache;
    }

}
