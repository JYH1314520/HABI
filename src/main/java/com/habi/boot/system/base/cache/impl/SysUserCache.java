package com.habi.boot.system.base.cache.impl;

import com.habi.boot.system.auth.entity.SysUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class SysUserCache extends HashStringRedisCache<SysUserEntity> {
    private Logger logger = LoggerFactory.getLogger(SysUserCache.class);


    public SysUserCache() {
        this.setLoadOnStartUp(true);
        this.setType(SysUserEntity.class);
    }

    public SysUserEntity getValue(String key) {
        return (SysUserEntity)super.getValue(key);
    }

    public void setValue(String key, SysUserEntity s) {
        super.setValue(key, s);
    }

    public void reload(String C,SysUserEntity V) {

        try {

            this.setValue(C, V);
        } catch (Throwable var13) {

        } finally {
            }

        }


}
