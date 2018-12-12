package com.habi.boot.system.base.cache.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.habi.boot.system.base.cache.Cache;
import com.habi.boot.system.base.cache.ICacheListener;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RedisCache<T> implements Cache<T>, BeanNameAware {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSessionFactory sqlSessionFactory;
    private RedisTemplate<String, String> redisTemplate;
    private ICacheListener listener;
    private String name;
    private Class<?> type;
    private String[] keyField;
    private String category = "habi:cache";
    private boolean loading = false;
    private int db;
    private boolean loadOnStartUp = false;
    private String sqlId;
    protected RedisSerializer<String> strSerializer;

    public RedisCache() {
    }

    public void init() {
        this.strSerializer = this.redisTemplate.getStringSerializer();
        if (this.loadOnStartUp) {
            this.loading = true;

            try {
                this.initLoad();
            } finally {
                this.loading = false;
            }
        }

        if (this.listener != null) {
            this.listener.cacheInit(this);
        }

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return this.type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isLoadOnStartUp() {
        return this.loadOnStartUp;
    }

    public void setLoadOnStartUp(boolean loadOnStartUp) {
        this.loadOnStartUp = loadOnStartUp;
    }

    public String getSqlId() {
        return this.sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String[] getKeyField() {
        return this.keyField;
    }

    public void setKeyField(String[] keyField) {
        this.keyField = keyField;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return this.redisTemplate;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }

    @Autowired
    @Qualifier("sqlSessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public T getValue(String key) {
        return this.redisTemplate.execute((RedisCallback<T>)(connection) -> {
            byte[] keyBytes = this.strSerializer.serialize(this.getFullKey(key));
            Map<byte[], byte[]> value = connection.hGetAll(keyBytes);
            if (value.size() == 0) {
                return null;
            } else {
                try {
                    Object bean = this.type.newInstance();
                    Iterator var6 = value.entrySet().iterator();

                    while(var6.hasNext()) {
                        Entry<byte[], byte[]> entry = (Entry)var6.next();
                        String pName = (String)this.strSerializer.deserialize((byte[])entry.getKey());
                        String pValue = (String)this.strSerializer.deserialize((byte[])entry.getValue());
                        if (bean instanceof Map) {
                            ((Map)bean).put(pName, pValue);
                        } else {
                            PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, pName);
                            if (pd != null) {
                                Class<?> pType = pd.getPropertyType();
                                if (pType == Date.class) {
                                    Long time = pValue.length() == 0 ? null : Long.parseLong(pValue);
                                    BeanUtils.setProperty(bean, pName, time);
                                } else {
                                    BeanUtils.setProperty(bean, pName, pValue);
                                }
                            }
                        }
                    }

                    return (T)bean;
                } catch (Exception var13) {
                    this.logger.error(var13.getMessage(), var13);
                    return null;
                }
            }
        });
    }

    public void setValue(String key, T value) {
        try {
            Map<String, Object> map = this.convertToMap(value);
            this.setValue(key, map);
        } catch (Exception var4) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error("setValue error ", var4);
            }
        }

    }

    public void remove(String key) {
        this.redisTemplate.execute((RedisCallback<T>) (connection) -> {
            byte[] keyBytes = this.strSerializer.serialize(this.getFullKey(key));
            connection.del(new byte[][]{keyBytes});
            return null;
        });
    }

    public String getCacheKey(T value) {
        try {
            return getKeyOfBean(value, this.keyField);
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public void reload() {
        this.loading = true;

        try {
            this.clear();
            this.initLoad();
        } finally {
            this.loading = false;
        }

    }

    protected boolean isLoading() {
        return this.loading;
    }

    private void setValue(String key, Map<String, Object> value) {
        byte[] keyBytes = this.strSerializer.serialize(this.getFullKey(key));
        Map<byte[], byte[]> data = new HashMap();
        value.forEach((k, v) -> {
            if (k.charAt(0) != '_') {
                if (v instanceof Date) {
                    v = ((Date)v).getTime();
                }

                if (v != null) {
                    data.put(this.strSerializer.serialize(k), this.strSerializer.serialize(v.toString()));
                }

            }
        });
        this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            connection.hMSet(keyBytes, data);
            return null;
        });
    }

    protected void initLoad() {
        try {
            SqlSession sqlSession = this.sqlSessionFactory.openSession();
            Throwable var2 = null;

            try {
                sqlSession.select(this.sqlId, (resultContext) -> {
                    Object row = resultContext.getResultObject();
                    this.handleRow(row);
                });
            } catch (Throwable var12) {
                var2 = var12;
                throw var12;
            } finally {
                if (sqlSession != null) {
                    if (var2 != null) {
                        try {
                            sqlSession.close();
                        } catch (Throwable var11) {
                            var2.addSuppressed(var11);
                        }
                    } else {
                        sqlSession.close();
                    }
                }

            }
        } catch (Exception var14) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(var14.getMessage(), var14);
            }
        }

    }

    protected void handleRow(Object row) {
        try {
            Map<String, Object> rowMap = this.convertToMap(row);
            String key = getKeyOfBean(row, this.keyField);
            this.setValue(key, rowMap);
        } catch (Exception var4) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(var4.getMessage(), var4);
            }
        }

    }

    public static String getKeyOfBean(Object bean, String[] properties) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String key = BeanUtils.getProperty(bean, properties[0]);
        if (properties.length > 1) {
            StringBuilder sb = new StringBuilder(key);

            for(int i = 1; i < properties.length; ++i) {
                sb.append('.').append(BeanUtils.getProperty(bean, properties[i]));
            }

            key = sb.toString();
        }

        return key;
    }

    private Map<String, Object> convertToMap(Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (obj instanceof Map) {
            return (Map)obj;
        } else {
            Map<String, Object> map = PropertyUtils.describe(obj);
            map.remove("class");
            return map;
        }
    }

    protected String getFullKey(String key) {
        return this.getCategory() + ":" + this.getName() + ":" + key;
    }

    public void clear() {
    }

    public void setListener(ICacheListener listener) {
        this.listener = listener;
    }

    public void setBeanName(String name) {
        if (this.getName() == null) {
            this.setName(name);
        }

    }

    protected void onCacheReload() {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("cache reloaded:" + this.getName());
        }

    }
}

