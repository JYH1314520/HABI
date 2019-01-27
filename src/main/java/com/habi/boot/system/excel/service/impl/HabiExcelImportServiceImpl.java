package com.habi.boot.system.excel.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import com.habi.boot.system.excel.service.ExcelException;
import com.habi.boot.system.excel.service.IHabiExcelImportService;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

@Service
@Transactional
public class HapExcelImportService implements IHabiExcelImportService {
    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    ThreadLocal<DefaultRowStrategy> defaultRowStrategy = ThreadLocal.withInitial(() -> {
        return new DefaultRowStrategy(this.dataSource);
    });

    public HapExcelImportService() {
    }

    public void loadExcel(InputStream inputStream, String tableName) throws ExcelException {
        DefaultRowStrategy rowStrategy = (DefaultRowStrategy)this.defaultRowStrategy.get();
        rowStrategy.setTableName(tableName);
        ExcelUtil excelUtil = new ExcelUtil(rowStrategy);
        excelUtil.process(inputStream);
    }

    private void createExcelTemplate(String tableName, OutputStream outputStream) throws IOException {
        EntityTable entityTable = EntityHelper.getEntityTable(tableName.getClass());
        SXSSFWorkbook wb = new SXSSFWorkbook(50);
        SXSSFSheet sheet = (SXSSFSheet) wb.createSheet();
        sheet.createRow(0).createCell(0).setCellValue("*");
        SXSSFRow firstRow = (SXSSFRow) sheet.createRow(1);
        Set<EntityColumn> entityColumn = entityTable.getEntityClassColumns();
        int columnSize = entityColumn.size();
        EntityColumn[] columns = (EntityColumn[])entityColumn.toArray(new EntityColumn[columnSize]);
        List<String> pkColumns = new ArrayList();
        entityTable.getEntityClassPKColumns().forEach((v) -> {
            if (null != v.getGenerator()) {
                pkColumns.add(v.getColumn());
            }

        });
        int index = 0;

        for(int i = 0; i < columns.length; ++i) {
            String name = columns[i].getColumn();
            if (!"OBJECT_VERSION_NUMBER".equalsIgnoreCase(name) && !"REQUEST_ID".equalsIgnoreCase(name) && !"PROGRAM_ID".equalsIgnoreCase(name) && !"CREATED_BY".equalsIgnoreCase(name) && !"CREATION_DATE".equalsIgnoreCase(name) && !"LAST_UPDATED_BY".equalsIgnoreCase(name) && !"LAST_UPDATE_DATE".equalsIgnoreCase(name) && !"LAST_UPDATE_LOGIN".equalsIgnoreCase(name) && !pkColumns.contains(name)) {
                SXSSFCell firstCell = (SXSSFCell) firstRow.createCell(index++);
                firstCell.setCellValue(name);
            }
        }

        try {
            wb.write(outputStream);
        } catch (IOException var18) {
            throw var18;
        } finally {
            wb.close();
        }

    }

    public void exportExcelTemplate(String tableName, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) throws IOException {
        this.setExcelHeader(httpServletResponse, httpServletRequest, "template");
        this.createExcelTemplate(tableName, httpServletResponse.getOutputStream());
    }

    private void setExcelHeader(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, String fileName) throws UnsupportedEncodingException {
        String name = fileName + ".xlsx";
        String userAgent = httpServletRequest.getHeader("User-Agent");
        if (userAgent.contains("Firefox")) {
            name = new String(name.getBytes("UTF-8"), "ISO8859-1");
        } else {
            name = URLEncoder.encode(name, "UTF-8");
        }

        httpServletResponse.addHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        httpServletResponse.setContentType("application/vnd.ms-excel;charset=UTF-8");
        httpServletResponse.setHeader("Accept-Ranges", "bytes");
    }
}
