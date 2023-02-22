/*
package com.zwx.learn.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "${mq.group.test}", topic = "${mq.topic.test}")
public class RemedyCardMailMQHandler implements RocketMQListener<MessageExt> {


    @Override
    public void onMessage(MessageExt messageExt) {
        log.info("接收补卡或请假的MQ消息，{}", messageExt);

    }
}*/
