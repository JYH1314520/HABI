package com.habi.boot.system.base.mybatis;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;

@RegisterMapper
public interface MyBaseMapper<T> extends BaseSelectMapper<T>, MyBaseInsertMapper<T>, BaseUpdateMapper<T>, BaseDeleteMapper<T> {
}

