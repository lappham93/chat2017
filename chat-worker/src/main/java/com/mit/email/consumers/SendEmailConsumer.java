package com.mit.email.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mit.asset.services.EmailService;
import com.mit.feedback.bodies.FeedbackBody;
import com.mit.rabbitmq.RabbitRoutingKey;
import com.mit.rabbitmq.RabbitTopicExchange;

@Service
public class SendEmailConsumer {
	private Logger logger = LoggerFactory.getLogger(SendEmailConsumer.class);
	
	@Autowired
	private EmailService emailService;
	
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitRoutingKey.FEEDBACK_EMAIL),
                    exchange = @Exchange(value = RabbitTopicExchange.TOPIC),
                    key = RabbitRoutingKey.FEEDBACK_EMAIL))
    public void sendFeedback(FeedbackBody body) {
    	logger.info("send feedback email userId=" + body.getUserId());
    	emailService.sendFeedbackContinue(body.getUserId(), body.getMessage());
    }
}
