package com.habi.boot.system.logs.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.habi.boot.system.base.utils.ToolUtil;
import com.habi.boot.system.logs.entity.SysLog;
import com.habi.boot.system.logs.service.ISysLogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * todo:
 */
@Aspect
@Order(5)
@Component
public class SysLogAspect {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    private ISysLogService sysLogService;

    private SysLog sysLog = null;

    @Pointcut("@annotation(com.habi.boot.system.logs.annotation.SysLog)")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = (HttpSession) attributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        sysLog = new SysLog();
        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        sysLog.setHttpMethod(request.getMethod());
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object o = args[i];
            if(o instanceof ServletRequest || (o instanceof ServletResponse) || o instanceof MultipartFile){
                args[i] = o.toString();
            }
        }
        //获取所有的请求参数
        Enumeration<String> paraNames=request.getParameterNames();
        JSONObject paraOject   = new JSONObject();
        for(Enumeration<String> e=paraNames;e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            String thisValue=request.getParameter(thisName);
            paraOject.put(thisName,thisValue);
            System.out.println("param的key:"+thisName+"--------------param的value:"+thisValue);
        }
        String requestBody = JSONObject.toJSONString(paraOject);
        sysLog.setRequestBody(requestBody.length()>3000?requestBody.substring(0, 2999):requestBody);
       //获取所有的头部参数
        Enumeration<String> headerNames=request.getHeaderNames();
        JSONObject headerObject  = new JSONObject();
        for(Enumeration<String> e=headerNames;e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            String thisValue=request.getHeader(thisName);
            headerObject.put(thisName,thisValue);
            System.out.println("header的key:"+thisName+"--------------header的value:"+thisValue);
        }
        String requestHeader = JSONObject.toJSONString(headerObject);
        sysLog.setRequestHeader(requestHeader.length()>3000?requestHeader.substring(0, 2999):requestHeader);

        String str = JSONObject.toJSONString(request.getParameterMap());
        sysLog.setParams(str.length()>3000?requestHeader.substring(0, 2999):str);
        String ip = ToolUtil.getClientIp(request);
        if("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)){
            ip = "127.0.0.1";
        }
        sysLog.setRemoteAddr(ip);
        sysLog.setRequestUri(request.getRequestURL().toString());
        if(session != null){
            sysLog.setSessionId(session.getId());
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.habi.boot.system.logs.annotation.SysLog mylog = method.getAnnotation(com.habi.boot.system.logs.annotation.SysLog.class);
        if(mylog != null){
            //注解上的描述
            sysLog.setTitle(mylog.value());
        }

        Map<String,String> browserMap = ToolUtil.getOsAndBrowserInfo(request);
        sysLog.setBrowser(browserMap.get("os")+"-"+browserMap.get("browser"));

        if(!"127.0.0.1".equals(ip)){
            Map<String,String> map = (Map<String,String>)session.getAttribute("addressIp");
            if(map == null){
                map = ToolUtil.getAddressByIP(ToolUtil.getClientIp(request));
                session.setAttribute("addressIp",map);
            }
            sysLog.setArea(map.get("area"));
            sysLog.setProvince(map.get("province"));
            sysLog.setCity(map.get("city"));
            sysLog.setIsp(map.get("isp"));
        }
        sysLog.setType(ToolUtil.isAjax(request)?"Ajax请求":"普通请求");
        String  userName = (String) SecurityUtils.getSubject().getPrincipal();
        if(userName != null) {
            sysLog.setUsername(StringUtils.isNotBlank(userName) ? userName : "未知");
        }else{
            sysLog.setUsername("未登录用户");
        }
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object obj = proceedingJoinPoint.proceed();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            sysLog.setException(e.getMessage());
            throw e;
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        String  userName = (String) SecurityUtils.getSubject().getPrincipal();
        if(userName != null) {
            sysLog.setUsername(StringUtils.isNotBlank(userName) ? userName : "未知");
        }else{
            sysLog.setUsername("未登录用户");
        }
        String retString = JSONObject.toJSONString(ret);
        sysLog.setResponse(retString.length()>3000?retString.substring(0, 2999):retString);
        sysLog.setUseTime(System.currentTimeMillis() - startTime.get());
        sysLogService.insert(null,sysLog);
    }
}

