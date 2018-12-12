package com.habi.boot.system.base.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class StringCache extends HashStringRedisCache<String> {
    private Logger logger = LoggerFactory.getLogger(StringCache.class);


    public StringCache() {
        this.setLoadOnStartUp(true);
        this.setType(String.class);
    }

    public String getValue(String key) {
        return (String)super.getValue(key);
    }

    public void setValue(String key, String s) {
        super.setValue(key, s);
    }

    public void reload(String C,String V) {

        try {

            this.setValue(C, V);
        } catch (Throwable var13) {

        } finally {
            }

        }


}
