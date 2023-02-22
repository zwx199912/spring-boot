package com.zwx.learn;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zwx
 * @description
 * @date 2022 -09-19
 **/
@SpringBootApplication(exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class})
@EnableDiscoveryClient
public class NacosStudyLearnApplicaion {

    public static void main(String[] args) {

        SpringApplication.run(NacosStudyLearnApplicaion.class, args);
    }
}

