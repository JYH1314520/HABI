package com.habi.boot.system.excel.dto;

import java.util.List;

public class ExportConfig<T, E> {
    private String fileName;
    private List<E> columnsInfo;
    private T param = null;

    public ExportConfig() {
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<E> getColumnsInfo() {
        return this.columnsInfo;
    }

    public void setColumnsInfo(List<E> columnsInfo) {
        this.columnsInfo = columnsInfo;
    }

    public T getParam() {
        return this.param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}

