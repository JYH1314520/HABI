package com.habi.boot.system.base;


import java.util.List;

import com.github.pagehelper.Page;
import com.habi.boot.system.base.annotation.StdWho;
import com.habi.boot.system.base.utils.QueryFilter;
import org.springframework.transaction.annotation.Transactional;

public interface IBaseService<T> {
    List<T> select(IRequest var1, T var2, int var3, int var4);

    T insert(IRequest var1, @StdWho T var2);

    T insertSelective(IRequest var1, @StdWho T var2);

    T updateByPrimaryKey(IRequest var1, @StdWho T var2);

    @Transactional(
            rollbackFor = {Exception.class}
    )
    T updateByPrimaryKeySelective(IRequest var1, @StdWho T var2);


    T selectByPrimaryKey(IRequest var1, T var2);

    int deleteByPrimaryKey(T var1);

    @Deprecated
    List<T> selectAll();

    List<T> selectAll(IRequest var1);

    List<T> batchUpdate(IRequest var1, @StdWho List<T> var2);

    int batchDelete(List<T> var1);
    //查询所有数据
    public List<T> findAll();
    //根据filter查询
    public List<T> findFilter (QueryFilter filter);
    //根据filter分页查询
    public Page<T> findPage(QueryFilter filter);



}


