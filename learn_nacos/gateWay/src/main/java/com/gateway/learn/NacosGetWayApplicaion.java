package com.gateway.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zwx
 * @description
 * @date 2022 -09-19
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class NacosGetWayApplicaion {

    public static void main(String[] args) {
        SpringApplication.run(NacosGetWayApplicaion.class, args);
    }
}

