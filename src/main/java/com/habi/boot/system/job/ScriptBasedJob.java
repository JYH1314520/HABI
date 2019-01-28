package com.habi.boot.system.job;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import com.habi.boot.system.script.service.IScriptService;
import org.apache.commons.io.IOUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ScriptBasedJob extends AbstractJob {
    private Logger logger = LoggerFactory.getLogger(ScriptBasedJob.class);
    @Autowired
    private IScriptService scriptService;
    private Exception exception = null;

    public ScriptBasedJob() {
    }

    public String initFileFormat(String scriptFile) {
        String sf = scriptFile.trim().replace('\\', '/');
        if (!sf.startsWith("/")) {
            sf = "/" + sf;
        }

        return sf;
    }

    public void safeExecute(JobExecutionContext context) throws Exception {
        Object ret = null;

        try {
            String scriptName = context.getMergedJobDataMap().getString("scriptName");
            String script = context.getMergedJobDataMap().getString("script");
            String scriptFile = context.getMergedJobDataMap().getString("scriptFile");
            if (script != null) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("begin execute script,scriptName:{}  Script:{}", scriptName, script);
                }

                ret = this.scriptService.execute(scriptName, script, (Map)null);
            } else {
                if (scriptFile == null) {
                    throw new Exception("both script and scriptFile is blank");
                }

                InputStream inputStream = ScriptBasedJob.class.getResourceAsStream(this.initFileFormat(scriptFile));
                if (inputStream == null) {
                    throw new Exception("scriptFile is blank");
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copyLarge(inputStream, baos);
                IOUtils.closeQuietly(baos);
                byte[] bytes = baos.toByteArray();
                String scriptContent = new String(bytes, "UTF-8");
                IOUtils.closeQuietly(inputStream);
                ret = this.scriptService.execute(scriptName, scriptContent, (Map)null);
            }
        } catch (Exception var13) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(var13.getMessage(), var13);
            }

            this.exception = var13;
            throw var13;
        } finally {
            if (this.exception != null) {
                this.setExecutionSummary(this.exception.getClass().getName() + ":" + this.exception.getMessage());
            } else if (ret != null) {
                this.setExecutionSummary("execution result:" + ret);
            }

        }

    }

    public boolean isRefireImmediatelyWhenException() {
        return false;
    }
}

