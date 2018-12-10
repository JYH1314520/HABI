package com.habi.boot.system.base;

import javax.servlet.http.HttpServletRequest;


public interface IRequestListener {
    IRequest newInstance();
}