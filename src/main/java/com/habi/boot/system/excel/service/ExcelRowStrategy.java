package com.habi.boot.system.excel.service;


import java.util.List;

public interface ExcelRowStrategy {
    void optRow(int var1, int var2, List<String> var3) throws ExcelException;

    void doService() throws ExcelException;
}

