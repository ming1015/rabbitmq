package com.ming.mq.rabbitmq.consumer;

import com.ming.mq.rabbitmq.config.ConfirmConfig;
import com.rabbitmq.client.Channel;
import java.lang.String;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * ClassName:WarningConsumer
 * Package:com.ming.mq.rabbitmq.consumer
 * Description:
 *
 * @Date:2021/8/16 16:03
 * Author:ming
 */
@Slf4j
@Component
public class WarningConsumer {
    @RabbitListener(queues = {ConfirmConfig.WARNING_QUEUE})
    public void warning(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("{}不可路由",msg);
    }

}
