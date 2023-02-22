package com.zwx.learn.aop.controller;

import com.common.utill.config.SftpConfig;
import com.zwx.learn.aop.apiCall.ApiCall;
import com.zwx.learn.aop.doMain.Test;
import com.zwx.learn.aop.service.ApiCallService;
import com.common.utill.redis.RedisUtils;
import com.zwx.learn.aopLua.Limit;
import com.zwx.learn.aopLua.LimitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/apiCall")
public class ApiCallController {

    private static int count1 = 0;
    private static int count2 = 0;
    private static int count3 = 0;

    @Autowired
    private ApiCallService apiCallService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SftpConfig readSftpConfig;



    @ApiCall(timecount = 5, time = 10,  timeunit = TimeUnit.SECONDS)
    @GetMapping("test")
    public List<Test>  timedMail(){
        List<Test> testList = apiCallService.queryTest();
        return testList;
    }


    @Limit(key = "limitTest", period = 10, count = 5)
    @GetMapping("limit1")
    public List<Test>  timedMail2(){
        List<Test> testList = apiCallService.queryTest();
        return testList;
    }


    /**
     * 秒内允许请求3次，自定义key
     *
     * @return
     */
    @Limit(key = "customer_limit_key", period = 10, count = 5, limitType = LimitType.CUSTOMER)
    @GetMapping("/limitOwnKey")
    public String testLimiter2() {
        return "success--" + ++count2;
    }

    /**
     * 秒内允许请求3次，key为请求ip
     *
     * @return
     */
    @Limit(period = 10, count = 5, limitType = LimitType.IP)
    @GetMapping("/limitIp")
    public String testLimiter3() {
        return "success--" + ++count3;
    }




    @GetMapping("sentinel")
    public String timedMail112(){
        return "success--" + ++count2;
    }

}
