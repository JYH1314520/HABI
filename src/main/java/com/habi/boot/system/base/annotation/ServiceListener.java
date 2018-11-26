package com.habi.boot.system.base.annotation;


import com.habi.boot.system.base.IServiceListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceListener {
    Class<?> target();

    Class<? extends IServiceListener>[] before() default {};

    Class<? extends IServiceListener>[] after() default {};
}

