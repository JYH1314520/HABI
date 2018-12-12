package com.habi.boot.system.base.cache.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.habi.boot.system.base.cache.CacheGroup;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HashStringRedisCacheGroup<T> extends RedisCache<T> implements CacheGroup<T> {
    private Logger logger = LoggerFactory.getLogger(HashStringRedisCacheGroup.class);
    private ObjectMapper objectMapper;
    private String[] groupField;
    private String valueField;
    private Map<String, HashStringRedisCache<T>> groupMap = new HashMap();

    public HashStringRedisCacheGroup() {
    }

    public String[] getGroupField() {
        return this.groupField;
    }

    public void setGroupField(String[] groupField) {
        this.groupField = groupField;
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getGroupValue(Object bean) {
        String key = null;

        try {
            key = getKeyOfBean(bean, this.groupField);
        } catch (Exception var4) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(var4.getMessage(), var4);
            }
        }

        return key;
    }

    public void init() {
        super.init();
    }

    public T getValue(String key) {
        return null;
    }

    public void setValue(String key, T value) {
        String group = this.getGroupValue(value);
        HashStringRedisCache cache = this.getGroup(group);
        if (StringUtils.isNotEmpty(this.getValueField())) {
            try {
                Object ret = PropertyUtils.getProperty(value, this.valueField);
                cache.setValue(key, ret);
            } catch (Exception var6) {
                this.logger.error(var6.getMessage(), var6);
            }
        } else {
            cache.setValue(key, value);
        }

    }

    public List<T> getGroupAll(String group) {
        return this.getGroup(group).getAll();
    }

    public void remove(String key) {
    }

    public HashStringRedisCache<T> getGroup(String group) {
        HashStringRedisCache hashStringRedisCache = (HashStringRedisCache)this.groupMap.get(group);
        if (hashStringRedisCache == null) {
            hashStringRedisCache = new HashStringRedisCache();
            hashStringRedisCache.setRedisTemplate(this.getRedisTemplate());
            hashStringRedisCache.setObjectMapper(this.objectMapper);
            hashStringRedisCache.setName(this.getName() + ":" + group);
            hashStringRedisCache.setCategory(this.getCategory());
            hashStringRedisCache.setKeyField(this.getKeyField());
            hashStringRedisCache.setType(this.getType());
            if (StringUtils.isNotEmpty(this.getValueField())) {
                hashStringRedisCache.setValueField(this.getValueField());
            }

            hashStringRedisCache.init();
            this.groupMap.put(group, hashStringRedisCache);
        }

        return hashStringRedisCache;
    }

    public T getValue(String group, String key) {
        return this.getGroup(group).getValue(key);
    }

    public void setValue(String group, String key, T value) {
        this.getGroup(group).setValue(key, value);
    }

    public void remove(String group, String key) {
        this.getGroup(group).remove(key);
    }

    public void removeGroup(String group) {
        this.getGroup(group).clear();
    }

    protected void handleRow(Object row) {
        String group = this.getGroupValue(row);
        HashStringRedisCache<T> cache = this.getGroup(group);
        cache.handleRow(row);
    }

    public void clear() {
        super.clear();
        this.groupMap.forEach((k, v) -> {
            v.clear();
        });
    }

    public String getValueField() {
        return this.valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }
}

