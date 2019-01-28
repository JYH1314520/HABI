package com.habi.boot.system.job.exception;

import com.habi.boot.system.base.exception.BaseException;

public class JobException extends BaseException {
    public static final String JOB_EXCEPTION = "JOB_EXCEPTION";
    public static final String NOT_SUB_CLASS = "job.error.invalid_job_class";
    public static final String NOT_TRIGGER = "job.error.has_no_trigger";
    private static final long serialVersionUID = 2809497996266500743L;

    public JobException(String code, String descriptionKey, Object[] parameters) {
        super(code, descriptionKey, parameters);
    }

    public JobException(String code, String descriptionKey) {
        this(code, descriptionKey, (Object[])null);
    }
}
