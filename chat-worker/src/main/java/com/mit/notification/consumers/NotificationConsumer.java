package com.mit.notification.consumers;

import com.mit.notification.bodies.MultiTargetNotification;
import com.mit.notification.bodies.SingleTargetNotification;
import com.mit.notification.services.NotificationService;
import com.mit.rabbitmq.RabbitRoutingKey;
import com.mit.rabbitmq.RabbitTopicExchange;
import com.mit.utils.JsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hung Le on 4/5/17.
 */

@Service
public class NotificationConsumer {
	private Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    NotificationService notificationService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitRoutingKey.NOTIFY_SINGLE),
                    exchange = @Exchange(value = RabbitTopicExchange.TOPIC),
                    key = RabbitRoutingKey.NOTIFY_SINGLE))
    public void notifySingle(SingleTargetNotification notification) {
    	logger.info(JsonUtils.Instance.toJson(notification));
        notificationService.pushNotification(notification);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitRoutingKey.NOTIFY_MULTI),
                    exchange = @Exchange(value = RabbitTopicExchange.TOPIC),
                    key = RabbitRoutingKey.NOTIFY_MULTI))
    public void notifyMulti(MultiTargetNotification notification) {
    	logger.info(notification.getMessage());
        notificationService.pushNotification(notification);
    }
}
