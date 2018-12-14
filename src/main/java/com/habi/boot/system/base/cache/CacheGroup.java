package com.habi.boot.system.base.cache;

public interface CacheGroup<T> {
    Cache<T> getGroup(String var1);

    default T getValue(String group, String key) {
        return this.getGroup(group).getValue(key);
    }

    default void setValue(String group, String key, T value) {
        this.getGroup(group).setValue(key, value);
    }

    default void remove(String group, String key) {
        this.getGroup(group).remove(key);
    }

    void removeGroup(String var1);
}
