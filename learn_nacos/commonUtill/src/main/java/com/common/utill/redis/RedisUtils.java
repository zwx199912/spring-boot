package com.common.utill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtils {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/** 默认过期时长，单位：秒 */
	public static final long DEFAULT_EXPIRE = 60 * 60 * 24L;

	/** 不设置过期时长 */
	public static final long NOT_EXPIRE = -1;

	/**
	 * 插入缓存默认时间
	 * 
	 * @param key   键
	 * @param value 值
	 * @author zmr
	 */
	public void set(String key, Object value) {
		set(key, value,DEFAULT_EXPIRE);
	}



	/**
	 * 插入缓存
	 * 
	 * @param key    键
	 * @param value  值
	 * @param expire 过期时间(s)
	 * @author zmr
	 */
	public boolean set(String key, Object value, long expire) {
		redisTemplate.opsForValue().set(key, value,expire, TimeUnit.DAYS);
		return true;
	}

	/**
	 * 返回字符串结果
	 * 
	 * @param key 键
	 * @return
	 * @author zmr
	 */
	public String get(String key) {
		Object val = redisTemplate.opsForValue().get(key);
		return val == null ? null : String.valueOf(val);
	}

	/**
	 * 返回指定类型结果
	 * 
	 * @param key   键
	 * @param clazz 类型class
	 * @return
	 * @author zmr
	 */
	public <T> T get(String key, Class<T> clazz) {
		Object val = redisTemplate.opsForValue().get(key);
		return val == null ? null : fromJson(toJson(val), clazz);
	}
	/**
	 * 判断key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 查找匹配的key
	 *
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 增加(自增长), 负数则为自减
	 *
	 * @param key
	 * @return
	 */
	public Long incrBy(String key, long increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 键
	 * @author zmr
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * Object转成JSON数据
	 */
	private String toJson(Object object) {
		if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
				|| object instanceof Boolean || object instanceof String) {
			return String.valueOf(object);
		}
		return JSON.toJSONString(object);
	}

	/**
	 * JSON数据，转成Object
	 */
	private <T> T fromJson(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
}