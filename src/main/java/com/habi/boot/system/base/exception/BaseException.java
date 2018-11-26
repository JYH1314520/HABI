package com.habi.boot.system.base.exception;

public abstract class BaseException extends Exception {
    private static final long serialVersionUID = 1L;
    private String code;
    private String descriptionKey;
    private Object[] parameters;

    protected BaseException(String code, String descriptionKey, Object[] parameters) {
        super(descriptionKey);
        this.code = code;
        this.descriptionKey = descriptionKey;
        this.parameters = parameters;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescriptionKey() {
        return this.descriptionKey;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}