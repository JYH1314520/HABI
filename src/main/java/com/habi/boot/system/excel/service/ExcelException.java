package com.habi.boot.system.excel.service;


import com.habi.boot.system.base.exception.BaseException;

public class ExcelException extends BaseException {
    public ExcelException(String code, String descriptionKey, Object[] parameters) {
        super(code, descriptionKey, parameters);
    }
}
