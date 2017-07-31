package com.mit.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mit.app.enums.AppType;
import com.mit.bodies.RegisterBody;
import com.mit.http.ApiResponse;
import com.mit.sessions.AdminSession;
import com.mit.user.bodies.SocialSignup;
import com.mit.user.entities.Admin;
import com.mit.user.entities.AdminProfile;
import com.mit.user.enums.AdminType;
import com.mit.user.repositories.AdminProfileRepo;
import com.mit.user.repositories.AdminRepo;
import com.mit.user.responses.LoginToken;
import com.mit.user.responses.RefreshTokenResponse;
import com.mit.user.services.LoginService;
import com.mit.utils.AdminConstant;
import com.mit.utils.AuthenticateUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Register api")
@Controller
@RequestMapping(value = "/register")
public class RegisterController {
	private final int expireTimeInSec = 7 * 24 * 60 * 60;
	@Autowired
	LoginService loginService;
	@Value("${admin.prefix}")
	private String adminPrefix;
	
	@Autowired
	private AdminProfileRepo adminProfileRepo;
	@Autowired
	private AdminRepo adminRepo;

	@ApiOperation(value = " register", notes = "Redirect to register if profile is null")
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest req) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		SocialSignup body = new SocialSignup();
		body.setFirstName(req.getParameter("fname"));
		body.setLastName(req.getParameter("lname"));
		body.setPhone(req.getParameter("phone"));
		body.setEmail(req.getParameter("email"));
		body.setToken(req.getParameter("token"));
		body.setType(NumberUtils.toInt(req.getParameter("type")));
		body.setCountryCode(req.getParameter("country"));
		
		String token = body.getToken();
		int type = body.getType();
		LoginToken loginToken = loginService.socialLogin(token, type);
		if (loginToken == null || loginToken.getProfile() != null) {
			mv.setViewName(adminPrefix + "/error");
		} else {
			loginToken = loginService.socialRegister(loginToken, body);
			if (loginToken == null || loginToken.getProfile() == null) {
				mv.setViewName(adminPrefix + "/error");
			} else {
				req.getSession().setAttribute("sessionKey", loginToken.getSessionKey());
        		req.getSession().setAttribute("refreshSessionKey", loginToken.getRefreshToken());
				req.getSession().setMaxInactiveInterval(expireTimeInSec);
				mv.setViewName("redirect:" + adminPrefix);
			}
		}

		return mv;
	}
	
	public ApiResponse<Object> register(HttpServletRequest request, @RequestBody RegisterBody body) {
		try {
			AdminProfile ap = new AdminProfile(AdminType.SYSTEM_ADMIN.getValue());
			ap.setStatus(1);
			ap.setFirstName("");
			ap.setLastName("");
			adminProfileRepo.save(ap);
	
			Admin admin = new Admin();
			admin.setUserName(body.getUserName());
			admin.setProfileId(ap.getId());
			String salt = RandomStringUtils.random(10);
			String password = body.getPassword();
			admin.setSalt(salt);
			admin.setPassword(AuthenticateUtils.hashPassword(password, salt));
			admin.setStatus(1);
			adminRepo.save(admin);
			
			RefreshTokenResponse token = loginService.generateTokens(ap, AppType.CLIENT.getValue());
			if (token != null) {
				AdminSession.setSession(request, token.getSessionKey(), token.getRefreshToken());
				return new ApiResponse<>(0, AdminConstant.loginSuccessMsg);
			}
			return new ApiResponse<>();
		} catch (Exception e) {
		}
		return new ApiResponse<>(AdminConstant.errCode, AdminConstant.serverErrMsg);
	}
}
