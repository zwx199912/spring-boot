package com.zwx.learn.test;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
@Slf4j
public class SenDMessageToMq {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenDMessageToMq.class);

    @Value("${mq.topic.test}")
    private String leaveTopic;

    @Autowired
    private RocketMQTemplate rocketMqTemplate;

    public void sendToMQ() {
        try {
            rocketMqTemplate.sendOneWay(leaveTopic,"1212");
            LOGGER.info("发送消息给MQ成功");
        } catch (Exception e) {
            LOGGER.error("发送邮件失败", e);
        }
    }
}


