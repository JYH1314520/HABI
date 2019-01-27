package com.habi.boot.system.excel.service;

import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.excel.dto.ExportConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IExportService {
    void exportAndDownloadExcel(String var1, ExportConfig var2, HttpServletRequest var3, HttpServletResponse var4, IRequest var5) throws IOException;

    void exportAndDownloadExcel(String var1, ExportConfig var2, HttpServletRequest var3, HttpServletResponse var4, IRequest var5, int var6) throws IOException;
}

