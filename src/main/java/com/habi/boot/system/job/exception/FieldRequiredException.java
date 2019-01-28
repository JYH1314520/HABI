package com.habi.boot.system.job.exception;

import com.habi.boot.system.base.exception.BaseException;

public class FieldRequiredException extends BaseException {
    public FieldRequiredException(String descriptionKey, Object[] parameters) {
        this((String)null, descriptionKey, parameters);
    }

    protected FieldRequiredException(String code, String descriptionKey, Object[] parameters) {
        super(code, descriptionKey, parameters);
    }
}
