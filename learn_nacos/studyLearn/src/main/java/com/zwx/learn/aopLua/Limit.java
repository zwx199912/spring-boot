package com.zwx.learn.aopLua;

import java.lang.annotation.*;

//表明注解可用于的地方  METHOD:方法上  TYPE:用于描述类、接口(包括注解类型) 或enum声明
@Target({ElementType.METHOD, ElementType.TYPE})
//存活阶段   runtime:运行期
@Retention(RetentionPolicy.RUNTIME)
//可继承
@Inherited
//作用域 javaDoc
@Documented
public @interface Limit {
 
	// key
	String key() default "";
 
	// 给定的时间范围
	int period();
 
	// 一定时间内最多访问次数
	int count();
 
	// 限流的类型  （自定义key或者请求ip）
	LimitType limitType() default LimitType.CUSTOMER;
 
}