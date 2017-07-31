package com.mit.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mit.session.SessionManagerImpl;
import com.mit.sessions.AdminSession;
import com.mit.user.entities.AdminProfile;

@Controller
@RequestMapping(value = "/logout")
public class LogoutController {
	@Value("${admin.prefix}")
	private String adminPrefix;
	@Autowired
	private SessionManagerImpl sessionManagerImp;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest req) {
		ModelAndView rs = new ModelAndView();
		String sessionKey = AdminSession.getSessionKey(req);
		AdminSession.removeSession(req);
		sessionManagerImp.expireSession(sessionKey, AdminProfile.class);
		rs.setViewName("redirect:" + adminPrefix + "/login");
		return rs;
	}

}
