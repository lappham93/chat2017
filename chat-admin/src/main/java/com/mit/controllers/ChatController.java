package com.mit.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mit.utils.AdminUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Chat controller")
@RestController
@RequestMapping(value = "/chat")
public class ChatController {

	@ApiOperation(value = "render chat")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView renderAppConstant() {
		ModelAndView rs = AdminUtils.initPage("chat", "Chat", null, "isChatTab");
		return rs;
	}
}
