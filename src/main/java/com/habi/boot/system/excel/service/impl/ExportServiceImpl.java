package com.habi.boot.system.excel.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.excel.dto.ColumnInfo;
import com.habi.boot.system.excel.dto.ExportConfig;
import com.habi.boot.system.excel.service.IExportService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
public class ExportServiceImpl implements IExportService {
    private static final String ENC = "UTF-8";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    @Qualifier("sqlSessionFactory")
    SqlSessionFactory sqlSessionFactory;

    public ExportServiceImpl() {
    }

    private void exportExcel(String sqlId, ExportConfig gridInfo, IRequest iRequest, OutputStream outputStream, int rowMaxNumber) throws IOException {
        SXSSFWorkbook wxb = new SXSSFWorkbook(50);
        CellStyle dateFormat = wxb.createCellStyle();
        dateFormat.setDataFormat(wxb.createDataFormat().getFormat("yyyy-MM-DD HH:mm:ss"));
        AtomicInteger count = new AtomicInteger(1);
        AtomicInteger rowIndex = new AtomicInteger(1);
        this.initColumnType(gridInfo.getColumnsInfo(), gridInfo.getParam());
        SXSSFSheet[] sheet = new SXSSFSheet[]{(SXSSFSheet) wxb.createSheet()};
        this.createHeaderRow(gridInfo.getColumnsInfo(), wxb, sheet[0]);

        try {
            SqlSession sqlSession = this.sqlSessionFactory.openSession();
            Throwable var12 = null;

            try {
                sqlSession.select(sqlId, gridInfo.getParam(), (resultContext) -> {
                    Object object = resultContext.getResultObject();
                    sheet[0] = this.createSheet(wxb, sheet[0], object, count, rowIndex, rowMaxNumber, gridInfo, dateFormat);
                });
                wxb.write(outputStream);
            } catch (Throwable var28) {
                var12 = var28;
                throw var28;
            } finally {
                if (sqlSession != null) {
                    if (var12 != null) {
                        try {
                            sqlSession.close();
                        } catch (Throwable var27) {
                            var12.addSuppressed(var27);
                        }
                    } else {
                        sqlSession.close();
                    }
                }

            }
        } finally {
            //wxb.close();
            wxb.dispose();
        }

    }

    private SXSSFSheet createSheet(SXSSFWorkbook wb, SXSSFSheet sheet, Object object, AtomicInteger count, AtomicInteger rowIndex, int rowMaxNumber, ExportConfig gridInfo, CellStyle dateFormat) {
        if (count.get() % rowMaxNumber == 0) {
            sheet = (SXSSFSheet) wb.createSheet();
            this.createHeaderRow(gridInfo.getColumnsInfo(), wb, sheet);
            rowIndex.set(0);
        }

        count.getAndIncrement();
        SXSSFRow row = (SXSSFRow) sheet.createRow(rowIndex.getAndIncrement());
        this.createRow(gridInfo.getColumnsInfo(), object, row, dateFormat);
        return sheet;
    }

    private void createRow(List<ColumnInfo> columnInfos, Object object, SXSSFRow row, CellStyle dateFormat) {
        for(int ii = 0; ii < columnInfos.size(); ++ii) {
            Object fieldObject = null;

            try {
                fieldObject = PropertyUtils.getProperty(object, ((ColumnInfo)columnInfos.get(ii)).getName());
            } catch (Exception var11) {
                this.logger.error(var11.getMessage(), var11);
            }

            String type = ((ColumnInfo)columnInfos.get(ii)).getType();
            SXSSFCell cell = (SXSSFCell) row.createCell(ii);
            if (null == fieldObject) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String)null);
            } else {
                String var9 = type.toUpperCase();
                byte var10 = -1;
                switch(var9.hashCode()) {
                    case -1981034679:
                        if (var9.equals("NUMBER")) {
                            var10 = 0;
                        }
                        break;
                    case -1618932450:
                        if (var9.equals("INTEGER")) {
                            var10 = 4;
                        }
                        break;
                    case 72655:
                        if (var9.equals("INT")) {
                            var10 = 3;
                        }
                        break;
                    case 2090926:
                        if (var9.equals("DATE")) {
                            var10 = 6;
                        }
                        break;
                    case 2342524:
                        if (var9.equals("LONG")) {
                            var10 = 5;
                        }
                        break;
                    case 66988604:
                        if (var9.equals("FLOAT")) {
                            var10 = 1;
                        }
                        break;
                    case 782694408:
                        if (var9.equals("BOOLEAN")) {
                            var10 = 7;
                        }
                        break;
                    case 2022338513:
                        if (var9.equals("DOUBLE")) {
                            var10 = 2;
                        }
                }

                switch(var10) {
                    case 0:
                    case 1:
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue((double)(Float)fieldObject);
                        break;
                    case 2:
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue((Double)fieldObject);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue((double)(Long)fieldObject);
                        break;
                    case 6:
                        cell.setCellStyle(dateFormat);
                        cell.setCellValue((Date)fieldObject);
                        break;
                    case 7:
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        cell.setCellValue((Boolean)fieldObject);
                        break;
                    default:
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue((String)fieldObject);
                }
            }
        }

    }

    private void createHeaderRow(List<ColumnInfo> columnInfos, SXSSFWorkbook wb, SXSSFSheet sheet) {
        SXSSFRow firstRow = (SXSSFRow) sheet.createRow(0);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        for(int i = 0; i < columnInfos.size(); ++i) {
            SXSSFCell firstCell = (SXSSFCell) firstRow.createCell(i);
            firstCell.setCellValue(((ColumnInfo)columnInfos.get(i)).getTitle());
            sheet.setColumnWidth(i, ((ColumnInfo)columnInfos.get(i)).getWidth() * 80);
            firstCell.setCellStyle(cellStyle);
        }

    }

    private void initColumnType(List<ColumnInfo> columnInfos, Object object) {
        Iterator var3 = columnInfos.iterator();

        while(var3.hasNext()) {
            ColumnInfo columnInfo = (ColumnInfo)var3.next();
            columnInfo.setType(ReflectionUtils.findField(object.getClass(), columnInfo.getName()).getType().getSimpleName());
        }

    }

    public void exportAndDownloadExcel(String sqlId, ExportConfig exportConfig, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, IRequest iRequest, int rowMaxNumber) throws IOException {
        String name = exportConfig.getFileName() + ".xlsx";
        String userAgent = httpServletRequest.getHeader("User-Agent");
        if (userAgent.contains("Firefox")) {
            name = new String(name.getBytes("UTF-8"), "ISO8859-1");
        } else {
            name = URLEncoder.encode(name, "UTF-8");
        }

        httpServletResponse.addHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        httpServletResponse.setContentType("application/vnd.ms-excel;charset=UTF-8");
        httpServletResponse.setHeader("Accept-Ranges", "bytes");
        OutputStream outputStream = httpServletResponse.getOutputStream();
        Throwable var10 = null;

        try {
            this.exportExcel(sqlId, exportConfig, iRequest, outputStream, rowMaxNumber);
        } catch (Throwable var19) {
            var10 = var19;
            throw var19;
        } finally {
            if (outputStream != null) {
                if (var10 != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable var18) {
                        var10.addSuppressed(var18);
                    }
                } else {
                    outputStream.close();
                }
            }

        }

    }

    public void exportAndDownloadExcel(String sqlId, ExportConfig exportConfig, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, IRequest iRequest) throws IOException {
        this.exportAndDownloadExcel(sqlId, exportConfig, httpServletRequest, httpServletResponse, iRequest, 1000000);
    }
}

