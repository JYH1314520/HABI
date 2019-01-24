package com.habi.boot.generator.dto;

import java.util.List;

public class FtlInfo {
    private String fileName;
    private String packageName;
    private List<String> importName;
    private List<XmlColumnsInfo> columnsInfo;
    private String dir;
    private String projectPath;
    private String htmlModelName;

    public FtlInfo() {
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImportName() {
        return this.importName;
    }

    public void setImportName(List<String> importName) {
        this.importName = importName;
    }

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getProjectPath() {
        return this.projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getHtmlModelName() {
        return this.htmlModelName;
    }

    public void setHtmlModelName(String htmlModelName) {
        this.htmlModelName = htmlModelName;
    }

    public List<XmlColumnsInfo> getColumnsInfo() {
        return this.columnsInfo;
    }

    public void setColumnsInfo(List<XmlColumnsInfo> columnsInfo) {
        this.columnsInfo = columnsInfo;
    }
}

