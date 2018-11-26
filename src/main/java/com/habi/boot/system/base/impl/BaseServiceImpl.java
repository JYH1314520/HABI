package com.habi.boot.system.base.impl;


import java.util.Iterator;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.habi.boot.system.base.BaseEntity;
import com.habi.boot.system.base.IBaseService;
import com.habi.boot.system.base.IRequest;
import com.habi.boot.system.base.exception.UpdateFailedException;
import com.habi.boot.system.base.mapper.Mapper;
import com.habi.boot.system.base.annotation.StdWho;
import com.habi.boot.system.base.utils.QueryFilter;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class BaseServiceImpl<T> implements IBaseService<T> {
    @Autowired
    protected Mapper<T> mapper;
    @Autowired
    private ServiceListenerChainFactory chainFactory;

    public BaseServiceImpl() {
    }

    @Transactional(
            propagation = Propagation.SUPPORTS
    )
    public List<T> select(IRequest request, T condition, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return this.mapper.select(condition);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public T insert(IRequest request,@StdWho T record) {
        record = (T) this.chainFactory.getChain(this).beforeInsert(request, record);
        this.mapper.insert(record);
        record = (T) this.chainFactory.getChain(this).afterInsert(request, record);
        return record;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public T insertSelective(IRequest request,@StdWho T record) {
        record = (T) this.chainFactory.getChain(this).beforeInsert(request, record);
        this.mapper.insertSelective(record);
        record = (T) this.chainFactory.getChain(this).afterInsert(request, record);
        return record;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public T updateByPrimaryKey(IRequest request, @StdWho T record) {
        record = (T) this.chainFactory.getChain(this).beforeUpdate(request, record);
        int ret = this.mapper.updateByPrimaryKey(record);
        this.checkOvn(ret, record);
        record = (T) this.chainFactory.getChain(this).afterUpdate(request, record);
        return record;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public T updateByPrimaryKeySelective(IRequest request, @StdWho T record) {
        record = (T) this.chainFactory.getChain(this).beforeUpdate(request, record);
        int ret = this.mapper.updateByPrimaryKeySelective(record);
        this.checkOvn(ret, record);
        record = (T) this.chainFactory.getChain(this).afterUpdate(request, record);
        return record;
    }



    @Transactional(
            propagation = Propagation.SUPPORTS
    )
    public T selectByPrimaryKey(IRequest request, T record) {
        return (T) this.mapper.selectByPrimaryKey(record);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public int deleteByPrimaryKey(T record) {
        record = (T) this.chainFactory.getChain(this).beforeDelete((IRequest)null, record);
        int ret = this.mapper.deleteByPrimaryKey(record);
        this.checkOvn(ret, record);
        record = (T) this.chainFactory.getChain(this).afterDelete((IRequest)null, record);
        return ret;
    }

    protected void checkOvn(int updateCount, Object record) {
        if (updateCount == 0 && record instanceof BaseEntity) {
            BaseEntity baseDTO = (BaseEntity)record;
            if (baseDTO.getObjectVersionNumber() != null) {
                throw new RuntimeException(new UpdateFailedException(baseDTO));
            }
        }

    }

    /** @deprecated */
    @Deprecated
    @Transactional(
            propagation = Propagation.SUPPORTS
    )
    public List<T> selectAll() {
        return this.mapper.selectAll();
    }

    @Transactional(
            propagation = Propagation.SUPPORTS
    )
    public List<T> selectAll(IRequest request) {
        return this.mapper.selectAll();
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public List<T> batchUpdate(IRequest request, @StdWho List<T> list) {
        IBaseService<T> self = (IBaseService)AopContext.currentProxy();
        Iterator var4 = list.iterator();

        while(var4.hasNext()) {
            T t = (T) var4.next();
            String var6 = ((BaseEntity)t).get__status();
            byte var7 = -1;
            switch(var6.hashCode()) {
                case -1335458389:
                    if (var6.equals("delete")) {
                        var7 = 2;
                    }
                    break;
                case -838846263:
                    if (var6.equals("update")) {
                        var7 = 1;
                    }
                    break;
                case 96417:
                    if (var6.equals("add")) {
                        var7 = 0;
                    }
            }

            switch(var7) {
                case 0:
                    self.insertSelective(request, t);
                    break;
                case 1:
                    if (this.useSelectiveUpdate()) {
                        self.updateByPrimaryKeySelective(request, t);
                    } else {
                        self.updateByPrimaryKey(request, t);
                    }
                    break;
                case 2:
                    self.deleteByPrimaryKey(t);
            }
        }

        return list;
    }

    protected boolean useSelectiveUpdate() {
        return true;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public int batchDelete(List<T> list) {
        IBaseService<T> self = (IBaseService)AopContext.currentProxy();
        int c = 0;

        Object t;
        for(Iterator var4 = list.iterator(); var4.hasNext(); c += self.deleteByPrimaryKey((T) t)) {
            t = var4.next();
        }

        return c;
    }

    public List<T> findAll() {
        return this.mapper.selectAll();
    }

    public List<T> findFilter (QueryFilter filter) {
        return this.mapper.selectByExample(filter.getExample());
    }

    public Page<T> findPage(QueryFilter filter) {
        Page<T> p = null;
        if (Integer.valueOf(-1).compareTo(filter.getPageSize()) == 0) {
            p = PageHelper.startPage(filter.getPage(), 0);
        } else {
            p = PageHelper.startPage(filter.getPage(), filter.getPageSize());
        }

        this.mapper.selectByExample(filter.getExample());
        return p;
    }

}

