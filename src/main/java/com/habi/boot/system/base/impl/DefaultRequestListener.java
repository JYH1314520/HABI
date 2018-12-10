package com.habi.boot.system.base.impl;

import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.IRequestListener;

import javax.servlet.http.HttpServletRequest;

public class DefaultRequestListener implements IRequestListener {
    public DefaultRequestListener() {
    }

    public IRequest newInstance() {
        return new ServiceRequest();
    }

}