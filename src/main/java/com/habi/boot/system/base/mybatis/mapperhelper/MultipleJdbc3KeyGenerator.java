package com.habi.boot.system.base.mybatis.mapperhelper;

import java.sql.ResultSet;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

public class MultipleJdbc3KeyGenerator extends Jdbc3KeyGenerator {
    public MultipleJdbc3KeyGenerator() {
    }

    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        this.processBatch(ms, stmt, this.getParameters(parameter));
    }

    public void processBatch(MappedStatement ms, Statement stmt, Collection<Object> parameters) {
        ResultSet rs = null;

        try {
            rs = stmt.getGeneratedKeys();
            Configuration configuration = ms.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            String[] keyProperties = ms.getKeyProperties();
            ResultSetMetaData rsmd = rs.getMetaData();
            TypeHandler<?>[] typeHandlers = null;
            MetaObject metaParam;
            if (keyProperties != null && rsmd.getColumnCount() >= keyProperties.length) {
                for(Iterator var10 = parameters.iterator(); var10.hasNext(); this.populateKeys(rs, metaParam, keyProperties, typeHandlers)) {
                    Object parameter = var10.next();
                    if (!rs.next()) {
                        break;
                    }

                    metaParam = configuration.newMetaObject(parameter);
                    if (typeHandlers == null) {
                        typeHandlers = this.getTypeHandlers(typeHandlerRegistry, metaParam, keyProperties);
                    }
                }
            }
        } catch (Exception var20) {
            throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + var20, var20);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception var19) {
                    ;
                }
            }

        }

    }

    private Collection<Object> getParameters(Object parameter) {
        Collection<Object> parameters = null;
        if (parameter instanceof Collection) {
            parameters = (Collection)parameter;
        } else if (parameter instanceof Map) {
            Map parameterMap = (Map)parameter;
            if (parameterMap.containsKey("collection")) {
                parameters = (Collection)parameterMap.get("collection");
            } else if (parameterMap.containsKey("list")) {
                parameters = (List)parameterMap.get("list");
            } else if (parameterMap.containsKey("array")) {
                parameters = Arrays.asList((Object[])((Object[])parameterMap.get("array")));
            }
        }

        if (parameters == null) {
            parameters = new ArrayList();
            ((Collection)parameters).add(parameter);
        }

        return (Collection)parameters;
    }

    private TypeHandler<?>[] getTypeHandlers(TypeHandlerRegistry typeHandlerRegistry, MetaObject metaParam, String[] keyProperties) {
        TypeHandler<?>[] typeHandlers = new TypeHandler[keyProperties.length];

        for(int i = 0; i < keyProperties.length; ++i) {
            if (metaParam.hasSetter(keyProperties[i])) {
                Class<?> keyPropertyType = metaParam.getSetterType(keyProperties[i]);
                TypeHandler<?> th = typeHandlerRegistry.getTypeHandler(keyPropertyType);
                typeHandlers[i] = th;
            }
        }

        return typeHandlers;
    }

    private void populateKeys(ResultSet rs, MetaObject metaParam, String[] keyProperties, TypeHandler<?>[] typeHandlers) throws SQLException {
        for(int i = 0; i < keyProperties.length; ++i) {
            TypeHandler<?> th = typeHandlers[i];
            if (th != null) {
                Object value = th.getResult(rs, i + 1);
                metaParam.setValue(keyProperties[i], value);
            }
        }

    }
}

