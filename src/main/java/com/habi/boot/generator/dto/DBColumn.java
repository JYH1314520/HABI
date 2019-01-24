package com.habi.boot.generator.dto;

public class DBColumn {
    private String name;
    private boolean isId = false;
    private boolean isNotEmpty = false;
    private boolean isNotNull = false;
    private String javaType;
    private String jdbcType;
    private String columnLength;
    private boolean isMultiLanguage = false;
    private String remarks;

    public DBColumn() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isId() {
        return this.isId;
    }

    public void setId(boolean id) {
        this.isId = id;
    }

    public String getJavaType() {
        return this.javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return this.jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public boolean isMultiLanguage() {
        return this.isMultiLanguage;
    }

    public void setMultiLanguage(boolean multiLanguage) {
        this.isMultiLanguage = multiLanguage;
    }

    public boolean isNotEmpty() {
        return this.isNotEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.isNotEmpty = notEmpty;
    }

    public boolean isNotNull() {
        return this.isNotNull;
    }

    public void setNotNull(boolean notNull) {
        this.isNotNull = notNull;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getColumnLength() {
        return this.columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }
}

