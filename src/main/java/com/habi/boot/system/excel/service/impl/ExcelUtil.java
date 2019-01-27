package com.habi.boot.system.excel.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.habi.boot.system.excel.service.ExcelException;
import com.habi.boot.system.excel.service.ExcelRowStrategy;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExcelUtil extends DefaultHandler {
    private SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;
    private ExcelRowStrategy rowStrategy;
    private int sheetIndex = -1;
    private List<String> rowlist = new ArrayList();
    private int curRow = 0;
    private int curCol = 0;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String preRef = null;
    private String ref = null;
    private String maxRef = null;

    public ExcelUtil(ExcelRowStrategy rowStrategy) {
        this.rowStrategy = rowStrategy;
    }

    public void process(InputStream inputStream) throws ExcelException {
        if (null == this.rowStrategy) {
            throw new ExcelException((String)null, "需要rowStrategy", (Object[])null);
        } else {
            try {
                OPCPackage pkg = OPCPackage.open(inputStream);
                XSSFReader r = new XSSFReader(pkg);
                SharedStringsTable sst = r.getSharedStringsTable();
                XMLReader parser = this.fetchSheetParser(sst);
                Iterator sheets = r.getSheetsData();

                while(sheets.hasNext()) {
                    this.curRow = 0;
                    ++this.sheetIndex;
                    InputStream sheet = (InputStream)sheets.next();
                    InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                    sheet.close();
                }

                this.rowStrategy.doService();
            } catch (Exception var12) {
                this.logger.error(var12.getMessage(), var12);
                throw new ExcelException((String)null, "excel导入失败", (Object[])null);
            } finally {
                this.cleanData();
            }
        }
    }

    public void cleanData() {
        this.sheetIndex = -1;
        this.rowlist = new ArrayList();
        this.curRow = 0;
        this.curCol = 0;
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (name.equals("c")) {
            if (this.preRef == null) {
                this.preRef = attributes.getValue("r");
            } else {
                this.preRef = this.ref;
            }

            this.ref = attributes.getValue("r");
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s")) {
                this.nextIsString = true;
            } else {
                this.nextIsString = false;
            }
        }

        this.lastContents = "";
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        if (this.nextIsString) {
            int idx = Integer.parseInt(this.lastContents);
            this.lastContents = (new XSSFRichTextString(this.sst.getEntryAt(idx))).toString();
            this.nextIsString = false;
        }

        int len;
        int i;
        String value;
        if (name.equals("v")) {
            value = this.lastContents.trim();
            value = value.equals("") ? " " : value;
            if (!this.ref.equals(this.preRef)) {
                len = this.countNullCell(this.ref, this.preRef);

                for(i = 0; i < len; ++i) {
                    this.rowlist.add(this.curCol, "");
                    ++this.curCol;
                }
            }

            this.rowlist.add(this.curCol, value);
            ++this.curCol;
        } else if (name.equals("row")) {
            value = "";
            if (this.curRow == 1 && this.sheetIndex == 0) {
                this.maxRef = this.ref;
            }

            if (this.maxRef != null) {
                len = this.countNullCell(this.maxRef, this.ref);

                for(i = 0; i <= len; ++i) {
                    this.rowlist.add(this.curCol, "");
                    ++this.curCol;
                }
            }

            try {
                if (!this.rowlist.isEmpty()) {
                    this.rowStrategy.optRow(this.sheetIndex, this.curRow, this.rowlist);
                }
            } catch (ExcelException var7) {
                this.logger.error(var7.getMessage(), var7);
                throw new SAXException(var7.getMessage());
            }

            ++this.curRow;
            this.rowlist.clear();
            this.curCol = 0;
            this.preRef = null;
            this.ref = null;
        }

    }

    public int countNullCell(String ref, String preRef) {
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");
        xfd = this.fillChar(xfd, 3, '@', true);
        xfd_1 = this.fillChar(xfd_1, 3, '@', true);
        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
        return res - 1;
    }

    String fillChar(String str, int len, char let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            int i;
            if (isPre) {
                for(i = 0; i < len - len_1; ++i) {
                    str = let + str;
                }
            } else {
                for(i = 0; i < len - len_1; ++i) {
                    str = str + let;
                }
            }
        }

        return str;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        this.lastContents = this.lastContents + new String(ch, start, length);
    }
}

