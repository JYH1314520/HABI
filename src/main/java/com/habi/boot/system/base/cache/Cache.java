package com.habi.boot.system.base.cache;

public interface Cache<T> {
    void init();

    String getName();

    void setName(String var1);

    T getValue(String var1);

    void setValue(String var1, T var2);

    void remove(String var1);

    String getCacheKey(T var1);

    void reload();

    void clear();
}
