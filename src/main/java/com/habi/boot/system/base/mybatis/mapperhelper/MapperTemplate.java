package com.habi.boot.system.base.mybatis.mapperhelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.mapping.ParameterMapping.Builder;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.ChooseSqlNode;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.util.StringUtil;

public abstract class MapperTemplate {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    private Map<String, Method> methodMap = new HashMap();
    private Map<String, Class<?>> entityClassMap = new HashMap();
    private Class<?> mapperClass;
    private MapperHelper mapperHelper;
    private String UUID;

    public MapperTemplate() {
    }

    public MapperTemplate(Class<?> mapperClass, MapperHelper mapperHelper) {
        this.mapperClass = mapperClass;
        this.mapperHelper = mapperHelper;
    }

    public static Class<?> getMapperClass(String msId) {
        if (msId.indexOf(46) == -1) {
            throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
        } else {
            String mapperClassStr = msId.substring(0, msId.lastIndexOf(46));

            try {
                return Class.forName(mapperClassStr);
            } catch (ClassNotFoundException var3) {
                return null;
            }
        }
    }

    public Map<String, Class<?>> getEntityClassMap() {
        return this.entityClassMap;
    }

    public static String getMethodName(MappedStatement ms) {
        return getMethodName(ms.getId());
    }

    public static String getMethodName(String msId) {
        return msId.substring(msId.lastIndexOf(".") + 1);
    }

    public String dynamicSQL(Object record) {
        return "dynamicSQL";
    }

    public void addMethodMap(String methodName, Method method) {
        this.methodMap.put(methodName, method);
    }

    public String getUUID() {
        return StringUtil.isNotEmpty(this.UUID) ? this.UUID : "@java.util.UUID@randomUUID().toString().replace(\"-\", \"\")";
    }

    public String getIDENTITY() {
        return this.mapperHelper.getConfig().getIDENTITY();
    }

    public boolean isBEFORE() {
        return this.mapperHelper.getConfig().isBEFORE();
    }

    public boolean isNotEmpty() {
        return this.mapperHelper.getConfig().isNotEmpty();
    }

    public boolean supportMethod(String msId) {
        Class<?> newMapperClass = getMapperClass(msId);
        if (newMapperClass != null && this.mapperClass.isAssignableFrom(newMapperClass)) {
            String methodName = getMethodName(msId);
            return this.methodMap.get(methodName) != null;
        } else {
            return false;
        }
    }

    protected void setResultType(MappedStatement ms, Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        List<ResultMap> resultMaps = new ArrayList();
        resultMaps.add(entityTable.getResultMap(ms.getConfiguration()));
        MetaObject metaObject = SystemMetaObject.forObject(ms);
        metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
    }

    protected void setSqlSource(MappedStatement ms, SqlSource sqlSource) {
        MetaObject msObject = SystemMetaObject.forObject(ms);
        msObject.setValue("sqlSource", sqlSource);
        KeyGenerator keyGenerator = ms.getKeyGenerator();
        if (keyGenerator instanceof Jdbc3KeyGenerator) {
            msObject.setValue("keyGenerator", new MultipleJdbc3KeyGenerator());
        }

    }

    private void checkCache(MappedStatement ms) throws Exception {
        if (ms.getCache() == null) {
            String nameSpace = ms.getId().substring(0, ms.getId().lastIndexOf(46));

            Cache cache;
            try {
                cache = ms.getConfiguration().getCache(nameSpace);
            } catch (IllegalArgumentException var5) {
                return;
            }

            if (cache != null) {
                MetaObject metaObject = SystemMetaObject.forObject(ms);
                metaObject.setValue("cache", cache);
            }
        }

    }

    public void setSqlSource(MappedStatement ms) throws Exception {
        if (this.mapperClass == getMapperClass(ms.getId())) {
            throw new RuntimeException("请不要配置或扫描通用Mapper接口类：" + this.mapperClass);
        } else {
            Method method = (Method) this.methodMap.get(getMethodName(ms));

            try {
                if (method.getReturnType() == Void.TYPE) {
                    method.invoke(this, ms);
                } else if (SqlNode.class.isAssignableFrom(method.getReturnType())) {
                    SqlNode sqlNode = (SqlNode) method.invoke(this, ms);
                    DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(ms.getConfiguration(), sqlNode);
                    this.setSqlSource(ms, dynamicSqlSource);
                } else {
                    if (!String.class.equals(method.getReturnType())) {
                        throw new RuntimeException("自定义Mapper方法返回类型错误,可选的返回类型为void,SqlNode,String三种!");
                    }

                    String xmlSql = (String) method.invoke(this, ms);
                    SqlSource sqlSource = this.createSqlSource(ms, xmlSql);
                    this.setSqlSource(ms, sqlSource);
                }

                this.checkCache(ms);
            } catch (IllegalAccessException var5) {
                throw new RuntimeException(var5);
            } catch (InvocationTargetException var6) {
                throw new RuntimeException((Throwable) (var6.getTargetException() != null ? var6.getTargetException() : var6));
            }
        }
    }

    public SqlSource createSqlSource(MappedStatement ms, String xmlSql) {
        return languageDriver.createSqlSource(ms.getConfiguration(), "<script>\n\t" + xmlSql + "</script>", (Class) null);
    }

    public Class<?> getEntityClass(MappedStatement ms) {
        String msId = ms.getId();
        if (this.entityClassMap.containsKey(msId)) {
            return (Class) this.entityClassMap.get(msId);
        } else {
            Class<?> mapperClass = getMapperClass(msId);
            Type[] types = mapperClass.getGenericInterfaces();
            if (null != types) {
                Type[] var5 = types;
                int var6 = types.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    Type type = var5[var7];
                    if (type instanceof ParameterizedType) {
                        ParameterizedType t = (ParameterizedType) type;
                        if (t.getRawType() == this.mapperClass || this.mapperClass.isAssignableFrom((Class) t.getRawType())) {
                            Class<?> returnType = (Class) t.getActualTypeArguments()[0];
                            EntityHelper.initEntityNameMap(returnType, this.mapperHelper.getConfig());
                            this.entityClassMap.put(msId, returnType);
                            return returnType;
                        }
                    }
                }
            }

            throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected List<ParameterMapping> getPrimaryKeyParameterMappings(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        Set<EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        List<ParameterMapping> parameterMappings = new ArrayList();
        Iterator var5 = entityColumns.iterator();

        while (var5.hasNext()) {
            EntityColumn column = (EntityColumn) var5.next();
            Builder builder = new Builder(ms.getConfiguration(), column.getProperty(), column.getJavaType());
            builder.mode(ParameterMode.IN);
            parameterMappings.add(builder.build());
        }

        return parameterMappings;
    }

    protected String getSeqNextVal(EntityColumn column) {
        return StringUtil.isNotEmpty(null) ? null + ".nextval" : MessageFormat.format(this.mapperHelper.getConfig().getSeqFormat(), null, column.getColumn(), column.getProperty(), column.getTable().getName());
    }

    protected String tableName(Class<?> entityClass) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String prefix = entityTable.getPrefix();
        if (StringUtil.isEmpty(prefix)) {
            prefix = this.mapperHelper.getConfig().getPrefix();
        }

        return StringUtil.isNotEmpty(prefix) ? prefix + "." + entityTable.getName() : entityTable.getName();
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getDynamicTableNameNode(Class<?> entityClass) {
        if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
            List<SqlNode> ifSqlNodes = new ArrayList();
            ifSqlNodes.add(new IfSqlNode(new TextSqlNode("${dynamicTableName}"), "@com.srm.hrp.mybatis.util.OGNL@isDynamicParameter(_parameter) and dynamicTableName != null and dynamicTableName != ''"));
            ifSqlNodes.add(new IfSqlNode(new StaticTextSqlNode(this.tableName(entityClass)), "@com.srm.hrp.mybatis.util.OGNL@isNotDynamicParameter(_parameter) or dynamicTableName == null or dynamicTableName == ''"));
            return new MixedSqlNode(ifSqlNodes);
        } else {
            return new StaticTextSqlNode(this.tableName(entityClass));
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getDynamicTableNameNode(Class<?> entityClass, String parameterName) {
        if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
            List<SqlNode> ifSqlNodes = new ArrayList();
            ifSqlNodes.add(new IfSqlNode(new TextSqlNode("${" + parameterName + ".dynamicTableName}"), "@com.srm.hrp.mybatis.util.OGNL@isDynamicParameter(" + parameterName + ") and " + parameterName + ".dynamicTableName != null and  " + parameterName + ".dynamicTableName != ''"));
            ifSqlNodes.add(new IfSqlNode(new StaticTextSqlNode(this.tableName(entityClass)), "@com.srm.hrp.mybatis.util.OGNL@isNotDynamicParameter(" + parameterName + ") or " + parameterName + ".dynamicTableName == null or " + parameterName + ".dynamicTableName == ''"));
            return new MixedSqlNode(ifSqlNodes);
        } else {
            return new StaticTextSqlNode(this.tableName(entityClass));
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getIfNotNull(EntityColumn column, SqlNode columnNode) {
        return this.getIfNotNull(column, columnNode, false);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getIfNotNull(EntityColumn column, SqlNode columnNode, boolean empty) {
        return empty && column.getJavaType().equals(String.class) ? new IfSqlNode(columnNode, column.getProperty() + " != null and " + column.getProperty() + " != ''") : new IfSqlNode(columnNode, column.getProperty() + " != null ");
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getIfIsNull(EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + " == null ");
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getIfCacheNotNull(EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + "_cache != null ");
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getIfCacheIsNull(EntityColumn column, SqlNode columnNode) {
        return new IfSqlNode(columnNode, column.getProperty() + "_cache == null ");
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getColumnEqualsProperty(EntityColumn column, boolean first) {
        return new StaticTextSqlNode((first ? "" : " AND ") + column.getColumnEqualsHolder());
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected SqlNode getAllIfColumnNode(Class<?> entityClass) {
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList();
        boolean first = true;

        for (Iterator var5 = columnList.iterator(); var5.hasNext(); first = false) {
            EntityColumn column = (EntityColumn) var5.next();
            ifNodes.add(this.getIfNotNull(column, this.getColumnEqualsProperty(column, first), this.isNotEmpty()));
        }

        return new MixedSqlNode(ifNodes);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected List<ParameterMapping> getColumnParameterMappings(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        Set<EntityColumn> entityColumns = EntityHelper.getColumns(entityClass);
        List<ParameterMapping> parameterMappings = new ArrayList();
        Iterator var5 = entityColumns.iterator();

        while (var5.hasNext()) {
            EntityColumn column = (EntityColumn) var5.next();
            Builder builder = new Builder(ms.getConfiguration(), column.getProperty(), column.getJavaType());
            builder.mode(ParameterMode.IN);
            parameterMappings.add(builder.build());
        }

        return parameterMappings;
    }

    protected void newSelectKeyMappedStatement(MappedStatement ms, EntityColumn column) {
        String keyId = ms.getId() + "!selectKey";
        if (!ms.getConfiguration().hasKeyGenerator(keyId)) {
            Class<?> entityClass = this.getEntityClass(ms);
            Configuration configuration = ms.getConfiguration();
            Boolean executeBefore = this.isBEFORE();
            String generator = column.getGenerator() == null ? null : column.getGenerator();
            String IDENTITY = !"IDENTITY".equals(generator) && !StringUtil.isEmpty(generator) ? generator : this.getIDENTITY();
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
                MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, keyId, sqlSource, SqlCommandType.SELECT);
                statementBuilder.resource(ms.getResource());
                statementBuilder.fetchSize((Integer) null);
                statementBuilder.statementType(StatementType.STATEMENT);
                statementBuilder.keyGenerator(new NoKeyGenerator());
                statementBuilder.keyProperty(column.getProperty());
                statementBuilder.keyColumn((String) null);
                statementBuilder.databaseId((String) null);
                statementBuilder.lang(configuration.getDefaultScriptingLanuageInstance());
                statementBuilder.resultOrdered(false);
                statementBuilder.resulSets((String) null);
                statementBuilder.timeout(configuration.getDefaultStatementTimeout());
                List<ParameterMapping> parameterMappings = new ArrayList();
                org.apache.ibatis.mapping.ParameterMap.Builder inlineParameterMapBuilder = new org.apache.ibatis.mapping.ParameterMap.Builder(configuration, statementBuilder.id() + "-Inline", entityClass, parameterMappings);
                statementBuilder.parameterMap(inlineParameterMapBuilder.build());
                List<ResultMap> resultMaps = new ArrayList();
                ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration, statementBuilder.id() + "-Inline", column.getJavaType(), new ArrayList(), (Boolean) null);
                resultMaps.add(inlineResultMapBuilder.build());
                statementBuilder.resultMaps(resultMaps);
                statementBuilder.resultSetType((ResultSetType) null);
                statementBuilder.flushCacheRequired(false);
                statementBuilder.useCache(false);
                statementBuilder.cache((Cache) null);
                MappedStatement statement = statementBuilder.build();

                try {
                    configuration.addMappedStatement(statement);
                } catch (Exception var21) {
                    ;
                }

                MappedStatement keyStatement = configuration.getMappedStatement(keyId, false);
                keyGenerator = new SelectKeyGenerator(keyStatement, executeBefore);

                try {
                    configuration.addKeyGenerator(keyId, (KeyGenerator) keyGenerator);
                } catch (Exception var20) {
                    ;
                }
            }

            try {
                MetaObject msObject = SystemMetaObject.forObject(ms);
                msObject.setValue("keyGenerator", keyGenerator);
                msObject.setValue("keyProperties", column.getTable().getKeyProperties());
                msObject.setValue("keyColumns", column.getTable().getKeyColumns());
            } catch (Exception var19) {
                ;
            }

        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public IfSqlNode ExampleValidSqlNode(Configuration configuration) {
        List<SqlNode> whenSqlNodes = new ArrayList();
        IfSqlNode noValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition}"), "criterion.noValue");
        whenSqlNodes.add(noValueSqlNode);
        IfSqlNode singleValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition} #{criterion.value}"), "criterion.singleValue");
        whenSqlNodes.add(singleValueSqlNode);
        IfSqlNode betweenValueSqlNode = new IfSqlNode(new TextSqlNode(" and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}"), "criterion.betweenValue");
        whenSqlNodes.add(betweenValueSqlNode);
        List<SqlNode> listValueContentSqlNodes = new ArrayList();
        listValueContentSqlNodes.add(new TextSqlNode(" and ${criterion.condition}"));
        ForEachSqlNode listValueForEachSqlNode = new ForEachSqlNode(configuration, new StaticTextSqlNode("#{listItem}"), "criterion.value", (String) null, "listItem", "(", ")", ",");
        listValueContentSqlNodes.add(listValueForEachSqlNode);
        IfSqlNode listValueSqlNode = new IfSqlNode(new MixedSqlNode(listValueContentSqlNodes), "criterion.listValue");
        whenSqlNodes.add(listValueSqlNode);
        ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, (SqlNode) null);
        ForEachSqlNode criteriaSqlNode = new ForEachSqlNode(configuration, chooseSqlNode, "criteria.criteria", (String) null, "criterion", (String) null, (String) null, (String) null);
        TrimSqlNode trimSqlNode = new TrimSqlNode(configuration, criteriaSqlNode, "(", "and", ")", (String) null);
        IfSqlNode validSqlNode = new IfSqlNode(trimSqlNode, "criteria.valid");
        return validSqlNode;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public WhereSqlNode exampleWhereClause(Configuration configuration) {
        ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, this.ExampleValidSqlNode(configuration), "oredCriteria", (String) null, "criteria", (String) null, (String) null, " or ");
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, forEachSqlNode);
        return whereSqlNode;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public WhereSqlNode updateByExampleWhereClause(Configuration configuration) {
        ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, this.ExampleValidSqlNode(configuration), "example.oredCriteria", (String) null, "criteria", (String) null, (String) null, " or ");
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, forEachSqlNode);
        return whereSqlNode;
    }
}


