package com.habi.boot.system.auth.entity;


import com.habi.boot.system.base.BaseEntity;
import com.habi.boot.system.base.annotation.TableGeneratedValue;
import com.habi.boot.system.config.DatabaseTypeConfig;

import javax.persistence.*;
import java.util.List;

@Table(name = "sys_role_function")
@Entity
public class SysRoleFunctionEntity extends BaseEntity {
    @Id
    @TableGeneratedValue(databaseType = DatabaseTypeConfig.databaseType)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long roleFuncitonId;

    private Long functionId;

    private Long roleId;

    private Boolean mainFlag ;

    private Boolean enableFlag;


    @Transient
    private SysFunctionEntity SysFunction;




    public SysRoleFunctionEntity() {
    }

    public Long getRoleFuncitonId() {
        return roleFuncitonId;
    }

    public void setRoleFuncitonId(Long roleFuncitonId) {
        this.roleFuncitonId = roleFuncitonId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Boolean getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(Boolean mainFlag) {
        this.mainFlag = mainFlag;
    }

    public Boolean getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public SysFunctionEntity getSysFunction() {
        return SysFunction;
    }

    public void setSysFunction(SysFunctionEntity sysFunction) {
        SysFunction = sysFunction;
    }
}
