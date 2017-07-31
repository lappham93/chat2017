package com.mit.message.consumers;

import com.mit.http.ApiResponse;
import com.mit.message.bodies.MessageBody;
import com.mit.message.enums.MessageStatus;
import com.mit.message.repositories.MessageRepo;
import com.mit.message.responses.MessageResponse;
import com.mit.message.services.MessageService;
import com.mit.notification.services.NewsService;
import com.mit.rabbitmq.RabbitRoutingKey;
import com.mit.rabbitmq.RabbitTopicExchange;
import com.mit.session.entities.ProfileCache;
import com.mit.session.services.OnlineUserService;
import com.mit.session.services.ProfileCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Hung Le on 7/17/17.
 */
@Service
public class MessageConsumer {

    private Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private ProfileCacheService profileCacheService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitRoutingKey.MESSAGE_SEND),
                    exchange = @Exchange(value = RabbitTopicExchange.TOPIC),
                    key = RabbitRoutingKey.MESSAGE_SEND))
    public void sendMessage(MessageBody body) {
        try {
            ProfileCache fromUser = profileCacheService.getProfile(body.getFromUserId());
            ProfileCache toUser = profileCacheService.getProfile(body.getToUserId());
            if (!((fromUser.getBanUserIds() != null && fromUser.getBanUserIds().contains(body.getToUserId()))
                    || (toUser.getBanUserIds() != null && toUser.getBanUserIds().contains(body.getFromUserId())))) {
                MessageResponse messageResponse = messageService.addMessage(body);
                if (onlineUserService.isOnline(body.getToUserId() + "")) {
                    messageResponse.setStatus(MessageStatus.DELIVERED.getValue());
                    messageRepo.updateStatus(messageResponse.getId(), messageResponse.getStatus());
                    messagingTemplate.convertAndSendToUser(messageResponse.getToUser().getId() + "", "/queue/message", new ApiResponse<>(messageResponse));
                } else if (!toUser.isDisableNotifyChat()) {
                    newsService.notifyChatMessage(messageResponse);
                }
            }
        } catch (Exception ex) {
            logger.error("error send message", ex);
        }
    }
}
