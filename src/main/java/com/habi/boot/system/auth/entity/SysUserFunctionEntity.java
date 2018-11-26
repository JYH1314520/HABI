package com.habi.boot.system.auth.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.habi.boot.system.base.BaseEntity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "sys_user_function")
public class SysUserFunctionEntity extends BaseEntity {
    @Id
    @TableId
    private Long userFuncitonId;

    private Long functionId;

    private Long userId;

    private Boolean mainFlag ;

    private Boolean enableFlag;


    @Transient
    private SysFunctionEntity SysFunction;


    public Long getUserFuncitonId() {
        return userFuncitonId;
    }

    public void setUserFuncitonId(Long userFuncitonId) {
        this.userFuncitonId = userFuncitonId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
