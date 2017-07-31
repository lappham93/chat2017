package com.mit.upload.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Created by Hung on 3/24/2017.
 */

@Controller
public class FileUploadController {
    @MessageMapping("/upload")
    public void upload(SimpMessageHeaderAccessor headerAccessor, byte[] data) {
        System.out.println(new String(data));
    }
}
