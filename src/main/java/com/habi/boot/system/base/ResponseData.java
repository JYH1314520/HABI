package com.habi.boot.system.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.Page;
import com.habi.boot.system.auth.entity.SysUserEntity;

import java.util.List;

public class ResponseData {
    @JsonInclude(Include.NON_NULL)
    private String code;
    @JsonInclude(Include.NON_NULL)
    private String message;
    @JsonInclude(Include.NON_NULL)
    private List<?> rows;
    private boolean success;
    @JsonInclude(Include.NON_NULL)
    private Long total;
    @JsonInclude(Include.NON_NULL)
    private String sessionId;
    @JsonInclude(Include.NON_NULL)
    private SysUserEntity user;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public ResponseData() {
        this.success = true;
    }

    public ResponseData(boolean success) {
        this.success = true;
        this.setSuccess(success);
    }
    public ResponseData(boolean success,String message) {
        this.success = true;
        this.setSuccess(success);
        this.setMessage(message);
    }

    public ResponseData(List<?> list) {
        this(true);
        this.setRows(list);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public List<?> getRows() {
        return this.rows;
    }

    public Long getTotal() {
        return this.total;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
        if (rows instanceof Page) {
            this.setTotal(((Page)rows).getTotal());
        } else {
            this.setTotal((long)rows.size());
        }

    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public SysUserEntity getUser() {
        return user;
    }

    public void setUser(SysUserEntity user) {
        this.user = user;
    }
}

