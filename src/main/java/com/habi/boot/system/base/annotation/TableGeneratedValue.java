package com.habi.boot.system.base.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableGeneratedValue {
    String databaseType() default "MYSQL";
    String SequenceName() default "";
}
