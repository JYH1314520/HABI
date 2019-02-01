package com.habi.boot.generator.service.impl;

public class ListValue {
    private  String value ;
    private  String code ;
    public ListValue(String v,String c){
      this.code  = c;
      this.value  = v;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
