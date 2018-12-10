package com.habi.boot.system.base;

import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.base.impl.DefaultRequestListener;
import org.apache.shiro.SecurityUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.support.RequestContextUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;

public final class RequestHelper {
    private static ThreadLocal<IRequest> localRequestContext = new ThreadLocal();
    private static IRequestListener requestListener = new DefaultRequestListener();


    public RequestHelper() {
    }

    public IRequestListener getRequestListener() {
        return requestListener;
    }

    public void setRequestListener(IRequestListener requestListener) {
        requestListener = requestListener;
    }

    public static IRequest newEmptyRequest() {
        return requestListener.newInstance();
    }

    public static void setCurrentRequest(IRequest request) {
        localRequestContext.set(request);
    }

    public static void clearCurrentRequest() {
        localRequestContext.remove();
    }

    public static IRequest getCurrentRequest() {
        return getCurrentRequest(false);
    }

    public static IRequest getCurrentRequest(boolean returnEmptyForNull) {
        IRequest request = (IRequest)localRequestContext.get();
        return request == null && returnEmptyForNull ? newEmptyRequest() : request;
    }

    public static IRequest createServiceRequest(HttpServletRequest httpServletRequest,SysUserEntity sysUserEntity) {
        IRequest requestContext = requestListener.newInstance();
        HttpSession session = httpServletRequest.getSession(false);
        if (sysUserEntity != null){
            if (sysUserEntity.getUserId() != null) {
                requestContext.setUserId(sysUserEntity.getUserId());
            }


            if (sysUserEntity.getUserName() != null) {
                requestContext.setUserName(sysUserEntity.getUserName());
            }

            if (sysUserEntity.getCompanyId() != null) {
                requestContext.setCompanyId(sysUserEntity.getCompanyId());
            }

            if (sysUserEntity.getRoleCode() != null) {
                requestContext.setRoleCode(sysUserEntity.getRoleCode());
            }
            if (sysUserEntity.getEmployeeCode()!= null) {
                requestContext.setEmployeeCode(sysUserEntity.getEmployeeCode());
            }

            Locale locale = RequestContextUtils.getLocale(httpServletRequest);
            if (locale != null) {
                requestContext.setLocale(locale.toString());
            }
            if(requestContext.getUserId() == null){
                String username =  session.getAttribute("originalPrincipalName").toString();

            }
        }
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        if (mdcMap != null) {
            mdcMap.forEach((k, v) -> {
                requestContext.setAttribute("MDC.".concat(k), v);
            });
        }


        return requestContext;
    }

}
