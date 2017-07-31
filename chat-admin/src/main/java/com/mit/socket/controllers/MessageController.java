package com.mit.socket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.mit.http.ApiResponse;
import com.mit.message.bodies.GetMessageBody;
import com.mit.message.repositories.Message2Repo;
import com.mit.socket.bodies.HelloMessage;
import com.mit.socket.responses.Greeting;
import com.mit.utils.AdminConstant;

@Controller
public class MessageController {
	@Autowired
	private Message2Repo messageRepo;
	@Autowired
	private SimpMessagingTemplate messageTemplate;

	@MessageMapping("/hello")
//    @SendTo("/topic/greetings")
    public Greeting greeting(SimpMessageHeaderAccessor headerAccessor, HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        messageTemplate.convertAndSend("/topic/greetings", new Greeting("Hello, " + message.getName() + "!"));
        return new Greeting("Hello, " + message.getName() + "!");
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
