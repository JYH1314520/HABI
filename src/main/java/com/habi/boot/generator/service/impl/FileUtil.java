package com.habi.boot.generator.service.impl;


import com.github.pagehelper.util.StringUtil;
import com.habi.boot.generator.dto.*;
import com.habi.boot.system.base.*;
import com.habi.boot.system.base.impl.BaseServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    private static List<String> allClassFiles = new ArrayList();
    private static List<String> allXmlFiles = new ArrayList();
    private static List<String> allHtmlFiles = new ArrayList();

    private FileUtil() {
    }

    public static String columnToCamel(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(sb);
        String s = sb.toString();
        return s;
    }

    public static String camelToColumn(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    public static void createDto(DBTable table, GeneratorInfo generatorInfo) throws IOException {
        boolean needUtil = false;
        boolean needNotNull = false;
        boolean needNotEmpty = false;
        String name = generatorInfo.getDtoName().substring(0, generatorInfo.getDtoName().indexOf("."));
        String projectPath = generatorInfo.getProjectPath();
        String parentPackagePath = generatorInfo.getParentPackagePath();
        String packagePath = generatorInfo.getPackagePath();
        String directory = projectPath + "/src/main/java/" + parentPackagePath + "/" + packagePath + "/entity/" + generatorInfo.getDtoName();
        File file = new File(directory);
        createFileDir(file);
        file.createNewFile();
        List<DBColumn> columns = table.getColumns();
        Iterator var12 = columns.iterator();

        String d;
        while(var12.hasNext()) {
            DBColumn s = (DBColumn)var12.next();
            d = s.getJdbcType().toUpperCase();
            byte var15 = -1;
            switch(d.hashCode()) {
                case -2034720975:
                    if (d.equals("DECIMAL")) {
                        var15 = 7;
                    }
                    break;
                case -1981034679:
                    if (d.equals("NUMBER")) {
                        var15 = 10;
                    }
                    break;
                case -1718637701:
                    if (d.equals("DATETIME")) {
                        var15 = 2;
                    }
                    break;
                case -1618932450:
                    if (d.equals("INTEGER")) {
                        var15 = 6;
                    }
                    break;
                case -1453246218:
                    if (d.equals("TIMESTAMP")) {
                        var15 = 1;
                    }
                    break;
                case -594415409:
                    if (d.equals("TINYINT")) {
                        var15 = 8;
                    }
                    break;
                case 72655:
                    if (d.equals("INT")) {
                        var15 = 5;
                    }
                    break;
                case 2090926:
                    if (d.equals("DATE")) {
                        var15 = 0;
                    }
                    break;
                case 2575053:
                    if (d.equals("TIME")) {
                        var15 = 3;
                    }
                    break;
                case 66988604:
                    if (d.equals("FLOAT")) {
                        var15 = 11;
                    }
                    break;
                case 1959128815:
                    if (d.equals("BIGINT")) {
                        var15 = 4;
                    }
                    break;
                case 2022338513:
                    if (d.equals("DOUBLE")) {
                        var15 = 9;
                    }
            }

            switch(var15) {
                case 0:
                case 1:
                case 2:
                case 3:
                    s.setJavaType("Date");
                    s.setJdbcType("DATE");
                    needUtil = true;
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    s.setJavaType("Long");
                    s.setJdbcType("DECIMAL");
                    break;
                case 9:
                    s.setJavaType("Double");
                    s.setJdbcType("DECIMAL");
                    break;
                case 10:
                case 11:
                    s.setJavaType("Float");
                    s.setJdbcType("DECIMAL");
                    break;
                default:
                    s.setJavaType("String");
                    s.setJdbcType("VARCHAR");
            }

            if (s.isNotNull()) {
                needNotNull = true;
            } else if (s.isNotEmpty()) {
                needNotEmpty = true;
            }
        }

        StringBuilder sb = new StringBuilder();
        String dir1 = parentPackagePath + "/" + packagePath + "/entity";
        dir1 = dir1.replaceAll("/", ".");
        sb.append("package " + dir1 + ";\r\n\r\n");
        sb.append("/**Auto Generated By  Code Generator**/\r\n");
        sb.append("import javax.persistence.GeneratedValue;\r\n");
        sb.append("import javax.persistence.Id;\r\n");
        sb.append("import com.habi.boot.system.base.annotation.TableGeneratedValue;\r\n");
        sb.append("import com.habi.boot.system.config.DatabaseTypeConfig;\r\n");
        sb.append("import org.hibernate.validator.constraints.Length;\r\n");
        sb.append("import javax.persistence.Table;\r\n");
        sb.append("import javax.persistence.Column;\r\n");
        sb.append("import javax.persistence.GenerationType;\r\n");
        d = BaseEntity.class.getName();
        sb.append("import " + d + ";\r\n");
        if (needUtil) {
            sb.append("import java.util.Date;\r\n");
        }

        if (needNotNull) {
            sb.append("import javax.validation.constraints.NotNull;\r\n");
        }

        if (needNotEmpty) {
            sb.append("import org.hibernate.validator.constraints.NotEmpty;\r\n");
        }



        sb.append("@Table(name = \"" + table.getName() + "\")\r\n");
        sb.append("public class " + name + "  extends BaseEntity {\r\n\r\n");
        Iterator var23 = columns.iterator();

        String str;
        DBColumn cl;
        while(var23.hasNext()) {
            cl = (DBColumn)var23.next();
            str = "     public static final String FIELD_" + cl.getName().toUpperCase() + " = \"" + columnToCamel(cl.getName()) + "\";\r\n";
            sb.append(str);
        }

        sb.append("\r\n\r\n");
        var23 = columns.iterator();

        while(var23.hasNext()) {
            cl = (DBColumn)var23.next();
            if (cl.isId()) {
                sb.append("     @Id\r\n     @GeneratedValue(strategy=GenerationType.IDENTITY)\r\n      @TableGeneratedValue(databaseType = DatabaseTypeConfig.databaseType)\r\n");
            } else {
                if (cl.isNotEmpty()) {
                    sb.append("     @NotEmpty\r\n");
                } else if (cl.isNotNull()) {
                    sb.append("     @NotNull\r\n");
                }

                if (cl.getJavaType().equalsIgnoreCase("String")) {
                    sb.append("     @Length(max = ");
                    sb.append(cl.getColumnLength() + ")\r\n");
                }
            }
            sb.append("     @Column(name = \"" + cl.getName().toUpperCase()  + "\")\r\n");
            if (cl.isMultiLanguage()) {
                sb.append("     @MultiLanguageField\r\n");
            }

            str = "     private " + cl.getJavaType() + " " + columnToCamel(cl.getName()) + ";";
            if (!StringUtil.isEmpty(cl.getRemarks())) {
                str = str + " //" + cl.getRemarks();
            }

            str = str + "\r\n\r\n";
            sb.append(str);
        }

        sb.append("\r\n");
        var23 = columns.iterator();

        while(var23.hasNext()) {
            cl = (DBColumn)var23.next();
            str = columnToCamel(cl.getName());
            String name2 = str.substring(0, 1).toUpperCase() + str.substring(1);
            sb.append("     public void set" + name2 + "(" + cl.getJavaType() + " " + str + "){\r\n");
            sb.append("         this." + str + " = " + str + ";\r\n");
            sb.append("     }\r\n\r\n");
            sb.append("     public " + cl.getJavaType() + " get" + name2 + "(){\r\n");
            sb.append("         return " + str + ";\r\n");
            sb.append("     }\r\n\r\n");
        }

        sb.append("     }\r\n");
        PrintWriter p = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
        p.write(sb.toString());
        p.close();
    }

    public static void createFtlInfoByType(FileUtil.pType type, DBTable table, GeneratorInfo generatorInfo) throws IOException, TemplateException {
        String projectPath = generatorInfo.getProjectPath();
        String parentPackagePath = generatorInfo.getParentPackagePath();
        String packagePath = generatorInfo.getPackagePath();
        String htmlModelName = generatorInfo.getHtmlModelName();
        String pac = parentPackagePath + "/" + packagePath;
        FtlInfo info = new FtlInfo();
        String directory = null;
        List<String> importPackages = new ArrayList();
        if (type == FileUtil.pType.Controller) {
            directory = projectPath + "/src/main/java/" + pac + "/controllers/" + generatorInfo.getControllerName();
            importPackages.add("org.springframework.stereotype.Controller");
            importPackages.add(BaseController.class.getName());
            importPackages.add(IRequest.class.getName());
            importPackages.add(ResponseData.class.getName());
        } else if (type == FileUtil.pType.Mapper) {
            directory = projectPath + "/src/main/java/" + pac + "/mapper/" + generatorInfo.getMapperName();
            importPackages.add("com.habi.boot.system.base.mapper.Mapper");
        } else if (type == FileUtil.pType.MapperXml) {
            directory = projectPath + "/src/main/resources/" + pac + "/mapper/" + generatorInfo.getMapperXmlName();
        } else if (type == FileUtil.pType.Service) {
            directory = projectPath + "/src/main/java/" + pac + "/service/" + generatorInfo.getServiceName();
            importPackages.add(ProxySelf.class.getName());
            importPackages.add(IBaseService.class.getName());
        } else if (type == FileUtil.pType.Impl) {
            directory = projectPath + "/src/main/java/" + pac + "/service/impl/" + generatorInfo.getImplName();
            importPackages.add(BaseServiceImpl.class.getName());
            importPackages.add("org.springframework.stereotype.Service");
        } else if (type == FileUtil.pType.Html) {
            directory = projectPath + "/src/main/webapp/WEB-INF/views/" + packagePath + "/" + generatorInfo.getHtmlName();
        }

        pac = pac.replaceAll("/", ".");
        info.setPackageName(pac);
        info.setDir(directory);
        info.setProjectPath(projectPath);
        info.setImportName(importPackages);
        info.setHtmlModelName(htmlModelName);
        List<DBColumn> columns = table.getColumns();
        List<XmlColumnsInfo> columnsInfos = new ArrayList();
        Iterator var13 = columns.iterator();

        while(var13.hasNext()) {
            DBColumn column = (DBColumn)var13.next();
            XmlColumnsInfo columnsInfo = new XmlColumnsInfo();
            columnsInfo.setTableColumnsName(columnToCamel(column.getName()));
            columnsInfo.setdBColumnsName(column.getName());
            columnsInfo.setJdbcType(column.getJdbcType());
            columnsInfos.add(columnsInfo);
        }

        info.setColumnsInfo(columnsInfos);
        createFtl(info, type, generatorInfo);
    }

    public static void createFtl(FtlInfo ftlInfo, FileUtil.pType type, GeneratorInfo generatorInfo) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        String projectPath = generatorInfo.getProjectPath();
        String directory = projectPath + "/src/main/resources/templates/generator/ftl/";
        Template template = null;
        Map<String, Object> map = new HashMap();
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
       // cfg.setServletContextForTemplateLoading(request.getServletContext(), "/templates/generator/ftl/");
        cfg.setDirectoryForTemplateLoading(new File(directory));
        if (type == FileUtil.pType.Controller) {
            template = cfg.getTemplate("controllers.ftl");
        } else if (type == FileUtil.pType.MapperXml) {
            template = cfg.getTemplate("mapperxml.ftl");
        } else if (type == FileUtil.pType.Mapper) {
            template = cfg.getTemplate("mapper.ftl");
        } else if (type == FileUtil.pType.Service) {
            template = cfg.getTemplate("service.ftl");
        } else if (type == FileUtil.pType.Impl) {
            template = cfg.getTemplate("impl.ftl");
        } else if (type == FileUtil.pType.Html) {
            template = cfg.getTemplate(ftlInfo.getHtmlModelName());
        }

        if (null != template) {
            template.setEncoding("UTF-8");
            File file = new File(ftlInfo.getDir());
            createFileDir(file);
            OutputStream out = new FileOutputStream(file);
            map.put("package", ftlInfo.getPackageName());
            map.put("import", ftlInfo.getImportName());
            map.put("name", ftlInfo.getFileName());
            map.put("dtoName", generatorInfo.getDtoName().substring(0, generatorInfo.getDtoName().indexOf(46)));
            map.put("controllerName", generatorInfo.getControllerName().substring(0, generatorInfo.getControllerName().indexOf(46)));
            map.put("implName", generatorInfo.getImplName().substring(0, generatorInfo.getImplName().indexOf(46)));
            map.put("serviceName", generatorInfo.getServiceName().substring(0, generatorInfo.getServiceName().indexOf(46)));
            map.put("mapperName", generatorInfo.getMapperName().substring(0, generatorInfo.getMapperName().indexOf(46)));
            map.put("xmlName", generatorInfo.getMapperXmlName().substring(0, generatorInfo.getMapperXmlName().indexOf(46)));
            map.put("columnsInfo", ftlInfo.getColumnsInfo());
            String url = generatorInfo.getTargetName().toLowerCase();
            if (url.endsWith("_b")) {
                url = url.substring(0, url.length() - 2);
            }

            url = url.replaceAll("_", "/");
            map.put("queryUrl", "/" + url + "/query");
            map.put("submitUrl", "/" + url + "/submit");
            map.put("removeUrl", "/" + url + "/remove");
            template.process(map, new OutputStreamWriter(out));
            out.flush();
            out.close();
        }

    }

    public static void createFileDir(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

    }

    public static int isFileExist(GeneratorInfo generatorInfo) {
        int rs = 0;
        String classDir = generatorInfo.getProjectPath() + "/src/main/java/" + generatorInfo.getParentPackagePath();
        String xmlDir = generatorInfo.getProjectPath() + "/src/main/resources/" + generatorInfo.getParentPackagePath();
        String htmlDir = generatorInfo.getProjectPath() + "/src/main/webapp/WEB-INF/views";
        getFileList(classDir, classDir, generatorInfo);
        Iterator var5 = allClassFiles.iterator();

        String name;
        File file1;
        while(var5.hasNext()) {
            name = (String)var5.next();
            if (name.equals(generatorInfo.getDtoName())) {
                if ("Create".equalsIgnoreCase(generatorInfo.getDtoStatus())) {
                    rs = 1;
                    break;
                }

                if ("Cover".equalsIgnoreCase(generatorInfo.getDtoStatus())) {
                    file1 = new File(classDir + "/" + generatorInfo.getPackagePath() + "/dto/" + generatorInfo.getDtoName());
                    if (!file1.exists()) {
                        rs = 1;
                        break;
                    }
                }
            }
        }

        if (rs == 0) {
            var5 = allClassFiles.iterator();

            while(var5.hasNext()) {
                name = (String)var5.next();
                if (name.equals(generatorInfo.getServiceName())) {
                    if ("Create".equalsIgnoreCase(generatorInfo.getServiceStatus())) {
                        rs = 2;
                        break;
                    }

                    if ("Cover".equalsIgnoreCase(generatorInfo.getServiceStatus())) {
                        file1 = new File(classDir + "/" + generatorInfo.getPackagePath() + "/service/" + generatorInfo.getServiceName());
                        if (!file1.exists()) {
                            rs = 2;
                            break;
                        }
                    }
                }
            }
        }

        if (rs == 0) {
            var5 = allClassFiles.iterator();

            while(var5.hasNext()) {
                name = (String)var5.next();
                if (name.equals(generatorInfo.getImplName())) {
                    if ("Create".equalsIgnoreCase(generatorInfo.getImplStatus())) {
                        rs = 3;
                        break;
                    }

                    if ("Cover".equalsIgnoreCase(generatorInfo.getImplStatus())) {
                        file1 = new File(classDir + "/" + generatorInfo.getPackagePath() + "/service/impl/" + generatorInfo.getImplName());
                        if (!file1.exists()) {
                            rs = 3;
                            break;
                        }
                    }
                }
            }
        }

        if (rs == 0) {
            var5 = allClassFiles.iterator();

            while(var5.hasNext()) {
                name = (String)var5.next();
                if (name.equals(generatorInfo.getControllerName())) {
                    if ("Create".equalsIgnoreCase(generatorInfo.getControllerStatus())) {
                        rs = 4;
                        break;
                    }

                    if ("Cover".equalsIgnoreCase(generatorInfo.getControllerStatus())) {
                        file1 = new File(classDir + "/" + generatorInfo.getPackagePath() + "/controllers/" + generatorInfo.getControllerName());
                        if (!file1.exists()) {
                            rs = 4;
                            break;
                        }
                    }
                }
            }
        }

        if (rs == 0) {
            var5 = allClassFiles.iterator();

            while(var5.hasNext()) {
                name = (String)var5.next();
                if (name.equals(generatorInfo.getMapperName())) {
                    if ("Create".equalsIgnoreCase(generatorInfo.getMapperStatus())) {
                        rs = 5;
                        break;
                    }

                    if ("Cover".equalsIgnoreCase(generatorInfo.getMapperStatus())) {
                        file1 = new File(classDir + "/" + generatorInfo.getPackagePath() + "/core.base.system.mapper/" + generatorInfo.getMapperName());
                        if (!file1.exists()) {
                            rs = 5;
                            break;
                        }
                    }
                }
            }
        }

        if (rs == 0) {
            getFileList(xmlDir, xmlDir, generatorInfo);
            var5 = allXmlFiles.iterator();

            while(var5.hasNext()) {
                name = (String)var5.next();
                if (name.equals(generatorInfo.getMapperXmlName())) {
                    if ("Create".equalsIgnoreCase(generatorInfo.getMapperXmlStatus())) {
                        rs = 6;
                        break;
                    }

                    if ("Cover".equalsIgnoreCase(generatorInfo.getMapperXmlStatus())) {
                        file1 = new File(xmlDir + "/" + generatorInfo.getPackagePath() + "/core.base.system.mapper/" + generatorInfo.getMapperXmlName());
                        if (!file1.exists()) {
                            rs = 6;
                            break;
                        }
                    }
                }
            }
        }

        if (rs == 0) {
            getFileList(htmlDir, htmlDir, generatorInfo);
            var5 = allHtmlFiles.iterator();

            while(var5.hasNext()) {
                name = (String)var5.next();
                if (name.equals(generatorInfo.getHtmlName())) {
                    if ("Create".equalsIgnoreCase(generatorInfo.getHtmlStatus())) {
                        rs = 7;
                        break;
                    }

                    if ("Cover".equalsIgnoreCase(generatorInfo.getHtmlStatus())) {
                        file1 = new File(htmlDir + "/" + generatorInfo.getPackagePath() + "/" + generatorInfo.getHtmlName());
                        if (!file1.exists()) {
                            rs = 7;
                            break;
                        }
                    }
                }
            }
        }

        return rs;
    }

    public static void getFileList(String basePath, String directory, GeneratorInfo generatorInfo) {
        File dir = new File(basePath);
        File[] files = dir.listFiles();
        if (files != null) {
            for(int i = 0; i < files.length; ++i) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    getFileList(files[i].getAbsolutePath(), directory, generatorInfo);
                } else if (directory.equals(generatorInfo.getProjectPath() + "/src/main/java/" + generatorInfo.getParentPackagePath())) {
                    allClassFiles.add(fileName);
                } else if (directory.equals(generatorInfo.getProjectPath() + "/src/main/resources/" + generatorInfo.getParentPackagePath())) {
                    allXmlFiles.add(fileName);
                } else if (directory.equals(generatorInfo.getProjectPath() + "/src/main/webapp/WEB-INF/views")) {
                    allHtmlFiles.add(fileName);
                }
            }
        }

    }

    public static enum pType {
        Controller,
        MapperXml,
        Mapper,
        Service,
        Impl,
        Html;

        private pType() {
        }
    }
}
