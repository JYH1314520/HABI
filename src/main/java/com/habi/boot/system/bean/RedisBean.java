package com.habi.boot.system.bean;

import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.base.cache.impl.HashStringRedisCacheGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisBean {
    @Bean(name = "sysUserGrougCache")
    public HashStringRedisCacheGroup functionCache(){
        String[] groupFields= {"userType"};
        String[] keyFields=  {"userName"};
        HashStringRedisCacheGroup sysUserGrougCache = new HashStringRedisCacheGroup();
        sysUserGrougCache.setName("sys_user");
        sysUserGrougCache.setGroupField(groupFields);
        sysUserGrougCache.setKeyField(keyFields);
        sysUserGrougCache.setLoadOnStartUp(true);
        sysUserGrougCache.setType(SysUserEntity.class);
        sysUserGrougCache.setSqlId("com.habi.boot.system.auth.mapper.SysUserMapper.selectUserForCache");
        return sysUserGrougCache;
    }

}
