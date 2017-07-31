package com.mit.socket.controllers;

import com.mit.socket.bodies.HelloMessage;
import com.mit.socket.responses.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Created by Hung Le on 2/28/17.
 */

@Controller
public class GreetingController {

    Logger logger = LoggerFactory.getLogger("requestLogging");

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(SimpMessageHeaderAccessor headerAccessor, HelloMessage message) throws Exception {
        logger.info("sessionKey in greeting -- " + headerAccessor.getSessionAttributes().get("sessionKey"));

        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
