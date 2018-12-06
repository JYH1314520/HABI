package com.habi.boot.system.base.mybatis;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.RowBoundsMapper;

@RegisterMapper
public interface MyMapper<T> extends MyBaseMapper<T>, ExampleMapper<T>, RowBoundsMapper<T>, Marker {
}
