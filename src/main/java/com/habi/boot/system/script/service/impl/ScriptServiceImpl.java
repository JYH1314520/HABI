package com.habi.boot.system.script.service.impl;

import java.io.Reader;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import com.habi.boot.system.script.service.IScriptService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ScriptServiceImpl implements IScriptService {
    @Autowired
    private ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(ScriptServiceImpl.class);

    public ScriptServiceImpl() {
    }

    private ScriptEngine initScriptEngine(Map<String, Object> contextParameter) {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("JavaScript");
        engine.put("applicationContext", this.applicationContext);
        engine.put("out", System.out);
        engine.put("logger", this.logger);
        if (contextParameter != null) {
            contextParameter.forEach((k, v) -> {
                engine.put(k, v);
            });
        }

        return engine;
    }

    public Object execute(String scriptName, Reader reader, Map<String, Object> contextParameter) throws Exception {
        if (null == reader) {
            throw new Exception("reader is blank");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            char[] buff = new char[1024];
            boolean var6 = true;

            int len;
            while((len = IOUtils.read(reader, buff)) != -1 && len != 0) {
                stringBuilder.append(buff, 0, len);
            }

            IOUtils.closeQuietly(reader);
            Object result = this.execute(scriptName, stringBuilder.toString(), contextParameter);
            return result;
        }
    }

    public Object execute(String scriptName, String script, Map<String, Object> contextParameter) throws Exception {
        if (StringUtils.isBlank(script)) {
            throw new Exception("script is blank");
        } else {
            Object result = null;
            long starTime = System.currentTimeMillis();
            ScriptException scriptException = null;
            boolean var15 = false;

            try {
                var15 = true;
                result = this.initScriptEngine(contextParameter).eval(script);
                var15 = false;
            } catch (ScriptException var16) {
                scriptException = var16;
                throw var16;
            } finally {
                if (var15) {
                    long endTime = System.currentTimeMillis();
                    if (this.logger.isDebugEnabled() && null == scriptException) {
                        this.logger.debug("The script {} running time: {} ms", scriptName, endTime - starTime);
                    }

                }
            }

            long endTime = System.currentTimeMillis();
            if (this.logger.isDebugEnabled() && null == scriptException) {
                this.logger.debug("The script {} running time: {} ms", scriptName, endTime - starTime);
            }

            return result;
        }
    }
}

