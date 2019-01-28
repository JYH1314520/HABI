package com.habi.boot.system.script.service;

import java.io.Reader;
import java.util.Map;

public interface IScriptService {
    Object execute(String var1, Reader var2, Map<String, Object> var3) throws Exception;

    Object execute(String var1, String var2, Map<String, Object> var3) throws Exception;
}