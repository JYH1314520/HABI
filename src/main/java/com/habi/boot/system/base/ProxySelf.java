package com.habi.boot.system.base;

import org.springframework.aop.framework.AopContext;

public interface ProxySelf<T> {
    default T self() {
        return (T) AopContext.currentProxy();
    }
}
