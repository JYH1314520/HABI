package com.habi.boot.system.initMethod;

import com.habi.boot.system.base.cache.impl.HashStringRedisCache;
import com.habi.boot.system.base.cache.impl.HashStringRedisCacheGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * APP启动时自动执行
 */
@Component
@Order(value = 1)
public class bootApplicationRunner implements ApplicationRunner {

    @Resource(name = "sysUserCacheInit")
    private HashStringRedisCache sysUserCacheInit;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("=====APP启动完成======");
        System.out.println("=====用户缓存=========");
        sysUserCacheInit.init();

    }

}

