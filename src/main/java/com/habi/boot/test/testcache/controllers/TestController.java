package com.habi.boot.test.testcache.controllers;

import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.ResponseData;
import com.habi.boot.system.base.cache.impl.JedisUtil;
import com.habi.boot.system.base.cache.impl.StringCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/test/testcache")
public class TestController extends BaseController {
    @Autowired
    private StringCache stringCache;

    @RequestMapping(value = "/test1")
    public ResponseData test(HttpServletRequest request) {
        JedisUtil jedisUtil = new JedisUtil();
        int i = jedisUtil.hmset("habi:test","test1","11233");
        return  new ResponseData() ;
    }

    @RequestMapping(value = "/test2")
    public ResponseData test2(HttpServletRequest request) {
        JedisUtil jedisUtil = new JedisUtil();
        String s = jedisUtil.hmget("habi:test","test1");
        return  new ResponseData() ;
    }



    @RequestMapping(value = "/test3")
    public ResponseData test3(HttpServletRequest request) {
        stringCache.setName("ADC");
        stringCache.setValue("test3","你好啊");
        return  new ResponseData() ;
    }

    @RequestMapping(value = "/test4")
    public ResponseData test4(HttpServletRequest request) {
        stringCache.setName("ADC");
       String s = stringCache.getValue("test3");
        return  new ResponseData() ;
    }

}
