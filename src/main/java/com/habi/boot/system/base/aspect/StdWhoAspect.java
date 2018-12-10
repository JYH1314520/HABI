package com.habi.boot.system.base.aspect;


import com.habi.boot.system.base.BaseEntity;
import com.habi.boot.system.base.EntityClassInfo;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.RequestHelper;
import com.habi.boot.system.base.annotation.StdWho;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.FieldHelper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Aspect
@Order(4)
@Component
public class StdWhoAspect {
    private Logger logger = LoggerFactory.getLogger(StdWhoAspect.class);
    @Pointcut("execution(* *..*ServiceImpl.*(..))")
    public void servicePointcut(){}

    void initMDC(IRequest request) {
        if (request != null) {
            request.getAttributeMap().forEach((key, v) -> {
                if (key.startsWith("MDC.")) {
                    String mdcProperty = key.substring("MDC.".length());
                    MDC.put(mdcProperty, (String)v);
                }

            });
        }

    }

    @Before("servicePointcut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?>[] types = method.getParameterTypes();
        int idx = -1;

        for(int i = 0; i < types.length; ++i) {
            if (IRequest.class.isAssignableFrom(types[i])) {
                idx = i;
                break;
            }
        }
        if (idx != -1) {
            IRequest requestContext = (IRequest)args[idx];
            if (requestContext == null) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("{}'s IRequest argument is null.", method);
                }

            } else {
                RequestHelper.setCurrentRequest(requestContext);
                this.initMDC(requestContext);
                Parameter[] parameters = method.getParameters();

                for(int i = 0; i < types.length; ++i) {
                    StdWho who = (StdWho)parameters[i].getAnnotation(StdWho.class);
                    if (who != null && this.logger.isTraceEnabled()) {
                        this.logger.trace("enable StdWho for argument {}, type: {}", i, types[i].getName());
                    }



                    if (args[i] instanceof BaseEntity) {
                        BaseEntity baseDTO = (BaseEntity)args[i];
                        this.autoAssignStdProperty(this.logger, requestContext, baseDTO, who != null);
                    } else if (args[i] instanceof Collection) {
                        Iterator var10 = ((Collection)args[i]).iterator();

                        while(var10.hasNext()) {
                            Object o = var10.next();

                            if (o instanceof BaseEntity) {
                                this.autoAssignStdProperty(this.logger, requestContext, (BaseEntity)o, who != null);
                            }
                        }
                    }
                }

            }
        }
    }

    private void autoAssignStdProperty(Logger logger, IRequest request, BaseEntity dto, boolean stdWhoSupport) {
        EntityField[] var5;
        List<EntityField> fieldList = new ArrayList();
        int var6;
        int var7;
        EntityField field;
        if (stdWhoSupport) {
            fieldList = FieldHelper.getFields(dto.getClass());
            for(EntityField entityField : fieldList) {
                field = entityField;

                try {
                    if (PropertyUtils.getProperty(dto, field.getName()) == null) {
                        dto.setCreatedBy(request.getUserId());
                        dto.setCreationDate(new Date());
                        break;
                    }
                } catch (Exception var13) {
                    ;
                }
            }

            dto.setLastUpdateDate(new Date());
            dto.setLastUpdatedBy(request.getUserId());
            dto.setLastUpdateLogin(request.getUserId());
        }

//        var5 = EntityClassInfo.getChildrenFields(dto.getClass());
//        var6 = var5.length;
//
//        for(var7 = 0; var7 < var6; ++var7) {
//            field = var5[var7];
//            if (!this.checkChildrenType(field.getJavaType())) {
//                if (logger.isWarnEnabled()) {
//                    logger.warn("property '{}' is annotated by @Children, incorrect usage.", field.getName());
//                }
//
//                return;
//            }
//
//            try {
//                Object p = PropertyUtils.getProperty(dto, field.getName());
//                if (p instanceof BaseEntity) {
//                    this.autoAssignStdProperty(logger, request, (BaseEntity)p, stdWhoSupport);
//                } else if (p instanceof Collection) {
//                    Iterator var10 = ((Collection)p).iterator();
//
//                    while(var10.hasNext()) {
//                        Object o = var10.next();
//                        if (o instanceof BaseEntity) {
//                            this.autoAssignStdProperty(logger, request, (BaseEntity)o, stdWhoSupport);
//                        }
//                    }
//                }
//            } catch (Exception var12) {
//                if (logger.isErrorEnabled()) {
//                    logger.error(var12.getMessage(), var12);
//                }
//            }
//        }

    }

    protected boolean checkChildrenType(Class<?> type) {
        if (BaseEntity.class.isAssignableFrom(type)) {
            return true;
        } else {
            return Collection.class.isAssignableFrom(type);
        }
    }

//    @Around("servicePointcut()")
//    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        try {
//            Object obj = proceedingJoinPoint.proceed();
//            return obj;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    @AfterReturning(returning = "ret", pointcut = "servicePointcut()")
//    public void doAfterReturning(Object ret) {
//
//    }

}
