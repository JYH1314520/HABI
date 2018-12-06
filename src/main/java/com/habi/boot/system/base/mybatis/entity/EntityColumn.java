package com.habi.boot.system.base.mybatis.entity;

public class EntityColumn extends tk.mybatis.mapper.entity.EntityColumn{
    private String sequenceName;

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }
}
