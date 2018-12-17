package com.habi.boot.system.base;


import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.base.cache.impl.SysUserCache;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {
    @Autowired
    private SysUserCache sysUserCache;

    protected IRequest createRequestContext(HttpServletRequest request) {
        String  userName = (String) SecurityUtils.getSubject().getPrincipal();
        SysUserEntity sysUserEntity = null;
        if(userName != null) {
            sysUserCache.setName("sys_user");
            sysUserEntity = sysUserCache.getValue(userName);
        }
        return RequestHelper.createServiceRequest(request,sysUserEntity);
    }
}
