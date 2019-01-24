package com.habi.boot.sap.manager;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class RfcManager {
    private static final Logger log = LoggerFactory.getLogger(RfcManager.class);
    private static final String ABAP_AS_POOLED = "ABAP_AS_POOL";
    private static JCOProvider provider;
    private static JCoDestination destination;
    static {
        Properties properties = loadProperties();
        // catch IllegalStateException if an instance is already registered
        try {
            provider = new JCOProvider();
            Environment.registerDestinationDataProvider(provider);
            provider.changePropertiesForABAP_AS(ABAP_AS_POOLED, properties);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        createDataFile(ABAP_AS_POOLED, "jcoDestination", properties);
    }
    public static Properties loadProperties() {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "172.17.27.170");//服务器
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");        //系统编号
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "400");       //SAP集团
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "IT-DEV-HAND1");  //SAP用户名
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "IT12345");     //密码
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "zh");        //登录语言
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");  //最大连接数
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");     //最大连接线程

        return connectProperties;
    }



    public static JCoDestination getDestination() throws JCoException {
        if (destination == null) {
            destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        }
        return destination;
    }

    public static void execute(JCoFunction function) {
        System.out.println("call Function");
        System.out.println("SAP Function Name : " + function.getName());
        try {
            function.execute(getDestination());
        } catch (JCoException e) {
            e.printStackTrace();
        }
    }

    public static JCoFunction getFunction(String functionName) {
        JCoFunction function = null;
        try {
            function = getDestination().getRepository().getFunctionTemplate(functionName).getFunction();
        } catch (JCoException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return function;
    }

    /**
     * 创建SAP接口属性文件。
     * @param name	ABAP管道名称
     * @param suffix	属性文件后缀
     * @param properties	属性文件内容
     */
    private static void createDataFile(String name, String suffix, Properties properties){
        File cfg = new File(name+"."+suffix);
        if(cfg.exists()){
            cfg.deleteOnExit();
        }
        try{
            FileOutputStream fos = new FileOutputStream(cfg, false);
            properties.store(fos, "for tests only !");
            fos.close();
        }catch (Exception e){
            log.error("Create Data file fault, error msg: " + e.toString());
            throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);
        }
    }



}
