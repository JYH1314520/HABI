package com.habi.boot.generator.dto;

public class XmlColumnsInfo {
    private String tableColumnsName;
    private String dBColumnsName;
    private String jdbcType;

    public XmlColumnsInfo() {
    }

    public String getTableColumnsName() {
        return this.tableColumnsName;
    }

    public void setTableColumnsName(String tableColumnsName) {
        this.tableColumnsName = tableColumnsName;
    }

    public String getdBColumnsName() {
        return this.dBColumnsName;
    }

    public void setdBColumnsName(String dBColumnsName) {
        this.dBColumnsName = dBColumnsName;
    }

    public String getJdbcType() {
        return this.jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}
