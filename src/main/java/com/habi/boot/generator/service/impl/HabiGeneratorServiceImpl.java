package com.habi.boot.generator.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.habi.boot.generator.dto.DBColumn;
import com.habi.boot.generator.dto.DBTable;
import com.habi.boot.generator.dto.GeneratorInfo;
import com.habi.boot.generator.service.IHabiGeneratorService;
import freemarker.template.TemplateException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class HabiGeneratorServiceImpl implements IHabiGeneratorService {
    @Autowired
    @Qualifier("sqlSessionFactory")
    SqlSessionFactory sqlSessionFactory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HabiGeneratorServiceImpl() {
    }

    public List<ListValue> showTables() {
        try {
            SqlSession sqlSession = this.sqlSessionFactory.openSession();
            Throwable var2 = null;

            List var5;
            try {
                Connection conn = DBUtil.getConnectionBySqlSession(sqlSession);
                List<ListValue> tables = DBUtil.showAllTables(conn);
                conn.close();
                var5 = tables;
            } catch (Throwable var15) {
                var2 = var15;
                throw var15;
            } finally {
                if (sqlSession != null) {
                    if (var2 != null) {
                        try {
                            sqlSession.close();
                        } catch (Throwable var14) {
                            var2.addSuppressed(var14);
                        }
                    } else {
                        sqlSession.close();
                    }
                }

            }

            return var5;
        } catch (SQLException var17) {
            this.logger.error("数据库查询出错");
            return new ArrayList();
        }
    }

    public int generatorFile(GeneratorInfo info) {
        int rs = 0;
        String tableName = info.getTargetName();
        DBTable dbTable = this.getTableInfo(tableName);

        try {
            rs = this.createFile(dbTable, info);
        } catch (IOException var6) {
            rs = -1;
            this.logger.error(var6.getMessage());
        } catch (TemplateException var7) {
            rs = -1;
            this.logger.error(var7.getMessage());
        }

        return rs;
    }

    public DBTable getTableInfo(String tableName) {
        Connection conn = null;
        DBTable dbTable = new DBTable();
        List<DBColumn> columns = dbTable.getColumns();
        List<String> multiColumns = null;
        List NotNullColumns = null;

        try {
            SqlSession sqlSession = this.sqlSessionFactory.openSession();
            Throwable var8 = null;

            try {
                dbTable.setName(tableName);
                conn = DBUtil.getConnectionBySqlSession(sqlSession);
                DatabaseMetaData dbmd = conn.getMetaData();
                boolean multiLanguage = DBUtil.isMultiLanguageTable(tableName);
                if (multiLanguage) {
                    dbTable.setMultiLanguage(multiLanguage);
                    multiColumns = DBUtil.isMultiLanguageColumn(tableName, dbmd);
                }

                String columnPk = DBUtil.getPrimaryKey(tableName, dbmd);
                NotNullColumns = DBUtil.getNotNullColumn(tableName, dbmd);
                ResultSet rs1 = DBUtil.getTableColumnInfo(tableName, dbmd);

                while(rs1.next()) {
                    String columnName = rs1.getString("COLUMN_NAME");
                    if (!"OBJECT_VERSION_NUMBER".equalsIgnoreCase(columnName) && !"REQUEST_ID".equalsIgnoreCase(columnName) && !"PROGRAM_ID".equalsIgnoreCase(columnName) && !"CREATED_BY".equalsIgnoreCase(columnName) && !"CREATION_DATE".equalsIgnoreCase(columnName) && !"LAST_UPDATED_BY".equalsIgnoreCase(columnName) && !"LAST_UPDATE_DATE".equalsIgnoreCase(columnName) && !"LAST_UPDATE_LOGIN".equalsIgnoreCase(columnName) && !columnName.toUpperCase().startsWith("ATTRIBUTE")) {
                        columns.add(this.setColumnInfo(rs1, columnPk, NotNullColumns, multiLanguage, multiColumns));
                    }
                }

                rs1.close();
                conn.close();
            } catch (Throwable var22) {
                var8 = var22;
                throw var22;
            } finally {
                if (sqlSession != null) {
                    if (var8 != null) {
                        try {
                            sqlSession.close();
                        } catch (Throwable var21) {
                            var8.addSuppressed(var21);
                        }
                    } else {
                        sqlSession.close();
                    }
                }

            }
        } catch (SQLException var24) {
            this.logger.error(var24.getMessage());
        }

        return dbTable;
    }

    private DBColumn setColumnInfo(ResultSet rs1, String columnPk, List<String> NotNullColumns, boolean multiLanguage, List<String> multiColumns) throws SQLException {
        DBColumn column = new DBColumn();
        String columnName = rs1.getString("COLUMN_NAME");
        column.setName(columnName);
        String typeName = rs1.getString("TYPE_NAME");
        column.setJdbcType(typeName);
        if (StringUtil.isNotEmpty(rs1.getString("REMARKS"))) {
            column.setRemarks(rs1.getString("REMARKS"));
        }

        if (columnName.equalsIgnoreCase(columnPk)) {
            column.setId(true);
        }

        Iterator var9 = NotNullColumns.iterator();

        String m;
        while(var9.hasNext()) {
            m = (String)var9.next();
            if (columnName.equalsIgnoreCase(m) && !columnName.equalsIgnoreCase(columnPk)) {
                if ("BIGINT".equalsIgnoreCase(typeName)) {
                    column.setNotNull(true);
                } else if ("VARCHAR".equalsIgnoreCase(typeName)) {
                    column.setNotEmpty(true);
                }
            }
        }

        if (multiLanguage) {
            var9 = multiColumns.iterator();

            while(var9.hasNext()) {
                m = (String)var9.next();
                if (m.equals(columnName)) {
                    column.setMultiLanguage(true);
                    break;
                }
            }
        }

        column.setColumnLength(rs1.getString("COLUMN_SIZE"));
        return column;
    }

    public int createFile(DBTable table, GeneratorInfo info) throws IOException, TemplateException {
        int rs = FileUtil.isFileExist(info);
        if (rs == 0) {
            if (!"NotOperation".equalsIgnoreCase(info.getDtoStatus())) {
                FileUtil.createDto(table, info);
            }

            if (!"NotOperation".equalsIgnoreCase(info.getControllerStatus())) {
                FileUtil.createFtlInfoByType(FileUtil.pType.Controller, table, info);
            }

            if (!"NotOperation".equalsIgnoreCase(info.getMapperStatus())) {
                FileUtil.createFtlInfoByType(FileUtil.pType.Mapper, table, info);
            }

            if (!"NotOperation".equalsIgnoreCase(info.getImplStatus())) {
                FileUtil.createFtlInfoByType(FileUtil.pType.Impl, table, info);
            }

            if (!"NotOperation".equalsIgnoreCase(info.getServiceStatus())) {
                FileUtil.createFtlInfoByType(FileUtil.pType.Service, table, info);
            }

            if (!"NotOperation".equalsIgnoreCase(info.getMapperXmlStatus())) {
                FileUtil.createFtlInfoByType(FileUtil.pType.MapperXml, table, info);
            }

            if (!"NotOperation".equalsIgnoreCase(info.getHtmlStatus())) {
             //   FileUtil.createFtlInfoByType(FileUtil.pType.Html, table, info);
            }
        }

        return rs;
    }
}
