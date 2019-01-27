package com.habi.boot.system.excel.service.impl;

import com.habi.boot.generator.service.impl.DBUtil;
import com.habi.boot.system.excel.dto.ColumnInfo;
import com.habi.boot.system.excel.service.ExcelException;
import com.habi.boot.system.excel.service.ExcelRowStrategy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRowStrategy implements ExcelRowStrategy {
    DataSource dataSource;
    List<ColumnInfo> columnInfos = new ArrayList();
    private StringBuffer sql;
    private String tableName;
    private List<String> columnName;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private int batchCount = 0;
    private int MaxBach = 1000;
    private Connection connection;
    private PreparedStatement stmt;

    public DefaultRowStrategy(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void optRow(int sheetIndex, int curRow, List<String> rowList) throws ExcelException {
        if (!this.rowIsBlank(rowList) && (sheetIndex != 0 || curRow != 0)) {
            if (sheetIndex == 0 && curRow == 1) {
                this.columnName = new ArrayList(rowList);

                try {
                    this.readColumnType(this.tableName);
                } catch (SQLException var6) {
                    this.logger.error(var6.getMessage(), var6);
                    throw new ExcelException((String)null, "列信息读取失败", (Object[])null);
                }

                if (!this.isContains(this.columnInfos, rowList)) {
                    throw new ExcelException((String)null, "excel数据验证失败：非法的列存在", (Object[])null);
                }

                this.sql = new StringBuffer("insert into ");
                this.sql.append(this.tableName).append(" (");
                this.createSql(rowList);

                try {
                    this.connection = this.dataSource.getConnection();
                    this.connection.setAutoCommit(false);
                    this.stmt = this.connection.prepareStatement(this.sql.toString());
                } catch (SQLException var5) {
                    this.logger.error(var5.getMessage(), var5);
                    throw new ExcelException((String)null, "数据库链接失败", (Object[])null);
                }
            } else {
                this.logger.info("正在处理第" + sheetIndex + "页，第" + curRow + "行数据");
                this.createPreparedStatement(this.stmt, rowList);
                ++this.batchCount;
            }
        }

    }

    public void doService() throws ExcelException {
        this.insertData(this.stmt);

        try {
            this.connection.commit();
        } catch (SQLException var5) {
            this.logger.error(var5.getMessage(), var5);
            this.rollback();
            throw new ExcelException((String)null, "数据插入失败", (Object[])null);
        } finally {
            this.closeConnection();
            this.cleanData();
        }

        this.logger.info("数据插入成功");
    }

    private void readColumnType(String tableName) throws SQLException {
        try {
            Connection connection = this.dataSource.getConnection();
            Throwable var3 = null;

            try {
                ResultSet resultSet = DBUtil.getTableColumnInfo(tableName, connection.getMetaData());

                while(resultSet.next()) {
                    ColumnInfo columnInfo = new ColumnInfo();
                    String typeName = resultSet.getString("TYPE_NAME");
                    String columnName = resultSet.getString("COLUMN_NAME");
                    columnInfo.setName(columnName);
                    columnInfo.setType(typeName);
                    this.columnInfos.add(columnInfo);
                }

                resultSet.close();
            } catch (Throwable var16) {
                var3 = var16;
                throw var16;
            } finally {
                if (connection != null) {
                    if (var3 != null) {
                        try {
                            connection.close();
                        } catch (Throwable var15) {
                            var3.addSuppressed(var15);
                        }
                    } else {
                        connection.close();
                    }
                }

            }
        } catch (SQLException var18) {
            this.logger.error("读取列信息失败");
            throw new SQLException(var18);
        }
    }

    private boolean isContains(List<ColumnInfo> columnInfos, List<String> row) {
        boolean result = true;
        Iterator var4 = row.iterator();

        while(var4.hasNext()) {
            String cell = (String)var4.next();
            boolean key = false;
            Iterator var7 = columnInfos.iterator();

            while(var7.hasNext()) {
                ColumnInfo columnInfo = (ColumnInfo)var7.next();
                if ("".equals(cell) || !"".equals(cell) && cell.equalsIgnoreCase(columnInfo.getName())) {
                    key = true;
                    break;
                }
            }

            if (!key) {
                result = false;
                break;
            }
        }

        return result;
    }

    private void createSql(List<String> cells) {
        Iterator var2 = cells.iterator();

        String cell;
        while(var2.hasNext()) {
            cell = (String)var2.next();
            if (!"".equals(cell)) {
                this.sql.append(cell).append(",");
            }
        }

        this.sql.deleteCharAt(this.sql.length() - 1);
        this.sql.append(") values(");
        var2 = cells.iterator();

        while(var2.hasNext()) {
            cell = (String)var2.next();
            if (!"".equals(cell)) {
                this.sql.append("?,");
            }
        }

        this.sql.deleteCharAt(this.sql.length() - 1);
        this.sql.append(")");
    }

    private void createPreparedStatement(PreparedStatement stmt, List<String> row) throws ExcelException {
        int index = 1;

        for(int i = 0; i < row.size(); ++i) {
            String column = (String)this.columnName.get(i);
            if (!"".equals(column)) {
                String type = "STRING";
                Iterator var7 = this.columnInfos.iterator();

                while(var7.hasNext()) {
                    ColumnInfo columnInfo = (ColumnInfo)var7.next();
                    if (columnInfo.getName().equalsIgnoreCase(column)) {
                        type = columnInfo.getType();
                        break;
                    }
                }

                this.addBatch(stmt, (String)row.get(i), index++, type);
            }
        }

        try {
            stmt.addBatch();
        } catch (SQLException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new ExcelException((String)null, "addBatch失败", (Object[])null);
        }

        if (this.batchCount > this.MaxBach) {
            this.insertData(stmt);
            this.batchCount = 0;
        }

    }

    private void insertData(PreparedStatement statement) throws ExcelException {
        try {
            statement.executeBatch();
        } catch (SQLException var3) {
            this.logger.error(var3.getMessage(), var3);
            this.rollback();
            this.closeConnection();
            this.cleanData();
            throw new ExcelException((String)null, "sql执行失败", (Object[])null);
        }
    }

    private void cleanData() {
        this.columnInfos = new ArrayList();
        this.sql = null;
        this.tableName = null;
        this.columnName = null;
        this.batchCount = 0;
    }

    private void closeConnection() throws ExcelException {
        try {
            this.stmt.close();
            this.connection.close();
        } catch (SQLException var2) {
            this.logger.error(var2.getMessage(), var2);
            throw new ExcelException((String)null, "数据库会话关闭失败", (Object[])null);
        }
    }

    private void rollback() throws ExcelException {
        try {
            this.logger.info("数据库插入失败，数据回滚。。。");
            this.connection.rollback();
        } catch (SQLException var2) {
            this.logger.error(var2.getMessage(), var2);
            throw new ExcelException((String)null, "数据回滚失败", (Object[])null);
        }
    }

    private void addBatch(PreparedStatement stmt, String cell, int index, String type) throws ExcelException {
        try {
            byte var6;
            if (!"".equals(cell)) {
                var6 = -1;
                switch(type.hashCode()) {
                    case -2034720975:
                        if (type.equals("DECIMAL")) {
                            var6 = 6;
                        }
                        break;
                    case -1981034679:
                        if (type.equals("NUMBER")) {
                            var6 = 9;
                        }
                        break;
                    case -1718637701:
                        if (type.equals("DATETIME")) {
                            var6 = 2;
                        }
                        break;
                    case -1618932450:
                        if (type.equals("INTEGER")) {
                            var6 = 10;
                        }
                        break;
                    case -1453246218:
                        if (type.equals("TIMESTAMP")) {
                            var6 = 1;
                        }
                        break;
                    case -594415409:
                        if (type.equals("TINYINT")) {
                            var6 = 11;
                        }
                        break;
                    case 72655:
                        if (type.equals("INT")) {
                            var6 = 8;
                        }
                        break;
                    case 2090926:
                        if (type.equals("DATE")) {
                            var6 = 0;
                        }
                        break;
                    case 2575053:
                        if (type.equals("TIME")) {
                            var6 = 3;
                        }
                        break;
                    case 66988604:
                        if (type.equals("FLOAT")) {
                            var6 = 5;
                        }
                        break;
                    case 1959128815:
                        if (type.equals("BIGINT")) {
                            var6 = 7;
                        }
                        break;
                    case 2022338513:
                        if (type.equals("DOUBLE")) {
                            var6 = 4;
                        }
                }

                switch(var6) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        stmt.setDate(index, Date.valueOf(cell));
                        break;
                    case 4:
                        stmt.setDouble(index, Double.valueOf(cell));
                        break;
                    case 5:
                        stmt.setFloat(index, Float.valueOf(cell));
                        break;
                    case 6:
                        stmt.setBigDecimal(index, BigDecimal.valueOf(Double.valueOf(cell)));
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        stmt.setInt(index, Integer.valueOf(cell));
                        break;
                    default:
                        stmt.setString(index, cell);
                }
            } else {
                String var5 = type.toUpperCase();
                var6 = -1;
                switch(var5.hashCode()) {
                    case -2034720975:
                        if (var5.equals("DECIMAL")) {
                            var6 = 4;
                        }
                        break;
                    case -1718637701:
                        if (var5.equals("DATETIME")) {
                            var6 = 6;
                        }
                        break;
                    case -1618932450:
                        if (var5.equals("INTEGER")) {
                            var6 = 1;
                        }
                        break;
                    case -1453246218:
                        if (var5.equals("TIMESTAMP")) {
                            var6 = 8;
                        }
                        break;
                    case 2067286:
                        if (var5.equals("CHAR")) {
                            var6 = 0;
                        }
                        break;
                    case 2090926:
                        if (var5.equals("DATE")) {
                            var6 = 5;
                        }
                        break;
                    case 2575053:
                        if (var5.equals("TIME")) {
                            var6 = 7;
                        }
                        break;
                    case 1959128815:
                        if (var5.equals("BIGINT")) {
                            var6 = 2;
                        }
                        break;
                    case 2022338513:
                        if (var5.equals("DOUBLE")) {
                            var6 = 3;
                        }
                }

                switch(var6) {
                    case 0:
                        stmt.setNull(index, 1);
                        break;
                    case 1:
                        stmt.setNull(index, 4);
                        break;
                    case 2:
                        stmt.setNull(index, -5);
                        break;
                    case 3:
                        stmt.setNull(index, 8);
                        break;
                    case 4:
                        stmt.setNull(index, 3);
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        stmt.setNull(index, 91);
                        break;
                    default:
                        stmt.setNull(index, 12);
                }
            }
        } catch (IllegalStateException var7) {
            this.logger.error(var7.getMessage(), var7);
            throw new ExcelException((String)null, cell + " 数据类型错误: " + (index + 1) + "列,期待的类型: " + type, (Object[])null);
        } catch (SQLException var8) {
            this.logger.error(var8.getMessage(), var8);
            throw new ExcelException((String)null, "PreparedStatement插入数据失败", (Object[])null);
        }

        this.logger.info("正在将数据装入batch... 第" + (index + 1) + "列....当前值：" + cell);
    }

    private boolean rowIsBlank(List<String> row) {
        boolean key = true;
        Iterator var3 = row.iterator();

        while(var3.hasNext()) {
            String cell = (String)var3.next();
            if (!"".equals(cell)) {
                key = false;
                break;
            }
        }

        return key;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

