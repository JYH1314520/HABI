package com.habi.boot.system.auth.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.habi.boot.system.base.BaseEntity;

import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Table(name = "sys_user")
public class SysUserEntity extends BaseEntity {
    @Id
    private Long userId;  //pk

    @NotNull
    @PrimaryKeyJoinColumn
    private String userName;  //用户账号

    private String description;  //用户描述

    private String password;  //密码

    private String userType; //用户类型

    private String email;  //邮箱

    private String phone;  //电话

    private Date startActiveDate;  //有效日期从

    private Date endActiveDate;   //有效日期至

    private Integer status ;   //状态 :  0 正常  1 冻结

    private Long employeeId;  //员工id

    private Long companyId;  //公司id

    private String businessGroup;  //企业集团

    private Boolean userpermissionflag;  // 是否启用用户权限

    private String salt;//盐

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getUserpermissionflag() {
        return userpermissionflag;
    }

    public void setUserpermissionflag(Boolean userpermissionflag) {
        this.userpermissionflag = userpermissionflag;
    }

    public String getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(String businessGroup) {
        this.businessGroup = businessGroup;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String employeeCode;

    @Transient
    private List<String> roleCode;




    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public List<String> getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(List<String> roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * 密码盐.
     *
     * @return
     */
    public String getCredentialsSalt() {
        return this.userName + this.salt;
    }
}
