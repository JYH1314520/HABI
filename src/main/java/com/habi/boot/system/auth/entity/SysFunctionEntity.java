package com.habi.boot.system.auth.entity;


import com.habi.boot.system.base.BaseEntity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_function")
public class SysFunctionEntity extends BaseEntity {
    @Id
    private Long funcitonId;

    private String functionCode;

    private String functionType;

    private String functionDescription;

    private String functionIcon;

    private Long functionSequence;

    private String functionName;

    private String moduleCode;


    private Long parentFunctionId;

    private String resourceName;

    private Long resourceId;

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public Long getFuncitonId() {
        return funcitonId;
    }

    public void setFuncitonId(Long funcitonId) {
        this.funcitonId = funcitonId;
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
