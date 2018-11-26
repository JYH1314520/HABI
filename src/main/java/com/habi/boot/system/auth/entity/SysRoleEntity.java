package com.habi.boot.system.auth.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.habi.boot.system.base.BaseEntity;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@TableName("sys_role")
public class SysRoleEntity extends BaseEntity {
    @Id
    @TableId
    private Long roleId;

    private String roleCode ;

    private String roleName ;

    private String roleDescription;

    private Date startActiveDate;

    private Date endActiveDate;

    private String enableFlag;

    @Transient
    private List<SysFunctionEntity> SysFunction;


    public List<SysFunctionEntity> getSysFunction() {
        return SysFunction;
    }

    public void setSysFunction(List<SysFunctionEntity> sysFunction) {
        SysFunction = sysFunction;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public Date getStartActiveDate() {
        return startActiveDate;
    }

    public void setStartActiveDate(Date startActiveDate) {
        this.startActiveDate = startActiveDate;
    }

    public Date getEndActiveDate() {
        return endActiveDate;
    }

    public void setEndActiveDate(Date endActiveDate) {
        this.endActiveDate = endActiveDate;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
