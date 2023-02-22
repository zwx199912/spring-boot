package com.zwx.learn.aop.apiCall;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiCall {
    /**
     * 最多的访问限制次数
     */
    long timecount () default 10;

    /**
     * 多长时间过期
     */
    long time () default  100L;


    /**过期时间格式
     *
     */
    TimeUnit timeunit() default TimeUnit.DAYS;


}
