package com.zwx.learn.aopLua.aspect;

import com.zwx.learn.aopLua.Limit;
import com.zwx.learn.aopLua.LimitType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Configuration
public class LimitInterceptor {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
 
	/**
	 * 拦截有@Limit注解的public方法
	 * 
	 * @param
	 * @return
	 */
	@Around("@annotation(com.zwx.learn.aopLua.Limit)")
	public Object interceptor(ProceedingJoinPoint ppt) {
 
		// 获取方法对象
		MethodSignature signature = (MethodSignature) ppt.getSignature();
		Method method = signature.getMethod();
 
		// 获取@Limit注解对象
		Limit limitAnnotation = method.getAnnotation(Limit.class);
 
		// 获取key类型
		LimitType limitType = limitAnnotation.limitType();
 
		// 获取请求限制时间段
		int limitPeriod = limitAnnotation.period();
		//请求限制次数
		int limitCount = limitAnnotation.count();
 
		// 根据限流类型获取不同的key ,如果不传以方法名作为key
		String key;
		switch (limitType) {
		case IP:
			key = getIpAddress();
			break;
		case CUSTOMER:
			key = limitAnnotation.key();
			break;
		default:
			key = method.getName();
		}
 
		// 定义key参数
		List<String> keys = new ArrayList<String>();
		keys.add(key);
 
		try {
			// 获取Lua脚本内容
			String luaScript = buildLuaScript();
 
			// Reids整合Lua
			RedisScript<Number> redisScript = new DefaultRedisScript<>(
					luaScript, Number.class);
			// 执行Lua,并返回key值
			Number count = redisTemplate.execute(redisScript, keys, limitCount,
					limitPeriod);
 
			// 判断是否阻止请求
			if (count != null && count.intValue() <= limitCount) {
				return ppt.proceed();
			} else {
				throw new RuntimeException("访问次数超过最大限度,请稍后再试");
			}
		} catch (Throwable e) {
			if (e instanceof RuntimeException) {
				throw new RuntimeException(e.getLocalizedMessage());
			}
			throw new RuntimeException("服务器运行错误");
		}
 
	}
 
	/**
	 * 编写 redis Lua 限流脚本
	 */
	public String buildLuaScript() {
		StringBuilder lua = new StringBuilder();
		lua.append("local c");
		lua.append("\nc = redis.call('get',KEYS[1])");
		// 调用不超过最大值，则直接返回
		lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
		lua.append("\nreturn c;");
		lua.append("\nend");
		// 执行计算器自加
		lua.append("\nc = redis.call('incr',KEYS[1])");
		lua.append("\nif tonumber(c) == 1 then");
		// 从第一次调用开始限流，设置对应键值的过期
		lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
		lua.append("\nend");
		lua.append("\nreturn c;");
		return lua.toString();
	}
 
	/**
	 * 获取请求ip
	 */
	public String getIpAddress() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}