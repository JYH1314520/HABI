package com.habi.boot.system.base.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StdWho {
}
