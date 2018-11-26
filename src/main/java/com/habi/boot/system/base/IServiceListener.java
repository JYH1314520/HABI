package com.habi.boot.system.base;

import com.habi.boot.system.base.impl.ServiceListenerChain;

public interface IServiceListener<T> {
    void beforeInsert(IRequest var1, T var2, ServiceListenerChain<T> var3);

    void afterInsert(IRequest var1, T var2, ServiceListenerChain<T> var3);

    void beforeUpdate(IRequest var1, T var2, ServiceListenerChain<T> var3);

    void afterUpdate(IRequest var1, T var2, ServiceListenerChain<T> var3);

    void beforeDelete(IRequest var1, T var2, ServiceListenerChain<T> var3);

    void afterDelete(IRequest var1, T var2, ServiceListenerChain<T> var3);
}

