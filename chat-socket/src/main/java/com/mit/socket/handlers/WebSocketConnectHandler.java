package com.mit.socket.handlers;

import com.mit.message.enums.UserStatus;
import com.mit.message.services.ChatService;
import com.mit.session.services.OnlineUserService;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * Created by Hung Le on 3/6/17.
 */
public class WebSocketConnectHandler implements ApplicationListener<SessionConnectEvent> {

    Logger logger = LoggerFactory.getLogger("requestLogging");

    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private ChatService chatService;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        logger.info("connect user=" + sha.getUser().getName() + ", session=" + sha.getSessionId());

        onlineUserService.addUserSession(sha.getUser().getName(), sha.getSessionId());
        chatService.notifyUserStatus(NumberUtils.toLong(sha.getUser().getName()), UserStatus.ONLINE.getValue());
    }
}
