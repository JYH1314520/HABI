package com.habi.boot.system.base.mybatis.entity;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import  tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.resolve.DefaultEntityResolve;
import tk.mybatis.mapper.mapperhelper.resolve.EntityResolve;
import tk.mybatis.mapper.util.MetaObjectUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MyEntityHelper extends  EntityHelper{
    private static final Map<Class<?>, EntityTable> entityTableMap = new ConcurrentHashMap();
    private static final EntityResolve DEFAULT = new DefaultEntityResolve();
    private static EntityResolve resolve;

    public MyEntityHelper() {
    }

    public static EntityTable getEntityTable(Class<?> entityClass) {
        EntityTable entityTable = (EntityTable)entityTableMap.get(entityClass);
        if (entityTable == null) {
            throw new MapperException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
        } else {
            return entityTable;
        }
    }

    public static EntityTable getEntityTable(String tableName) {
        EntityTable entityTable = null;
        Iterator var2 = entityTableMap.values().iterator();

        while(var2.hasNext()) {
            EntityTable entity = (EntityTable)var2.next();
            if (entity.getName().equalsIgnoreCase(tableName)) {
                entityTable = entity;
                break;
            }
        }

        if (null == entityTable) {
            throw new RuntimeException("无法通过表名" + tableName + "获取对应的表实体类!");
        } else {
            return entityTable;
        }
    }



    public static String getOrderByClause(Class<?> entityClass) {
        EntityTable table = getEntityTable(entityClass);
        if (table.getOrderByClause() != null) {
            return table.getOrderByClause();
        } else {
            List<tk.mybatis.mapper.entity.EntityColumn> orderEntityColumns = new ArrayList();
            Iterator var3 = table.getEntityClassColumns().iterator();

            while(var3.hasNext()) {
                tk.mybatis.mapper.entity.EntityColumn column = (tk.mybatis.mapper.entity.EntityColumn)var3.next();
                if (column.getOrderBy() != null) {
                    orderEntityColumns.add(column);
                }
            }

            Collections.sort(orderEntityColumns, new Comparator<tk.mybatis.mapper.entity.EntityColumn>() {
                public int compare(tk.mybatis.mapper.entity.EntityColumn o1, tk.mybatis.mapper.entity.EntityColumn o2) {
                    return o1.getOrderPriority() - o2.getOrderPriority();
                }
            });
            StringBuilder orderBy = new StringBuilder();

            tk.mybatis.mapper.entity.EntityColumn column;
            for(Iterator var7 = orderEntityColumns.iterator(); var7.hasNext(); orderBy.append(column.getColumn()).append(" ").append(column.getOrderBy())) {
                column = (tk.mybatis.mapper.entity.EntityColumn)var7.next();
                if (orderBy.length() != 0) {
                    orderBy.append(",");
                }
            }

            table.setOrderByClause(orderBy.toString());
            return table.getOrderByClause();
        }
    }

    public static Set<tk.mybatis.mapper.entity.EntityColumn> getColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassColumns();
    }

    public static Set<tk.mybatis.mapper.entity.EntityColumn> getPKColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassPKColumns();
    }

    public static String getSelectColumns(Class<?> entityClass) {
        EntityTable entityTable = getEntityTable(entityClass);
        if (entityTable.getBaseSelect() != null) {
            return entityTable.getBaseSelect();
        } else {
            Set<tk.mybatis.mapper.entity.EntityColumn> columnList = getColumns(entityClass);
            StringBuilder selectBuilder = new StringBuilder();
            boolean skipAlias = Map.class.isAssignableFrom(entityClass);
            Iterator var5 = columnList.iterator();

            while(true) {
                while(var5.hasNext()) {
                    tk.mybatis.mapper.entity.EntityColumn entityColumn = (tk.mybatis.mapper.entity.EntityColumn)var5.next();
                    selectBuilder.append(entityColumn.getColumn());
                    if (!skipAlias && !entityColumn.getColumn().equalsIgnoreCase(entityColumn.getProperty())) {
                        if (entityColumn.getColumn().substring(1, entityColumn.getColumn().length() - 1).equalsIgnoreCase(entityColumn.getProperty())) {
                            selectBuilder.append(",");
                        } else {
                            selectBuilder.append(" AS ").append(entityColumn.getProperty()).append(",");
                        }
                    } else {
                        selectBuilder.append(",");
                    }
                }

                entityTable.setBaseSelect(selectBuilder.substring(0, selectBuilder.length() - 1));
                return entityTable.getBaseSelect();
            }
        }
    }

    public static synchronized void initEntityNameMap(Class<?> entityClass, Config config) {
        if (entityTableMap.get(entityClass) == null) {
            EntityTable entityTable = resolve.resolveEntity(entityClass, config);
            entityTableMap.put(entityClass, entityTable);
        }
    }

    static void setResolve(EntityResolve resolve) {
        resolve = resolve;
    }

    public static void setKeyProperties(Set<tk.mybatis.mapper.entity.EntityColumn> pkColumns, MappedStatement ms) {
        if (pkColumns != null && !pkColumns.isEmpty()) {
            List<String> keyProperties = new ArrayList(pkColumns.size());
            Iterator var3 = pkColumns.iterator();

            while(var3.hasNext()) {
                tk.mybatis.mapper.entity.EntityColumn column = (EntityColumn)var3.next();
                keyProperties.add(column.getProperty());
            }

            MetaObjectUtil.forObject(ms).setValue("keyProperties", keyProperties.toArray(new String[0]));
        }
    }

    static {
        resolve = DEFAULT;
    }
}
