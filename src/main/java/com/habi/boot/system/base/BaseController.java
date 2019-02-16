package com.habi.boot.system.base;


import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.base.cache.impl.SysUserCache;
import org.apache.shiro.SecurityUtils;
import org.hibernate.annotations.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Locale;

@RestController
public class BaseController {
    protected static final String DEFAULT_PAGE = "1";
    protected static final String DEFAULT_PAGE_SIZE = "10";

    @Autowired
    private SysUserCache sysUserCache;
    @Source
    private Validator evalidator;

    protected IRequest createRequestContext(HttpServletRequest request) {
        String  userName = (String) SecurityUtils.getSubject().getPrincipal();
        SysUserEntity sysUserEntity = null;
        if(userName != null) {
            sysUserCache.setName("sys_user");
            sysUserEntity = sysUserCache.getValue(userName);
        }
        return RequestHelper.createServiceRequest(request,sysUserEntity);
    }

    protected Validator getValidator() {
        return this.evalidator;
    }


    protected String getErrorMessage(Errors errors, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        String errorMessage = null;

        ObjectError error;
        for(Iterator var5 = errors.getAllErrors().iterator(); var5.hasNext(); errorMessage = error.getCode()) {
            error = (ObjectError)var5.next();
            if (error.getDefaultMessage() != null) {
                errorMessage = "参数集不正确！";
                break;
            }
        }

        return errorMessage;
    }
}
