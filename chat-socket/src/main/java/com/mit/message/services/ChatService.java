package com.mit.message.services;

import com.mit.http.ApiResponse;
import com.mit.message.entities.LastMessage;
import com.mit.message.repositories.LastMessageRepo;
import com.mit.message.responses.UserStatusResponse;
import com.mit.session.services.OnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hung Le on 7/12/17.
 */
@Service
public class ChatService {

    @Autowired
    private LastMessageRepo lastMessageRepo;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUserStatus(long userId, int status) {
        List<LastMessage> lastMessages = lastMessageRepo.getList(userId, 0, 50);
        for (LastMessage lastMessage: lastMessages) {
            long otherId = lastMessage.getRefIds().get(0) == userId ? lastMessage.getRefIds().get(1) : lastMessage.getRefIds().get(0);
            ApiResponse apiResponse = new ApiResponse<>(new UserStatusResponse(userId, status));
            if (onlineUserService.isOnline(otherId + "")) {
                messagingTemplate.convertAndSendToUser(otherId + "", "/queue/user-status", apiResponse);
            }
        }
    }
}
