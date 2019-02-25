package com.habi.boot.system.intercepter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TimeOutIntercepter implements HandlerInterceptor {
    //可以随意访问的url
    public String[] allowUrls;

    public void setAllowUrls(String[] allowUrls) {
        this.allowUrls = allowUrls;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {


        String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession(true);
        if(StringUtils.isNoneBlank(requestUrl)){
            for(String url:allowUrls){
                if(requestUrl.contains(url)){
                    return true;
                }
            }
        }
        //session持续时间
        int maxInactiveInterval = session.getMaxInactiveInterval();
        //session创建时间
        long creationTime = session.getCreationTime();
        //session最新链接时间
        long lastAccessedTime = session.getLastAccessedTime();

        System.out.println("-----> maxInactiveInterval: "+maxInactiveInterval);
        System.out.println("-----> creationTime: "+creationTime);
        System.out.println("-----> lastAccessedTime: "+lastAccessedTime);

        //从session获取上次链接时间
        Long operateTime = (Long)session.getAttribute("operateTime");
        System.out.println("-----> operateTime: "+operateTime);

        //如果operateTime是空，说明是第一次链接，对operateTime进行初始化
        if(operateTime ==null){
            session.setAttribute("operateTime",lastAccessedTime);
            return true;
        }else{
            //计算最新链接时间和上次链接时间的差值
            int intervalTime = (int)((lastAccessedTime - operateTime)/1000);
            System.out.println("-----> intervalTime: "+intervalTime);
            //如果超过十秒没有交互的话，就跳转到超时界面
            if(intervalTime>10){
                response.sendRedirect(request.getContextPath()+"/static/timeout.html");
                return true;
            }
            //更新operateTime
            session.setAttribute("operateTime",lastAccessedTime);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
