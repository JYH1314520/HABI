package com.habi.boot.system.job.dto;

import com.habi.boot.system.base.BaseEntity;

public class JobData extends BaseEntity {
    private static final long serialVersionUID = 200612977390984121L;
    private String name;
    private String value;

    public JobData() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

