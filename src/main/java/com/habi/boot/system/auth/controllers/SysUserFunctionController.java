package com.habi.boot.system.auth.controllers;


import com.habi.boot.system.auth.entity.SysFunctionEntity;
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
import java.util.ArrayList;
import java.util.Iterator;
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
    public ResponseData select(HttpServletRequest request,  @RequestParam("userName") String userName) {
        SysUserEntity  sysUserEntity = iSysUserService.selectByUserName(userName);
        if(sysUserEntity == null){
            return new ResponseData();
        }
        if ( "Y".equals(sysUserEntity.getUserpermissionflag())){
            return new ResponseData(iSysUserFunctionService.findByUserName(userName));
        }else{
            List<String>  Syscodes = sysUserEntity.getRoleCode();
            List<SysFunctionEntity> SysFunctionEntityAll = new ArrayList<SysFunctionEntity>();

            List<SysRoleFunctionEntity>  sysRoleFunctionEntityList = iSysRoleFunctionService.findByRoleCode(Syscodes) ;
            Iterator<SysRoleFunctionEntity> SysRoleFunctionEntitys = sysRoleFunctionEntityList.iterator();
            while (SysRoleFunctionEntitys.hasNext()) {
                    SysRoleFunctionEntity sysRoleFunctionEntity = SysRoleFunctionEntitys.next();
                    SysFunctionEntity sysFunctionEntity = sysRoleFunctionEntity.getSysFunction();
                    SysFunctionEntityAll.add(sysFunctionEntity);
            }
            if(SysFunctionEntityAll == null){
                return new ResponseData();
            } else{
               return new ResponseData(SysFunctionEntityAll);
            }
        }

    }
}
