package com.habi.boot.system.base.mybatis;

import com.habi.boot.system.base.mybatis.insert.MyInsertMapper;
import com.habi.boot.system.base.mybatis.insert.MyInsertSelectiveMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;


@RegisterMapper
public interface MyBaseInsertMapper<T> extends MyInsertMapper<T>, MyInsertSelectiveMapper<T> {
}
