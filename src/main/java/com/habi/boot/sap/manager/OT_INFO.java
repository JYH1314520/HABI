package com.habi.boot.sap.manager;

import java.io.Serializable;

public class OT_INFO  implements Serializable {
    private  String  MATNR;
    private  String  LIFNR;
    private  String  WERKS;
    private  String  REVLV;
    private  String  STSMA;
    private  String  ESTAT;
    private  String  TXT04;
    private  String  TXT30;
    private  String  SFLAG;
    private  String  MESSAGE;

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    public String getLIFNR() {
        return LIFNR;
    }

    public void setLIFNR(String LIFNR) {
        this.LIFNR = LIFNR;
    }

    public String getWERKS() {
        return WERKS;
    }

    public void setWERKS(String WERKS) {
        this.WERKS = WERKS;
    }

    public String getREVLV() {
        return REVLV;
    }

    public void setREVLV(String REVLV) {
        this.REVLV = REVLV;
    }

    public String getSTSMA() {
        return STSMA;
    }

    public void setSTSMA(String STSMA) {
        this.STSMA = STSMA;
    }

    public String getESTAT() {
        return ESTAT;
    }

    public void setESTAT(String ESTAT) {
        this.ESTAT = ESTAT;
    }

    public String getTXT04() {
        return TXT04;
    }

    public void setTXT04(String TXT04) {
        this.TXT04 = TXT04;
    }

    public String getTXT30() {
        return TXT30;
    }

    public void setTXT30(String TXT30) {
        this.TXT30 = TXT30;
    }

    public String getSFLAG() {
        return SFLAG;
    }

    public void setSFLAG(String SFLAG) {
        this.SFLAG = SFLAG;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }
}
