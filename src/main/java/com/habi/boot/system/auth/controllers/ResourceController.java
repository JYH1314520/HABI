package com.habi.boot.system.auth.controllers;

import org.springframework.stereotype.Controller;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.ResponseData;
import com.habi.boot.system.auth.entity.Resource;
import com.habi.boot.system.auth.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import java.util.List;

    @Controller
    public class ResourceController extends BaseController{

    @Autowired
    private IResourceService service;


    @RequestMapping(value = "/sys/resource/query")
    @ResponseBody
    public ResponseData query(Resource entity, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,entity,page,pageSize));
    }

    @RequestMapping(value = "/sys/resource/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<Resource> entity, BindingResult result, HttpServletRequest request){
        getValidator().validate(entity, result);
        if (result.hasErrors()) {
        ResponseData responseData = new ResponseData(false);
        responseData.setMessage(getErrorMessage(result, request));
        return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, entity));
    }

    @RequestMapping(value = "/sys/resource/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Resource> entity){
        service.batchDelete(entity);
        return new ResponseData();
    }
    }