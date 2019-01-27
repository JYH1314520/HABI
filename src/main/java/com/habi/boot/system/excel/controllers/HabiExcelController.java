package com.habi.boot.system.excel.controllers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.habi.boot.system.auth.entity.SysUserFunctionEntity;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.excel.dto.ColumnInfo;
import com.habi.boot.system.excel.dto.ExportConfig;
import com.habi.boot.system.excel.service.ExcelException;
import com.habi.boot.system.excel.service.IExportService;
import com.habi.boot.system.excel.service.IHabiExcelImportService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping({"/sys", "/api/sys"})
public class HabiExcelController extends BaseController {
    @Autowired
    IExportService excelService;
    @Autowired
    IHabiExcelImportService iImportService;
    @Autowired
    ObjectMapper objectMapper;

    public HabiExcelController() {
    }

    @RequestMapping({"/function/export"})
    public void createXLS(HttpServletRequest request, @RequestParam String config, HttpServletResponse httpServletResponse) throws IOException {
        IRequest requestContext = this.createRequestContext(request);
        JavaType type = this.objectMapper.getTypeFactory().constructParametrizedType(ExportConfig.class, ExportConfig.class, new Class[]{SysUserFunctionEntity.class, ColumnInfo.class});
        ExportConfig<SysUserFunctionEntity, ColumnInfo> exportConfig = (ExportConfig)this.objectMapper.readValue(config, type);
        this.excelService.exportAndDownloadExcel("com.hand.hap.function.mapper.FunctionMapper.selectAll", exportConfig, request, httpServletResponse, requestContext);
    }

    @RequestMapping({"/export/template/{tableName}"})
    public void exportImportTemplate(@PathVariable String tableName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.iImportService.exportExcelTemplate(tableName, response, request);
    }

    @RequestMapping({"/import/{tableName}"})
    public void importXLS(HttpServletRequest request, HttpServletResponse response, @PathVariable String tableName) throws IOException, ExcelException, SQLException, FileUploadException {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = upload.parseRequest((RequestContext) request);
        if (!items.isEmpty()) {
            Iterator iterator = items.iterator();

            while (iterator.hasNext()) {
                FileItem item = (FileItem) iterator.next();
                if (!item.isFormField()) {
                    InputStream fs = item.getInputStream();
                    this.iImportService.loadExcel(fs, tableName);
                }
            }
        }

    }
}
