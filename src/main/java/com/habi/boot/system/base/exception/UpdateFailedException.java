package com.habi.boot.system.base.exception;

import com.habi.boot.system.base.BaseEntity;

public class UpdateFailedException extends BaseException {
    public static final String MESSAGE_KEY = "error.record_not_exists_or_version_not_match";

    protected UpdateFailedException(String code, String descriptionKey, Object[] parameters) {
        super(code, descriptionKey, parameters);
    }

    public UpdateFailedException() {
        this("SYS", "error.record_not_exists_or_version_not_match", (Object[])null);
    }

    public UpdateFailedException(BaseEntity record) {
        this("SYS", "error.record_not_exists_or_version_not_match", new Object[]{record.get__id()});
    }
}

