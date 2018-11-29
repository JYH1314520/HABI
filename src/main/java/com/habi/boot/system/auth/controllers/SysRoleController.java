package com.habi.boot.system.auth.controllers;

import com.habi.boot.system.auth.entity.SysRoleEntity;
import com.habi.boot.system.auth.mapper.SysRoleMapper;
import com.habi.boot.system.auth.service.ISysRoleService;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/sys/role")
@RestController
public class SysRoleController extends BaseController {
   @Autowired
   private ISysRoleService sysRoleService;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @RequestMapping("/insert")
    public ResponseData insert(HttpServletRequest request){
        IRequest requestContext = this.createRequestContext(request);
         SysRoleEntity sysRoleEntity =  new SysRoleEntity();
        sysRoleEntity.setRoleCode("223333");
        sysRoleEntity.setRoleId(Integer.valueOf(2333333).longValue());
        sysRoleEntity.setRoleName("233333");
        sysRoleService.insert(requestContext,sysRoleEntity );
        return  new ResponseData() ;
    };
}
