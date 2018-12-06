package com.habi.boot.system.base.mybatis.mapperhelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import tk.mybatis.mapper.code.ORDER;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SelectKeyGenerator;
import tk.mybatis.mapper.util.MetaObjectUtil;
import tk.mybatis.mapper.util.StringUtil;

public class SelectKeyHelper {
    private MapperHelper mapperHelper;
    public SelectKeyHelper() {
    }


    protected String getSeqNextVal(EntityColumn column) {
        return StringUtil.isNotEmpty(null) ? null + ".nextval" : MessageFormat.format(this.mapperHelper.getConfig().getSeqFormat(), null, column.getColumn(), column.getProperty(), column.getTable().getName());
    }

    protected void newSelectKeyMappedStatement(MappedStatement ms, EntityColumn column, Class<?> entityClass, Boolean executeBefore, String identity) {
        String keyId = ms.getId() + "!selectKey";
        if (!ms.getConfiguration().hasKeyGenerator(keyId)) {
            Configuration configuration = ms.getConfiguration();
            String IDENTITY = column.getGenerator() != null && !"".equals(column.getGenerator()) ? column.getGenerator() : identity;
            Object keyGenerator;
            if ("JDBC".equalsIgnoreCase(IDENTITY)) {
                keyGenerator = new Jdbc3KeyGenerator();
            } else {
                if ("SEQUENCE".equalsIgnoreCase(IDENTITY)) {
                    //String DBType = (String) Optional.ofNullable(this.mapperHelper.getConfig().getDataBaseType()).orElse("oracle");
                    String DBType = (String) Optional.ofNullable(null).orElse("oracle");
                    String var11 = DBType.toLowerCase();
                    byte var12 = -1;
                    switch (var11.hashCode()) {
                        case 3194988:
                            if (var11.equals("hana")) {
                                var12 = 0;
                            }
                        default:
                            switch (var12) {
                                case 0:
                                    IDENTITY = "SELECT " + this.getSeqNextVal(column) + " FROM DUMMY";
                                    break;
                                default:
                                    IDENTITY = "SELECT " + this.getSeqNextVal(column) + " FROM DUAL";
                            }
                    }
                }
                SqlSource sqlSource = new RawSqlSource(configuration, IDENTITY, entityClass);
                Builder statementBuilder = new Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
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
