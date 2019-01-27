package com.habi.boot.system.excel.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public interface IHabiExcelImportService {
    void loadExcel(InputStream var1, String var2) throws IOException, ExcelException, SQLException;

    void exportExcelTemplate(String var1, HttpServletResponse var2, HttpServletRequest var3) throws IOException;
}