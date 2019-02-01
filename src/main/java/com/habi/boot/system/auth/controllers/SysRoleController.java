package com.habi.boot.system.auth.controllers;

import com.habi.boot.system.auth.entity.SysRoleEntity;
import com.habi.boot.system.auth.mapper.SysRoleMapper;
import com.habi.boot.system.auth.service.ISysRoleService;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.ResponseData;
import com.habi.boot.system.logs.annotation.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/api/sys/role")
@RestController
public class SysRoleController extends BaseController {
   @Autowired
   private ISysRoleService sysRoleService;


    @RequestMapping("/select")
    @SysLog("角色查询")
    public ResponseData select(HttpServletRequest request){
        return  new ResponseData(sysRoleService.findAll()) ;
    };



    @RequestMapping({"/submit"})
    @ResponseBody
    public ResponseData update(@RequestBody List<SysRoleEntity> dto, BindingResult result, HttpServletRequest request) {
        this.getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage("数据格式错误！");
            return responseData;
        } else {
            IRequest requestCtx = this.createRequestContext(request);
            return new ResponseData(this.sysRoleService.batchUpdate(requestCtx, dto));
        }
    }

    @RequestMapping({"/remove"})
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<SysRoleEntity> dto) {
        this.sysRoleService.batchDelete(dto);
        return new ResponseData();
    }
}
