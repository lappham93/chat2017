package com.mit.notification.consumers;

import com.mit.notification.bodies.ViewNewsBody;
import com.mit.notification.services.NewsService;
import com.mit.rabbitmq.RabbitRoutingKey;
import com.mit.rabbitmq.RabbitTopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hung Le on 5/8/17.
 */

@Service
public class NewsConsumer {
    @Autowired
    private NewsService newsService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitRoutingKey.NEWS_VIEW),
                    exchange = @Exchange(value = RabbitTopicExchange.TOPIC),
                    key = RabbitRoutingKey.NEWS_VIEW))
    public void updateView(ViewNewsBody body) {
        newsService.updateView(body);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitRoutingKey.NEWS_PROVIDER_VIEW),
                    exchange = @Exchange(value = RabbitTopicExchange.TOPIC),
                    key = RabbitRoutingKey.NEWS_PROVIDER_VIEW))
    public void updateProviderView(ViewNewsBody body) {
        newsService.updateProviderView(body);
    }
}
