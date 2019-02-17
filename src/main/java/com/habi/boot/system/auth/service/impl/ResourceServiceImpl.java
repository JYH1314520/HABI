package com.habi.boot.system.auth.service.impl;

import com.habi.boot.system.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.habi.boot.system.auth.entity.Resource;
import com.habi.boot.system.auth.service.IResourceService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements IResourceService{

}