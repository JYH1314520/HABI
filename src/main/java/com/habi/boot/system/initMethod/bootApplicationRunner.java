package com.habi.boot.system.initMethod;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * APP启动时自动执行
 */
@Component
@Order(value = 1)
public class bootApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("APP启动");
    }

}

