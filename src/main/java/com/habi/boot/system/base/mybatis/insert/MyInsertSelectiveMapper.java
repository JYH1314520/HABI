package com.habi.boot.system.base.mybatis.insert;

import com.habi.boot.system.base.mybatis.provider.MyBaseInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface MyInsertSelectiveMapper<T> {
    @InsertProvider(
            type = MyBaseInsertProvider.class,
            method = "dynamicSQL"
    )
    int insertSelective(T var1);
}
