package com.habi.boot.system.base;

import com.habi.boot.system.base.impl.ServiceRequest;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {
    protected IRequest createRequestContext(HttpServletRequest request) {
        return new ServiceRequest();
    }
}
