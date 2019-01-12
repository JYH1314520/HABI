package com.habi.boot.sap.manager;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JCOTest {
    private static final Logger log = LoggerFactory.getLogger(JCOTest.class);
    public static void main(String[] args)
    {
        getUser();
    }
    public static List<OT_INFO> getUser() {

        JCoFunction function = RfcManager.getFunction("ZNSRM072");

        JCoTable input = function.getTableParameterList().getTable("IT_INFO");
        input.appendRow();
        input.setValue("MATNR","01D2418");
        input.setValue("LIFNR","0000106647");
        input.setValue("WERKS","1150");
        RfcManager.execute(function);
        JCoParameterList outputParam = function.getTableParameterList();
        JCoTable exportTable = outputParam.getTable("OT_INFO");
        JCoTable reutnrTable = outputParam.getTable("RETURN");
        System.out.println("table row:" + exportTable.getNumRows());
        List<OT_INFO> list = new ArrayList<OT_INFO>();
        System.out.println("============返回记录===============");
        for (int i = 0; i < exportTable.getNumRows(); i++) {
            exportTable.setRow(i);
            for(int k = 0; k < exportTable.getMetaData().getFieldCount(); k++)
             {
              System.out.print(exportTable.getString(k) + "\n");
             //system.out.print(exportTable.getString("PRICE");  输出指定字段的值
             }
            OT_INFO ot_info = new OT_INFO();
            ot_info.setMATNR(exportTable.getString("MATNR"));
            ot_info.setLIFNR(exportTable.getString("LIFNR"));
            ot_info.setWERKS(exportTable.getString("WERKS"));
            ot_info.setREVLV(exportTable.getString("REVLV"));
            ot_info.setSTSMA(exportTable.getString("STSMA"));
            ot_info.setESTAT(exportTable.getString("ESTAT"));
            ot_info.setTXT04(exportTable.getString("TXT04"));
            ot_info.setTXT30(exportTable.getString("TXT30"));
            ot_info.setSFLAG(exportTable.getString("SFLAG"));
            ot_info.setMESSAGE(exportTable.getString("MESSAGE"));
            list.add(ot_info);
            //exportTable.nextRow();
        }
        //log.info(list.toString());
        System.out.println("============返回记录===============");
        for (OT_INFO info : list){
//            for (int i = 0; i < OT_INFO.; i++) {
//                System.out.print(info[i] + " --> ");
//            }
            System.out.print(info.toString());
        }
            return list;
    }
}
