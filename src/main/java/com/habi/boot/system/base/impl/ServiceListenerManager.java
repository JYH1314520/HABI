package com.habi.boot.system.base.impl;


import com.habi.boot.system.base.AppContextInitListener;
import com.habi.boot.system.base.IServiceListener;
import com.habi.boot.system.base.annotation.ServiceListener;
import com.habi.boot.system.base.utils.CommonUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceListenerManager implements AppContextInitListener {
    Map<Class<?>, List<IServiceListener>> listenerMapping = new HashMap();

    public ServiceListenerManager() {
    }

    public void contextInitialized(ApplicationContext applicationContext) {
        Map<String, IServiceListener> map = applicationContext.getBeansOfType(IServiceListener.class);
        map.forEach((k, v) -> {
            ServiceListener annotation = (ServiceListener)v.getClass().getAnnotation(ServiceListener.class);
            if (annotation == null) {
                System.err.println(v + " has no @ServiceListener");
            } else {
                Class<?> clazz = annotation.target();
                List<IServiceListener> list = (List)this.listenerMapping.get(clazz);
                if (list == null) {
                    list = new ArrayList();
                    this.listenerMapping.put(clazz, list);
                }

                ((List)list).add(v);
            }

        });
        this.listenerMapping.forEach((k, v) -> {
            v.sort((o1, o2) -> {
                ServiceListener a1 = (ServiceListener)o1.getClass().getAnnotation(ServiceListener.class);
                ServiceListener a2 = (ServiceListener)o1.getClass().getAnnotation(ServiceListener.class);
                if (CommonUtils.in(a2, a1.before())) {
                    return -1;
                } else if (CommonUtils.in(a1, a2.before())) {
                    return 1;
                } else if (CommonUtils.in(a2, a1.after())) {
                    return 1;
                } else {
                    return CommonUtils.in(a1, a2.after()) ? -1 : 0;
                }
            });
        });
    }

    public List<IServiceListener> getRegisteredServiceListener(Class<?> clazz) {
        return (List)this.listenerMapping.get(clazz);
    }
}