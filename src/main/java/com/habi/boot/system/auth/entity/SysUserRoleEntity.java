package com.habi.boot.system.auth.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.habi.boot.system.base.BaseEntity;

import javax.persistence.Id;
import javax.persistence.Transient;

@TableName("sys_user_role")
public class SysUserRoleEntity extends BaseEntity {
    @Id
    @TableId
    private Long surId;

    private Long userId;

    private Long roleId;

    @Transient
    private String userName;

    public SysUserRoleEntity() {
    }

    public Long getSurId() {
        return surId;
    }

    public void setSurId(Long surId) {
        this.surId = surId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
