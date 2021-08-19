package com.ming.mq.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:ConfirmConfig
 * Package:com.ming.mq.rabbitmq.config
 * Description:备份交换机优先级高于队列回退
 *
 * @Date:2021/8/16 14:09
 * Author:ming
 */
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    public static final String BACKUP_EXCHANGE = "backup.exchange";
    public static final String CONFIRM_QUEUE = "confirm.queue";
    public static final String BACKUP_QUEUE = "backup.queue";
    public static final String WARNING_QUEUE = "warning.queue";

    @Bean
    public DirectExchange directExchange() {
        //备份交换机：alternate
        return ExchangeBuilder.
                directExchange(CONFIRM_EXCHANGE).durable(true).
                alternate(BACKUP_EXCHANGE).build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding confirmBinding(
            @Qualifier("directExchange") DirectExchange directExchange,
            @Qualifier("confirmQueue") Queue queue
    ) {
        return BindingBuilder.bind(queue).to(directExchange).with("confirmKey");

    }

    //备份交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Binding backupBinding(
            @Qualifier("backupQueue") Queue queue,
            @Qualifier("backupExchange") FanoutExchange exchange
    ) {
        //广播交换机，路由地址不需要，绑定的队列都会收到
        return BindingBuilder.bind(queue).to(exchange);
    }
}

 /*   @Bean
    public Binding warningBinding(
            @Qualifier("warningQueue") Queue queue,
            @Qualifier("backupExchange") FanoutExchange exchange
    ) {*/
        //广播交换机，路由地址不需要，绑定的队列都会收到
        //return BindingBuilder.bind(queue).to(exch