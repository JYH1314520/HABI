package com.habi.boot.system.base.mybatis.provider;

import com.habi.boot.system.base.annotation.TableGeneratedValue;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.code.ORDER;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SelectKeyGenerator;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;
import tk.mybatis.mapper.util.MetaObjectUtil;
import tk.mybatis.mapper.util.StringUtil;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;

public class MyBaseInsertProvider  extends BaseInsertProvider {
    public MyBaseInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }
    @Override
    public String insert(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        boolean hasLogicDelete = false;
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        this.processKey(sql, entityClass, ms, columnList);
        sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        Iterator var6 = columnList.iterator();
        while(var6.hasNext()) {
            EntityColumn column = (EntityColumn)var6.next();
            if (column.isInsertable()) {
                hasLogicDelete = SqlHelper.isLogicDeleteColumn(entityClass, column, hasLogicDelete);
                if (hasLogicDelete) {
                    sql.append(SqlHelper.getLogicDeletedValue(column, false)).append(",");
                } else {
                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder((String)null, "_cache", ",")));
                    } else {
                        sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder((String)null, (String)null, ","), this.isNotEmpty()));
                    }

                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
                    } else {
                        sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder((String)null, (String)null, ","), this.isNotEmpty()));
                    }
                }
            }
        }

        sql.append("</trim>");
        return sql.toString();
    }
    @Override
    public String insertSelective(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        boolean hasLogicDelete = false;
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        this.processKey(sql, entityClass, ms, columnList);
        sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass)));
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Iterator var6 = columnList.iterator();
        EntityColumn column;
        while(var6.hasNext()) {
            column = (EntityColumn)var6.next();
            if (column.isInsertable()) {
                if (column.isIdentity()) {
                    sql.append(column.getColumn() + ",");
                } else {
                    hasLogicDelete = SqlHelper.isLogicDeleteColumn(entityClass, column, hasLogicDelete);
                    if (hasLogicDelete) {
                        sql.append(column.getColumn()).append(",");
                    } else {
                        sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", this.isNotEmpty()));
                    }
                }
            }
        }

        sql.append("</trim>");
        hasLogicDelete = false;
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        var6 = columnList.iterator();

        while(var6.hasNext()) {
            column = (EntityColumn)var6.next();
            if (column.isInsertable()) {
                hasLogicDelete = SqlHelper.isLogicDeleteColumn(entityClass, column, hasLogicDelete);
                if (hasLogicDelete) {
                    sql.append(SqlHelper.getLogicDeletedValue(column, false)).append(",");
                } else {
                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder((String)null, "_cache", ",")));
                    } else {
                        sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder((String)null, (String)null, ","), this.isNotEmpty()));
                    }

                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
                    }
                }
            }
        }

        sql.append("</trim>");
        return sql.toString();
    }

    private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<EntityColumn> columnList) {
        Boolean hasIdentityKey = false;
        Iterator var6 = columnList.iterator();
        EntityColumn column;
        label34:
        do {
            while(true) {
                while(var6.hasNext()) {
                    column = (EntityColumn)var6.next();
                    if (column.isIdentity()) {
                        sql.append(SqlHelper.getBindCache(column));
                        if (hasIdentityKey) {
                            continue label34;
                        }

                        this.newSelectKeyMappedStatement(ms, column, entityClass, this.isBEFORE(), this.getIDENTITY(column));
                        hasIdentityKey = true;
                    } else if (column.getGenIdClass() != null) {
                        sql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                        sql.append("_parameter").append(", '").append(column.getProperty()).append("'");
                        sql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                        sql.append(", '").append(this.tableName(entityClass)).append("'");
                        sql.append(", '").append(column.getColumn()).append("')");
                        sql.append("\"/>");
                    }
                }

                return;
            }
        } while(column.getGenerator() != null && "JDBC".equals(column.getGenerator()));

        throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
    }

    protected String getSeqNextVal(EntityColumn column,String SequenceName) {
       if(!"".equals(SequenceName)) {
           return StringUtil.isNotEmpty(SequenceName) ? SequenceName + ".nextval" : MessageFormat.format(this.mapperHelper.getConfig().getSeqFormat(), SequenceName, column.getColumn(), column.getProperty(), column.getTable().getName());
       }else{
           return MessageFormat.format(this.mapperHelper.getConfig().getSeqFormat(), column.getTable().getName() + "_s", column.getColumn(), column.getProperty(), column.getTable().getName());
       }
    }

    protected void newSelectKeyMappedStatement(MappedStatement ms, EntityColumn column, Class<?> entityClass, Boolean executeBefore, String identity) {
        String keyId = ms.getId() + "!selectKey";
        String databaseType = null;
        String SequenceName = null;
        if (!ms.getConfiguration().hasKeyGenerator(keyId)) {
            Configuration configuration = ms.getConfiguration();
            String IDENTITY = column.getGenerator() != null && !"".equals(column.getGenerator()) ? column.getGenerator() : identity;
            Object keyGenerator;
            try {
                Field field = entityClass.getDeclaredField(column.getProperty());
                TableGeneratedValue tableGeneratedValue = field.getAnnotation(TableGeneratedValue.class);
                databaseType = tableGeneratedValue.databaseType();
                SequenceName = tableGeneratedValue.SequenceName();
            }catch (Exception var19) {
                databaseType = null;
                SequenceName = null;
            }
            if ("JDBC".equalsIgnoreCase(IDENTITY)) {
                keyGenerator = new Jdbc3KeyGenerator();
            } else {
                if ("ORACLE".equalsIgnoreCase(databaseType)) {
                    IDENTITY = "SELECT " + this.getSeqNextVal(column,SequenceName) + " FROM DUAL";
                    column.setOrder(ORDER.BEFORE);
                }else if ("HANA".equalsIgnoreCase(databaseType)){
                    IDENTITY = "SELECT " + this.getSeqNextVal(column,SequenceName) + " FROM DUMMY";
                }
                SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);
                MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
                statementBuilder.resource(ms.getResource());
                statementBuilder.fetchSize((Integer)null);
                statementBuilder.statementType(StatementType.STATEMENT);
                statementBuilder.keyGenerator(new NoKeyGenerator());
                statementBuilder.keyProperty(column.getProperty());
                statementBuilder.keyColumn((String)null);
                statementBuilder.databaseId((String)null);
                statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
                statementBuilder.resultOrdered(false);
                statementBuilder.resulSets((String)null);
                statementBuilder.timeout(configuration.getDefaultStatementTimeout());
                List<ParameterMapping> parameterMappings = new ArrayList();
                org.apache.ibatis.mapping.ParameterMap.Builder inlineParameterMapBuilder = new org.apache.ibatis.mapping.ParameterMap.Builder(configuration, statementBuilder.id() + "-Inline", entityClass, parameterMappings);
                statementBuilder.parameterMap(inlineParameterMapBuilder.build());
                List<ResultMap> resultMaps = new ArrayList();
                org.apache.ibatis.mapping.ResultMap.Builder inlineResultMapBuilder = new org.apache.ibatis.mapping.ResultMap.Builder(configuration, statementBuilder.id() + "-Inline", column.getJavaType(), new ArrayList(), (Boolean)null);
                resultMaps.add(inlineResultMapBuilder.build());
                statementBuilder.resultMaps(resultMaps);
                statementBuilder.resultSetType((ResultSetType)null);
                statementBuilder.flushCacheRequired(false);
                statementBuilder.useCache(false);
                statementBuilder.cache((Cache)null);
                MappedStatement statement = statementBuilder.build();

                try {
                    configuration.addMappedStatement(statement);
                } catch (Exception var20) {
                    ;
                }

                MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
                keyGenerator = new SelectKeyGenerator(keyStatement, column.getOrder() != ORDER.DEFAULT ? column.getOrder() == ORDER.BEFORE : executeBefore);

                try {
                    configuration.addKeyGenerator(keyId, (KeyGenerator)keyGenerator);
                } catch (Exception var19) {
                    ;
                }
            }

            try {
                MetaObject msObject = MetaObjectUtil.forObject(ms);
                msObject.setValue("keyGenerator", keyGenerator);
                msObject.setValue("keyProperties", column.getTable().getKeyProperties());
                msObject.setValue("keyColumns", column.getTable().getKeyColumns());
            } catch (Exception var18) {
                ;
            }

        }
    }
}
