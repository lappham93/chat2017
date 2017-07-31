package com.mit.socket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.mit.http.ApiResponse;
import com.mit.message.bodies.GetMessageBody;
import com.mit.message.bodies.MessageBody;
import com.mit.message.entities.Message2;
import com.mit.message.repositories.Message2Repo;
import com.mit.utils.AdminConstant;

@Controller
public class MessageController {
	@Autowired
	private Message2Repo messageRepo;
	@Autowired
	private SimpMessagingTemplate messageTemplate;

	@MessageMapping(value = "/message-send")
	public ApiResponse<Object> sendMessage(SimpMessageHeaderAccessor headerAccessor, MessageBody body) {
		try {
			Message2 message = body.toMessage();
			messageRepo.save(message);
			messageTemplate.convertAndSend("/topic/message", message);
			return new ApiResponse<>();
		} catch (Exception e) {
			return new ApiResponse<>(AdminConstant.errCode, AdminConstant.serverErrMsg);
		}
	}
	
	@MessageMapping(value = "/message-get")
	public ApiResponse<Object> getMessage(SimpMessageHeaderAccessor headerAccessor, GetMessageBody body) {
		try {
			//TODO
			return new ApiResponse<>();
		} catch (Exception e) {
			return new ApiResponse<>(AdminConstant.errCode, AdminConstant.serverErrMsg);
		}
	}
	
	@MessageMapping(value = "/message-delete")
	public ApiResponse<Object> deleteMessage(SimpMessageHeaderAccessor headerAccessor, Object body) {
		try {
			//TODO
			return new ApiResponse<>();
		} catch (Exception e) {
			return new ApiResponse<>(AdminConstant.errCode, AdminConstant.serverErrMsg);
		}
	}
}
