package com.mit.app;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

/**
 * Created by Hung Le on 3/6/17.
 */

@Configuration
public class RabbitConfig {
    @Autowired
    Environment env;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(env.getProperty("rabbitmq.host"));
        connectionFactory.setPort(env.getProperty("rabbitmq.port", Integer.class));
        connectionFactory.setUsername(env.getProperty("rabbitmq.user"));
        connectionFactory.setPassword(env.getProperty("rabbitmq.password"));

        return connectionFactory;
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter(){
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setCreateMessageIds(true);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2MessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setMessageConverter(jackson2MessageConverter);
        rabbitTemplate.setConnectionFactory(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate, MappingJackson2MessageConverter jackson2Converter) {
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate(rabbitTemplate);
//        rabbitMessagingTemplate.setMessageConverter(jackson2Converter);
        return rabbitMessagingTemplate;
    }
}
