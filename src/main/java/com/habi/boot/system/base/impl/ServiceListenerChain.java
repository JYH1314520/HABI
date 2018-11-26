package com.habi.boot.system.base.impl;


import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.IServiceListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceListenerChain<T> {
    private ServiceListenerManager manager;
    private Object service;
    private List<IServiceListener<T>> serviceListeners = new ArrayList();
    private int index = 0;

    ServiceListenerChain() {
    }

    public ServiceListenerChain(ServiceListenerManager manager, Object service) {
        this.manager = manager;
        this.service = service;
        List<IServiceListener> list = manager.getRegisteredServiceListener(service.getClass());
        if (list != null) {
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                IServiceListener serviceListener = (IServiceListener)var4.next();
                this.serviceListeners.add(serviceListener);
            }
        }

    }

    public T beforeInsert(IRequest iRequest, T record) {
        if (this.index < this.serviceListeners.size()) {
            IServiceListener<T> current = (IServiceListener)this.serviceListeners.get(this.index++);
            current.beforeInsert(iRequest, record, this);
        }

        return record;
    }

    public T afterInsert(IRequest request, T record) {
        if (this.index < this.serviceListeners.size()) {
            IServiceListener<T> current = (IServiceListener)this.serviceListeners.get(this.index++);
            current.afterInsert(request, record, this);
        }

        return record;
    }

    public T beforeUpdate(IRequest iRequest, T record) {
        if (this.index < this.serviceListeners.size()) {
            IServiceListener<T> current = (IServiceListener)this.serviceListeners.get(this.index++);
            current.beforeUpdate(iRequest, record, this);
        }

        return record;
    }

    public T afterUpdate(IRequest request, T record) {
        if (this.index < this.serviceListeners.size()) {
            IServiceListener<T> current = (IServiceListener)this.serviceListeners.get(this.index++);
            current.afterUpdate(request, record, this);
        }

        return record;
    }

    public T beforeDelete(IRequest iRequest, T record) {
        if (this.index < this.serviceListeners.size()) {
            IServiceListener<T> current = (IServiceListener)this.serviceListeners.get(this.index++);
            current.beforeDelete(iRequest, record, this);
        }

        return record;
    }

    public T afterDelete(IRequest request, T record) {
        if (this.index < this.serviceListeners.size()) {
            IServiceListener<T> current = (IServiceListener)this.serviceListeners.get(this.index++);
            current.afterDelete(request, record, this);
        }

        return record;
    }

    public ServiceListenerChain copy() {
        ServiceListenerChain copy = new ServiceListenerChain();
        copy.manager = this.manager;
        copy.service = this.service;
        copy.serviceListeners.addAll(this.serviceListeners);
        copy.index = 0;
        return copy;
    }
}


