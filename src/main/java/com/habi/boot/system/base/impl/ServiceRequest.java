package com.habi.boot.system.base.impl;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.habi.boot.system.base.IRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ServiceRequest implements IRequest {
    private static final String ATTR_USER_ID = "_userId";
    private static final String ATTR_ROLE_ID = "_roleId";
    private static final String ATTR_COMPANY_ID = "_companyId";
    private static final String ATTR_LOCALE = "_locale";
    private static final long serialVersionUID = 3699668645012922404L;
    private Long userId = -1L;
    private Long roleId = -1L;
    private Long[] roleIds = new Long[0];
    private Long companyId = -1L;
    private String locale = Locale.getDefault().toString();
    private String employeeCode;
    private String userName;
    @JsonIgnore
    private Map<String, Object> attributeMap = new HashMap();

    public ServiceRequest() {
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        this.setAttribute("_userId", userId);
    }

    public String getEmployeeCode() {
        return this.employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
        this.setAttribute("_locale", locale);
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public Long[] getAllRoleId() {
        return this.roleIds;
    }

    public void setAllRoleId(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
        this.setAttribute("_roleId", roleId);
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
        this.setAttribute("_companyId", companyId);
    }

    @JsonAnyGetter
    public <T> T getAttribute(String name) {
        return (T) this.attributeMap.get(name);
    }

    @JsonAnySetter
    public void setAttribute(String name, Object value) {
        this.attributeMap.put(name, value);
    }

    @JsonIgnore
    public Map<String, Object> getAttributeMap() {
        return this.attributeMap;
    }

    @JsonIgnore
    public Set<String> getAttributeNames() {
        return this.attributeMap.keySet();
    }
}


