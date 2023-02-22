package com.zwx.learn.aop.mapper;

import com.zwx.learn.aop.doMain.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApiCallMapper {

    List<Test> queryTest();
}
