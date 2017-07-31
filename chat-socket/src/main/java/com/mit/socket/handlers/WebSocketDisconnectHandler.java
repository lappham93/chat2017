package com.mit.socket.handlers;

import com.mit.message.enums.UserStatus;
import com.mit.message.services.ChatService;
import com.mit.session.services.OnlineUserService;
import com.mit.user.repositories.ProfileRepo;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;

/**
 * Created by Hung Le on 3/6/17.
 */
public class WebSocketDisconnectHandler implements ApplicationListener<SessionDisconnectEvent> {

    Logger logger = LoggerFactory.getLogger("requestLogging");

    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ProfileRepo profileRepo;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        logger.info("disconnect user=" + sha.getUser().getName() + ", session=" + sha.getSessionId());

        onlineUserService.deleteUserSession(sha.getUser().getName(), sha.getSessionId());
        long userId = NumberUtils.toLong(sha.getUser().getName());
        chatService.notifyUserStatus(userId, UserStatus.OFFLINE.getValue());
        profileRepo.updateLastOnline(userId, new Date());
    }
}