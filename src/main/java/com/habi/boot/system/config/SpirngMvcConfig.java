package com.habi.boot.system.config;

import com.habi.boot.system.intercepter.TimeOutIntercepter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpirngMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TimeOutIntercepter timeOutIntercepter = new TimeOutIntercepter();
        String allowUrls[] = {"login","logout"};
        timeOutIntercepter.setAllowUrls(allowUrls);
        //添加拦截器
        registry.addInterceptor(timeOutIntercepter);

        super.addInterceptors(registry);
    }
}
