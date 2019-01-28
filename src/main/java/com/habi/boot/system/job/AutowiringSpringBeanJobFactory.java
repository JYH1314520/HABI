package com.habi.boot.system.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
    private transient AutowireCapableBeanFactory beanFactory;

    public AutowiringSpringBeanJobFactory() {
    }

    public void setApplicationContext(ApplicationContext context) {
        this.beanFactory = context.getAutowireCapableBeanFactory();
    }

    public Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object job = super.createJobInstance(bundle);
        this.beanFactory.autowireBean(job);
        return job;
    }
}
