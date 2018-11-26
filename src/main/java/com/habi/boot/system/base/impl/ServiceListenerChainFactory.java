package com.habi.boot.system.base.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceListenerChainFactory {
    @Autowired
    private ServiceListenerManager manager;
    Map<Object, ServiceListenerChain> chainCache = new HashMap();

    public ServiceListenerChainFactory() {
    }

    public ServiceListenerChain getChain(Object service) {
        ServiceListenerChain chain = (ServiceListenerChain)this.chainCache.get(service);
        if (chain == null) {
            chain = new ServiceListenerChain(this.manager, service);
            this.chainCache.put(service, chain);
        }

        return chain.copy();
    }
}
