package com.habi.boot.system.manager;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/11.
 * 自定义sessionId获取
 */
public class MySessionManager extends DefaultWebSessionManager {
    private static final Logger log = LoggerFactory.getLogger(DefaultWebSessionManager.class);

    private static final String AUTHORIZATION = "Authorization";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";



    public MySessionManager() {
        super();
    }

//    @Override
//    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
//        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
//        //如果请求头中有 Authorization 则其值为sessionId
//        if (!StringUtils.isEmpty(id)) {
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
//            return id;
//        } else {
//            //否则按默认规则从cookie取sessionId
//            return super.getSessionId(request, response);
//        }
//    }
            @Override
            protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
                return this.getReferencedSessionId(request, response);
            }


    /**
     * 获取sessionId从请求中
     *
     * @param request
     * @param response
     * @return
     */
    private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response) {
        String id = this.getSessionIdCookieValue(request, response);
        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "cookie");
        } else {
            id = this.getUriPathSegmentParamValue(request, "JSESSIONID");
            if (id == null) {
                // 获取请求头中的session
                id = WebUtils.toHttp(request).getHeader(this.AUTHORIZATION);
                if (id == null) {
                    String name = this.getSessionIdName();
                    id = request.getParameter(name);
                    if (id == null) {
                        id = request.getParameter(name.toLowerCase());
                    }
                }
            }
            if (id != null) {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            }
        }

        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }

        return id;
    }

    // copy super
    private String getSessionIdCookieValue(ServletRequest request, ServletResponse response) {
        if (!this.isSessionIdCookieEnabled()) {
            log.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        } else if (!(request instanceof HttpServletRequest)) {
            log.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        } else {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            return this.getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
        }
    }

    // copy super
    private String getUriPathSegmentParamValue(ServletRequest servletRequest, String paramName) {
        if (!(servletRequest instanceof HttpServletRequest)) {
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String uri = request.getRequestURI();
            if (uri == null) {
                return null;
            } else {
                int queryStartIndex = uri.indexOf(63);
                if (queryStartIndex >= 0) {
                    uri = uri.substring(0, queryStartIndex);
                }

                int index = uri.indexOf(59);
                if (index < 0) {
                    return null;
                } else {
                    String TOKEN = paramName + "=";
                    uri = uri.substring(index + 1);
                    index = uri.lastIndexOf(TOKEN);
                    if (index < 0) {
                        return null;
                    } else {
                        uri = uri.substring(index + TOKEN.length());
                        index = uri.indexOf(59);
                        if (index >= 0) {
                            uri = uri.substring(0, index);
                        }

                        return uri;
                    }
                }
            }
        }
    }

    // copy super
    private String getSessionIdName() {
        String name = this.getSessionIdCookie() != null ? this.getSessionIdCookie().getName() : null;
        if (name == null) {
            name = "JSESSIONID";
        }

        return name;
    }

}
