package com.habi.boot.system.base;


import org.springframework.context.ApplicationContext;

public interface AppContextInitListener {
    void contextInitialized(ApplicationContext var1);
}