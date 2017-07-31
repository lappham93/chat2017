package com.mit.message.controllers;

import com.mit.app.enums.AppConstKey;
import com.mit.app.services.AppConstManager;
import com.mit.define.ApiError;
import com.mit.http.ApiResponse;
import com.mit.http.MessageApiResponse;
import com.mit.message.bodies.*;
import com.mit.message.entities.LastMessage;
import com.mit.message.entities.Message;
import com.mit.message.enums.MessageContentType;
import com.mit.message.enums.MessageStatus;
import com.mit.message.repositories.LastMessageRepo;
import com.mit.message.repositories.MessageRepo;
import com.mit.message.responses.*;
import com.mit.message.services.MessageService;
import com.mit.notification.services.NewsService;
import com.mit.rabbitmq.RabbitRoutingKey;
import com.mit.session.entities.ProfileCache;
import com.mit.session.entities.UserSession;
import com.mit.session.services.OnlineUserService;
import com.mit.session.services.ProfileCacheService;
import com.mit.socket.services.StompMessagingService;
import com.mit.user.entities.Profile;
import com.mit.user.repositories.ProfileRepo;
import com.mit.user.responses.ProfileShortResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Hung on 6/24/2017.
 */

@Controller
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private StompMessagingService stompMessagingService;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private LastMessageRepo lastMessageRepo;
    @Autowired
    private ProfileCacheService profileCacheService;
    @Autowired
    private ProfileRepo profileRepo;
    @Autowired
    private AppConstManager appConstManager;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @MessageMapping("/message-send")
    public void sendMessage(SimpMessageHeaderAccessor headerAccessor, MessageBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");
        body.setFromUserId(us.getUserId());

        try {
            ProfileCache fromUser = profileCacheService.getProfile(us.getUserId());
            ProfileCache toUser = profileCacheService.getProfile(body.getToUserId());
            if ((fromUser.getBanUserIds() != null && fromUser.getBanUserIds().contains(body.getToUserId()))
                    || (toUser.getBanUserIds() != null && toUser.getBanUserIds().contains(us.getUserId()))) {
                stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.CHAT_BAN), headerAccessor);
            } else {
                MessageResponse messageResponse = messageService.addMessage(body);
                if (onlineUserService.isOnline(body.getToUserId() + "")) {
                    messageResponse.setStatus(MessageStatus.DELIVERED.getValue());
                    messageRepo.updateStatus(messageResponse.getId(), messageResponse.getStatus());
                    messagingTemplate.convertAndSendToUser(messageResponse.getToUser().getId() + "", "/queue/message", new ApiResponse<>(messageResponse));
                } else if (!toUser.isDisableNotifyChat()) {
                    newsService.notifyChatMessage(messageResponse);
                }
                stompMessagingService.sendResponse(new MessageApiResponse<>(messageResponse), headerAccessor);

                if (body.getToUserId() == appConstManager.getLong(AppConstKey.SUPPORT_USER_ID)) {
                    MessageBody messageBody = new MessageBody();
                    messageBody.setFromUserId(body.getToUserId());
                    messageBody.setToUserId(body.getFromUserId());
                    messageBody.setContent("Thank you for your feedback.");
                    messageBody.setContentType(MessageContentType.TEXT.getValue());
                    rabbitTemplate.convertAndSend(RabbitRoutingKey.MESSAGE_SEND, messageBody);
                }
            }
        } catch (Exception ex) {
            logger.error("error send message", ex);
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-get")
    public void getMessage(SimpMessageHeaderAccessor headerAccessor, GetMessageBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            Map<String, Object> rs = messageService.getMessage(us.getUserId(), body.getMessageId(), body.getCount(), body.isNewer());
            boolean hasMore = (boolean)rs.get("hasMore");
            List<LastMessageResponse> messageResponses = (List)rs.get("messages");
            MessageApiResponse apiResponse = new MessageApiResponse<>(messageResponses);
            stompMessagingService.sendResponse(apiResponse, headerAccessor);
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-user-delete")
    public void deleteUser(SimpMessageHeaderAccessor headerAccessor, DeleteUserBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");
        body.setFromUserId(us.getUserId());

        try {
            messageService.deleteUser(body);
            stompMessagingService.sendResponse(new MessageApiResponse<>(), headerAccessor);
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-user-get")
    public void getUserMessage(SimpMessageHeaderAccessor headerAccessor, GetUserMessageBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            Map<String, Object> rs = messageService.getUserMessage(us.getUserId(), body.getToUserId(),
                    body.getMessageId(), body.getCount(), body.isNewer());
            boolean hasMore = (boolean)rs.get("hasMore");
            List<MessageResponse> messageResponses = (List)rs.get("messages");
            MessageApiResponse apiResponse = new MessageApiResponse<>(messageResponses);
            stompMessagingService.sendResponse(apiResponse, headerAccessor);
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-status")
    public void updateMessageStatus(SimpMessageHeaderAccessor headerAccessor, MessageStatusBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            List<Message> messages = messageRepo.getListByIds(body.getIds());
            int userSide = 1;
            if (body.getStatus() == MessageStatus.DELETED.getValue()) {
                userSide = 0;
            }
            boolean check = true;
            for (Message message: messages) {
                if (message.getRefIds().get(userSide) != us.getUserId()) {
                    check = false;
                    break;
                }
            }

            if (check) {
                List<MessageStatusResponse> messageStatusResponses = new ArrayList<>(body.getIds().size());
                for (Message message: messages) {
                    message.setStatus(body.getStatus());

                    messageRepo.updateStatus(message.getId(), message.getStatus());

                    LastMessage lastMessage = lastMessageRepo.get(message.getRefIds());
                    if (lastMessage != null && lastMessage.getMessageId() == message.getId()) {
                        lastMessage.setStatus(message.getStatus());
                        if (message.getStatus() == MessageStatus.SEEN.getValue()) {
                            lastMessage.setNewCount(0);
                        }
                        lastMessageRepo.save(lastMessage);
                    }

                    MessageStatusResponse messageStatusResponse = new MessageStatusResponse(message);

                    long otherUserId = message.getRefIds().get(1 - userSide);
                    if (onlineUserService.isOnline(otherUserId + "")) {
                        messagingTemplate.convertAndSendToUser(otherUserId + "", "/queue/message-status", new ApiResponse<>(messageStatusResponse));
                    }

                    messageStatusResponses.add(messageStatusResponse);
                }
                stompMessagingService.sendResponse(new MessageApiResponse<>(messageStatusResponses), headerAccessor);
            } else {
                stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.DATA_INVALID), headerAccessor);
            }
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-status-get")
    public void getMessageStatus(SimpMessageHeaderAccessor headerAccessor, GetMessageStatusBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            List<MessageStatusResponse> messageResponses = messageService.getMessageStatus(us.getUserId(), body.getMessageId(), body.getLastUpdate());
            MessageApiResponse apiResponse = new MessageApiResponse<>(messageResponses);
            stompMessagingService.sendResponse(apiResponse, headerAccessor);
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-user-ban")
    public void banUser(SimpMessageHeaderAccessor headerAccessor, BanUserBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            Profile profile = profileRepo.getById(us.getUserId());
            if (profile.getBanUserIds() == null) {
                profile.setBanUserIds(new LinkedList<>());
            }
            boolean update = false;
            if (body.isBan()) {
                if (!profile.getBanUserIds().contains(body.getUserId())) {
                    profile.getBanUserIds().add(body.getUserId());
                    update = true;
                }
            } else {
                if (profile.getBanUserIds().contains(body.getUserId())) {
                    profile.getBanUserIds().remove(body.getUserId());
                    update = true;
                }
            }
            if (update) {
                profileRepo.updateBanUserIds(profile.getId(), profile.getBanUserIds());
                profileCacheService.deleteCache(profile.getId());
            }
            stompMessagingService.sendResponse(new MessageApiResponse<>(), headerAccessor);
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-user-ban-check")
    public void checkBanUser(SimpMessageHeaderAccessor headerAccessor, CheckBanUserBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            Profile profile = profileRepo.getById(us.getUserId());
            CheckBanUserResponse response = new CheckBanUserResponse(body.getUserId(), false);
            if (profile.getBanUserIds() != null && profile.getBanUserIds().contains(body.getUserId())) {
                response.setBan(true);
            }
            stompMessagingService.sendResponse(new MessageApiResponse<>(response), headerAccessor);
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/message-user-ban-get")
    public void getBannedUser(SimpMessageHeaderAccessor headerAccessor, GetBanUserBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            int from = (body.getPage() > 1) ? (body.getPage() - 1) * body.getCount() : 0;
            Profile user = profileRepo.getById(us.getUserId());
            List<Long> userIds = Collections.emptyList();
            boolean hasMore = false;
            if (!CollectionUtils.isEmpty(user.getBanUserIds()) && user.getBanUserIds().size() > from) {
                userIds = user.getBanUserIds().subList(from, Math.min(body.getCount() + from, user.getBanUserIds().size()));
                hasMore = user.getBanUserIds().size() > from + body.getCount();
            }
            Map<Long, Profile> profileMap = profileRepo.getMapByIds(userIds);
            List<ProfileShortResponse> userResponses = new ArrayList<>(userIds.size());
            for (long userId: userIds) {
                Profile profile = profileMap.get(userId);
                userResponses.add(new ProfileShortResponse(profile));
            }
            MessageApiResponse apiResponse = new MessageApiResponse<>(userResponses);
            ApiResponse.Paging paging = new ApiResponse.Paging(hasMore, body.getPage());
            apiResponse.setPaging(paging);
            stompMessagingService.sendResponse(apiResponse, headerAccessor);
        } catch (Exception ex) {
            logger.error("error get ban", ex);
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }

    @MessageMapping("/user-status-get")
    public void getUserStatus(SimpMessageHeaderAccessor headerAccessor, GetUserStatusBody body) {
        UserSession us = (UserSession) headerAccessor.getSessionAttributes().get("session");

        try {
            List<UserStatusResponse> statusResponses = messageService.getUserStatus(us.getUserId(), body.getUserIds());
            MessageApiResponse apiResponse = new MessageApiResponse<>(statusResponses);
            stompMessagingService.sendResponse(apiResponse, headerAccessor);
        } catch (Exception ex) {
            stompMessagingService.sendResponse(new MessageApiResponse<>(ApiError.SERVER_ERROR), headerAccessor);
        }
    }
}
