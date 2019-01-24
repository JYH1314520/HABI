package com.habi.boot.generator.dto;

import java.util.ArrayList;
import java.util.List;

public class DBTable {
    private String name;
    private List<DBColumn> columns = new ArrayList();
    private boolean isMultiLanguage = false;

    public DBTable() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMultiLanguage() {
        return this.isMultiLanguage;
    }

    public void setMultiLanguage(boolean multiLanguage) {
        this.isMultiLanguage = multiLanguage;
    }

    public List<DBColumn> getColumns() {
        return this.columns;
    }

    public void setColumns(List<DBColumn> columns) {
        this.columns = columns;
    }
}