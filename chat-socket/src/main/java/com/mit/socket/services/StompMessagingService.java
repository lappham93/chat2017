package com.mit.socket.services;

import com.mit.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * Created by Hung on 6/24/2017.
 */

@Service
public class StompMessagingService {
    @Autowired
    private MessageChannel clientOutboundChannel;

    public void sendResponse(Object payload, SimpMessageHeaderAccessor headerAccessor) {
        StompHeaderAccessor ackHeader = StompHeaderAccessor.create(StompCommand.MESSAGE);
        ackHeader.setDestination(headerAccessor.getDestination());
        ackHeader.setSessionId(headerAccessor.getSessionId());
        ackHeader.setUser(headerAccessor.getUser());
        ackHeader.setNativeHeader(StompHeaderAccessor.STOMP_RECEIPT_ID_HEADER, headerAccessor.getFirstNativeHeader(StompHeaderAccessor.STOMP_RECEIPT_HEADER));
        ackHeader.setContentType(MimeTypeUtils.APPLICATION_JSON);

        Message<byte[]> ackMessage = MessageBuilder.withPayload(JsonUtils.Instance.toByteJson(payload)).setHeaders(ackHeader).build();
        clientOutboundChannel.send(ackMessage);
//        clientOutboundChannel.send(MessageBuilder.createMessage(payload, headerAccessor.getMessageHeaders()));
    }
}
