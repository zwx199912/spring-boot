package com.zwx.learn.aop.apiCall;

import com.common.utill.redis.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class ApiCallAdvice {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IpAddressUtil ipAddressUtil;
    @Autowired
    RedisTemplate redisTemplate;

    private static final String DATE_FORMAT = "yyyy-MM-dd";



    @Pointcut("@annotation(com.zwx.learn.aop.apiCall.ApiCall)")
    public void apiCall(){


    }


    @Before("apiCall()")
    public void before(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURI();
        String date = dateFormat(DATE_FORMAT);
        String ip = ipAddressUtil.getIpAddress(request);


        if(StringUtils.isEmpty(ip)){
            throw new ApplicationContextException("ip地址为空，非法访问");
        }
        String ipkey = "API_LIMIT_DAY:"+url+"_"+ip+"_"+date;
        if(redisUtils.hasKey(ipkey)){
            if(Integer.parseInt(redisUtils.get(ipkey).toString())>10){
                throw new ApplicationContextException("超过最大访问次数");
            }
            redisUtils.incrBy(ipkey,1);
        }else {
            redisUtils.set(ipkey,1, 1L);
        }
    }

    @Around(value = "apiCall()")
    public Object requestLimitAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        ApiCall apiCall = methodSignature.getMethod().getAnnotation(ApiCall.class);

        long limitCount = apiCall.timecount();
        //获取request 然后获取访问ip
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = ipAddressUtil.getIpAddress(request);
        String url = request.getRequestURI();
        String key = "API_LIMIT"+url+"_"+ip+"_";
        int size = redisUtils.keys(key+"*").size();
        if(size> limitCount){
            throw new ApplicationContextException("超过最大访问次数");
        }
        Boolean stats = redisUtils.set(key+System.currentTimeMillis(),"1",10L);
        System.out.println("写入数据库成功"+stats);
        return pjp.proceed();
    }


    private String dateFormat(String format ) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        String dateString  = simpleDateFormat.format(date);
        return dateString;
    }

}

