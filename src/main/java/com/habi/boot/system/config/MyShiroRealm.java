package com.habi.boot.system.config;



import com.habi.boot.system.auth.entity.SysFunctionEntity;
import com.habi.boot.system.auth.entity.SysRoleFunctionEntity;
import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.auth.entity.SysUserFunctionEntity;
import com.habi.boot.system.auth.service.ISysRoleFunctionService;
import com.habi.boot.system.auth.service.ISysUserFunctionService;
import com.habi.boot.system.auth.service.ISysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Administrator on 2017/12/11.
 * 自定义权限匹配和账号密码匹配
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleFunctionService sysRoleFunctionService;

    @Autowired
    private ISysUserFunctionService sysUserFunctionService;
    /**
     * 提供用户信息返回权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 根据用户名查询当前用户拥有的角色
        SysUserEntity sysUserEntity = sysUserService.selectByUserName(username);
        List<String> roleCodes = sysUserEntity.getRoleCode();
        Set<String> roleNames = new HashSet<String>();
        for (String role : roleCodes) {
            roleNames.add(role);
        }
        // 将角色名称提供给info
        authorizationInfo.setRoles(roleNames);

        Set<String> permissionNames = new HashSet<String>();
        // 根据用户名查询当前用户权限
        if(sysUserEntity.getUserpermissionflag()) {
         List<SysUserFunctionEntity>  sysUserFunctionEntityList =  sysUserFunctionService.findByUserName(username);
         for (SysUserFunctionEntity sysUserFunctionEntity :sysUserFunctionEntityList){
             SysFunctionEntity sysFunctionEntity = sysUserFunctionEntity.getSysFunction();
             permissionNames.add(sysFunctionEntity.getResourceName());
         }
        }else{
            for (String role : roleCodes) {
                List<SysRoleFunctionEntity>  sysRoleFunctionEntityList =  sysRoleFunctionService.findByRoleCode(role);
                for (SysRoleFunctionEntity sysRoleFunctionEntity :sysRoleFunctionEntityList){
                    SysFunctionEntity sysFunctionEntity = sysRoleFunctionEntity.getSysFunction();
                    permissionNames.add(sysFunctionEntity.getResourceName());
                }
            }

        }


        // 将权限名称提供给info
        authorizationInfo.setStringPermissions(permissionNames);

        return authorizationInfo;
    }


    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
//        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        String password = new String((char[])token.getCredentials());
//        System.out.println(token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SysUserEntity sysUserEntity = sysUserService.selectByUserName(username);
        if (sysUserEntity == null) {
            return null;
        }
        if (sysUserEntity.getStatus() == "") { //账户冻结
            throw new LockedAccountException();
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                sysUserEntity.getUserName(), //用户名
                sysUserEntity.getPassword(), //密码
                //"admin",
                getName()  //realm name
        );
        if (sysUserEntity.getSalt() != null){
            authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(sysUserEntity.getCredentialsSalt()));
        }
        return authenticationInfo;
    }

}

