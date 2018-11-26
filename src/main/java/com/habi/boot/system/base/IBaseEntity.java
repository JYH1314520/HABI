package com.habi.boot.system.base;

public interface IBaseEntity {
    String __ID = "__id";
    String __STATUS = "__status";
    String __TLS = "__tls";
    String SORTNAME = "sortname";
    String SORTORDER = "sortorder";
    String _TOKEN = "_token";

    Object getAttribute(String var1);

    void setAttribute(String var1, Object var2);
}
