package com.habi.boot.system.auth.controllers;

import com.habi.boot.system.auth.entity.SysUserEntity;
import com.habi.boot.system.auth.service.ISysUserService;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.ResponseData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import static com.alibaba.druid.util.Utils.md5;

@RestController
public class LoginController extends BaseController {
    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping("login")
    public ResponseData login(HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("password") String password) {
       // UsernamePasswordToken token = new UsernamePasswordToken(userName, password,request.getRemoteAddr());
        String md5password = md5(password);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, md5password);
       // 后面参数含义：密码，盐，加密次数(两次代表：md5(md5())安全性更高)
        Md5Hash md5Hash = new Md5Hash("123456","admin8d78869f470951332959580424d4bf4f",2);
        String  passwordh = md5Hash.toHex();
      //  System.out.println(passwordh);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            // 捕获密码错误异常
            ResponseData responseData =   new ResponseData();
            responseData.setSuccess(false);
            responseData.setCode("200");
            responseData.setMessage("password error!");
            return responseData;
        } catch (UnknownAccountException uae) {
            // 捕获未知用户名异常
            ResponseData responseData =   new ResponseData();
            responseData.setSuccess(false);
            responseData.setCode("200");
            responseData.setMessage("username error!");
            return responseData;
        } catch (ExcessiveAttemptsException eae) {
            // 捕获错误登录过多的异常
            ResponseData responseData =   new ResponseData();
            responseData.setSuccess(false);
            responseData.setCode("200");
            responseData.setMessage("times error!");
            return responseData;
        }
        SysUserEntity sysUserEntity = sysUserService.selectByUserName(userName);
        subject.getSession().setAttribute("user", sysUserEntity);
        String sessionId = (String) subject.getSession().getId();
        ResponseData responseData =   new ResponseData();
        responseData.setSuccess(true);
        responseData.setCode("200");
        responseData.setSessionId(sessionId);
        return responseData;
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    public ResponseData unauth(HttpServletRequest request) {
        ResponseData responseData =   new ResponseData();
        responseData.setSuccess(false);
        responseData.setCode("100000");
        responseData.setMessage("重新登陆！");
        return responseData;
    }

    /**
     * 注销
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public ResponseData logout(HttpServletRequest request) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        ResponseData responseData =   new ResponseData();
        responseData.setSuccess(true);
        responseData.setCode("200");
        responseData.setMessage("注销成功！");
        return responseData;
    }


}
