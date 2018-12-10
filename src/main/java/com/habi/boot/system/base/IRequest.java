package com.habi.boot.system.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRequest extends Serializable {
    String FIELD_USER_ID = "userId";
    String FIELD_LOCALE = "locale";
    String FIELD_ROLE_ID = "roleId";
    String FIELD_LOGIN_ID = "loginId";
    String FIELD_ALL_ROLE_ID = "roleIds";
    String EMP_CODE = "employeeCode";
    String MDC_PREFIX = "MDC.";

    void setUserName(String var1);

    String getUserName();

    Long getUserId();

    <T> T getAttribute(String var1);

    Map<String, Object> getAttributeMap();

    Set<String> getAttributeNames();

    Long getCompanyId();

    String getLocale();

    Long getRoleId();

    Long[] getAllRoleId();

    void setAllRoleId(Long[] var1);

    void setUserId(Long var1);

    String getEmployeeCode();

    void setEmployeeCode(String var1);

    void setAttribute(String var1, Object var2);

    void setCompanyId(Long var1);

    void setLocale(String var1);

    void setRoleId(Long var1);

     List<String> getRoleCode();

    void setRoleCode(List<String> roleCode);
}
