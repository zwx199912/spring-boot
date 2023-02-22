package com.zwx.learn.aop.service.impl;

import com.zwx.learn.aop.doMain.Test;
import com.zwx.learn.aop.mapper.ApiCallMapper;
import com.zwx.learn.aop.service.ApiCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiCallServiceImpl implements ApiCallService {

    @Autowired
    private ApiCallMapper apiCallMapper;


    @Override
    public List<Test> queryTest() {
        List<Test> testList = apiCallMapper.queryTest();
        return testList;
    }
}
