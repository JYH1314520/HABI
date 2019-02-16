package com.habi.boot.system.auth.controllers;


import com.habi.boot.system.auth.entity.SysRoleFunctionEntity;
import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.auth.service.ISysRoleFunctionService;
import com.habi.boot.system.auth.service.ISysUserFunctionService;
import com.habi.boot.system.auth.service.ISysUserService;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RequestMapping("/api/sys/user")
@RestController
public class SysUserFunctionController  extends BaseController {
    @Autowired
    private ISysUserFunctionService iSysUserFunctionService;

    @Autowired
    private ISysRoleFunctionService  iSysRoleFunctionService;
    @Autowired
    private ISysUserService    iSysUserService;

    @RequestMapping({"/function"})
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,  @RequestParam("userName") String userName) {
        SysUserEntity  sysUserEntity = iSysUserService.selectByUserName(userName);
        if ( "Y".equals(sysUserEntity.getUserpermissionflag())){
            return new ResponseData(iSysUserFunctionService.findByUserName(userName));
        }else{
            List<String>  Syscodes = sysUserEntity.getRoleCode();
            List<SysRoleFunctionEntity> sysRoleFunctionEntityAll = null;
            for(String syscode : Syscodes){
                List<SysRoleFunctionEntity>  sysRoleFunctionEntityList = iSysRoleFunctionService.findByRoleCode(syscode) ;
                for (SysRoleFunctionEntity sysRoleFunctionEntity : sysRoleFunctionEntityList){
                    sysRoleFunctionEntityAll.add(sysRoleFunctionEntity);
                }
            }
            return new ResponseData(sysRoleFunctionEntityAll);
        }

    }
}
