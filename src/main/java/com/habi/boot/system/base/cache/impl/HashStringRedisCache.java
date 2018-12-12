package com.habi.boot.system.base.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

@Service
public class HashStringRedisCache<T> extends RedisCache<T> {
    private ObjectMapper objectMapper;
    private String valueField;
    private boolean isBasic = false;
    private Constructor stringConstructor;
    private Logger logger = LoggerFactory.getLogger(HashStringRedisCache.class);
    private String fullKey;
    private String topic;

    public HashStringRedisCache() {
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setType(Class<?> type) {
        super.setType(type);
        if (String.class == type || Boolean.class == type || Number.class.isAssignableFrom(type)) {
            this.isBasic = true;
        }

        if (this.isBasic) {
            try {
                this.stringConstructor = type.getConstructor(String.class);
            } catch (NoSuchMethodException var3) {
                throw new RuntimeException(var3);
            }
        }

    }

    public void setName(String name) {
        super.setName(name);
        this.topic = "cache." + name;
    }

    public T getValue(String key) {
        return this.getRedisTemplate().execute((RedisCallback<T>)(connection) -> {
            return this.hMGet(connection, this.getFullKey(key), key);
        });
    }

    public void setValue(String key, T value) {
        if (value == null) {
            this.remove(key);
        } else {
            this.getRedisTemplate().execute((RedisCallback<T>) (connection) -> {
                try {
                    this.hMSet(connection, this.getFullKey(key), key, value);
                } catch (JsonProcessingException var5) {
                    this.logger.error("JsonProcessingException: ", var5);
                }

                return null;
            });
            if (!this.isLoading()) {
                this.getRedisTemplate().convertAndSend(this.topic, key);
            }

        }
    }

    protected String objectToString(Object value) {
        if (this.isBasic) {
            return String.valueOf(value);
        } else {
            try {
                return this.objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException var3) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("invalid json: " + value);
                }

                throw new RuntimeException(var3);
            }
        }
    }

    protected T stringToObject(String value) {
        if (this.isBasic) {
            try {
                return (T) this.stringConstructor.newInstance(value);
            } catch (Exception var3) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("invalid value: " + value);
                }

                throw new RuntimeException(var3);
            }
        } else {
            try {
                return (T)this.objectMapper.readValue(value, this.getType());
            } catch (Exception var4) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("invalid value: " + value);
                }

                throw new RuntimeException(var4);
            }
        }
    }

    protected void hMSet(RedisConnection connection, String mapKey, String pName, Object pValue) throws JsonProcessingException {
        String string = this.objectToString(pValue);
        Map<byte[], byte[]> v = new HashMap();
       // v.put(this.strSerializer.serialize(pName), this.strSerializer.serialize(string)); //注释20181211
        v.put(pName.getBytes(), string.getBytes());
       // connection.hMSet(this.strSerializer.serialize(mapKey), v); //注释20181211
        connection.hMSet(mapKey.getBytes(), v);
    }

    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }

    protected <E> E hMGet(RedisConnection connection, String mapKey, String pName) {
       // byte[] mapKeyBytes = this.strSerializer.serialize(mapKey); //注释20181211
        byte[] mapKeyBytes = mapKey.getBytes();
              //  List<byte[]> result = connection.hMGet(mapKeyBytes, new byte[][]{this.strSerializer.serialize(pName)}); //注释20181211
        List<byte[]> result = connection.hMGet(mapKeyBytes, new byte[][]{ pName.getBytes()});
        if (!result.isEmpty() && result.get(0) != null) {
            //String string = (String)this.strSerializer.deserialize((byte[])result.get(0));
            String string = byteArrayToStr(result.get(0));
            Object obj = this.stringToObject(string);
            return (E) obj;
        } else {
            return null;
        }
    }

    protected <E> List<E> hVals(RedisConnection connection, String mapKey) {
//        byte[] mapKeyBytes = this.strSerializer.serialize(mapKey);
        byte[] mapKeyBytes = mapKey.getBytes();
        List<byte[]> result = connection.hVals(mapKeyBytes);
        List list = new ArrayList();
        Iterator var6 = result.iterator();

        while(var6.hasNext()) {
            byte[] bs = (byte[])var6.next();
           // String string = (String)this.strSerializer.deserialize(bs);
            String string = byteArrayToStr(bs);
            Object obj = this.stringToObject(string);
            list.add(obj);
        }

        return list;
    }

    public List<T> getAll() {
        return (List)this.getRedisTemplate().execute((RedisCallback<T>)(connection) -> {
            return (T)this.hVals(connection, this.getFullKey((String)null));
        });
    }

    public void remove(String key) {
        this.getRedisTemplate().execute((RedisCallback<T>) (connection) -> {
//            byte[] mapKeyBytes = this.strSerializer.serialize(this.getFullKey(key));
//            byte[] valueKeyBytes = this.strSerializer.serialize(key);
            byte[] mapKeyBytes = this.getFullKey(key).getBytes();
            byte[] valueKeyBytes = key.getBytes();
            connection.hDel(mapKeyBytes, new byte[][]{valueKeyBytes});
            return null;
        });
        if (!this.isLoading()) {
            this.getRedisTemplate().convertAndSend(this.topic, key);
        }

    }

    protected void handleRow(Object row) {
        try {
            String[] keyField = this.getKeyField();
            String key = getKeyOfBean(row, keyField);
            if (this.valueField != null) {
                Object v = BeanUtils.getProperty(row, this.valueField);
                this.setValue(key, (T) v);
            } else {
                this.setValue(key, (T) row);
            }
        } catch (Exception var5) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(var5.getMessage(), var5);
            }
        }

    }

    protected String getFullKey(String key) {
        if (this.fullKey == null) {
            this.fullKey = this.getCategory() + ":" + this.getName();
        }

        return this.fullKey;
    }

    public void clear() {
        this.getRedisTemplate().execute((RedisCallback<T>) (connection) -> {
           // connection.del(new byte[][]{this.strSerializer.serialize(this.getFullKey((String)null))});
            connection.del(new byte[][]{this.getFullKey((String)null).getBytes()});
            return null;
        });
    }

    public String getValueField() {
        return this.valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }
}