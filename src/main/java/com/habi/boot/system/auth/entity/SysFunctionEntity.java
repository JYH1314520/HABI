package com.habi.boot.system.auth.entity;


import com.habi.boot.system.base.BaseEntity;
import com.habi.boot.system.base.annotation.TableGeneratedValue;
import com.habi.boot.system.config.DatabaseTypeConfig;

import javax.persistence.*;

@Table(name = "sys_function")
@Entity
public class SysFunctionEntity extends BaseEntity {
    @Id
    @TableGeneratedValue(databaseType = DatabaseTypeConfig.databaseType)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "FUNCTION_ID")
    private Long functionId;
    @Column(name = "FUNCTION_CODE")
    private String functionCode;

    @Column(name = "FUNCTION_TYPE")
    private String functionType;
    @Column(name = "FUNCTION_DESCRIPTION")
    private String functionDescription;
    @Column(name = "FUNCTION_ICON")
    private String functionIcon;
    @Column(name = "FUNCTION_SEQUENCE")
    private Long functionSequence;
    @Column(name = "FUNCTION_NAME")
    private String functionName;
    @Column(name = "MODULE_CODE")
    private String moduleCode;

    @Column(name = "PARENT_FUNCTION_ID")
    private Long parentFunctionId;
    @Column(name = "RESOURCE_NAME")
    private String resourceName;
    @Column(name = "RESOURCE_ID")
    private Long resourceId;
    @Column(name = "LANG")
    private String lang;

    @Transient
    private  Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public Long getFuncitonId() {
        return functionId;
    }

    public void setFuncitonId(Long functionId) {
        this.functionId = functionId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    public void setFunctionDescription(String functionDescription) {
        this.functionDescription = functionDescription;
    }

    public String getFunctionIcon() {
        return functionIcon;
    }

    public void setFunctionIcon(String functionIcon) {
        this.functionIcon = functionIcon;
    }

    public Long getFunctionSequence() {
        return functionSequence;
    }

    public void setFunctionSequence(Long functionSequence) {
        this.functionSequence = functionSequence;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Long getParentFunctionId() {
        return parentFunctionId;
    }

    public void setParentFunctionId(Long parentFunctionId) {
        this.parentFunctionId = parentFunctionId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
