package com.mit.controllers;

import com.mit.app.enums.AppType;
import com.mit.bodies.LoginBody;
import com.mit.http.ApiResponse;
import com.mit.sessions.AdminSession;
import com.mit.user.entities.Admin;
import com.mit.user.entities.AdminProfile;
import com.mit.user.repositories.AdminProfileRepo;
import com.mit.user.repositories.AdminRepo;
import com.mit.user.responses.RefreshTokenResponse;
import com.mit.user.services.LoginService;
import com.mit.utils.AdminConstant;
import com.mit.utils.AuthenticateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Api(value = "Login api")
@Controller
@RequestMapping(value = "/login")
public class LoginController {
	@Autowired
	AdminProfileRepo adminProfileRepo;
	@Value("${admin.prefix}")
	private String adminPrefix;
	@Autowired
	private LoginService loginService;
	@Autowired 
	private AdminRepo adminRepo;

	public static final Logger _logger = LoggerFactory.getLogger(LoginController.class);

	@ApiOperation(value = "render page login")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView render(Model model) {
		ModelAndView rs = new ModelAndView("login");
		return rs;
	}

	@ApiOperation(value = "system admin login", notes = "User name - password login")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse<Object> systemAdminLogin(HttpServletRequest req,
			@RequestBody LoginBody body) {
		Admin admin = adminRepo.getByUserName(body.getName());
		if (admin != null) {
			if (AuthenticateUtils.hashPassword(body.getPassword(), admin.getSalt()).equals(admin.getPassword())) {
				AdminProfile profile = adminProfileRepo.getById(admin.getId());
				if (profile != null && profile.isActive()) {
					RefreshTokenResponse token = loginService.generateTokens(profile, AppType.CLIENT.getValue());
					if (token != null) {
						AdminSession.setSession(req, token.getSessionKey(), token.getRefreshToken());
						return new ApiResponse<>(0, AdminConstant.loginSuccessMsg);
					}
				}
			}
		}
		return new ApiResponse<>(AdminConstant.errCode, AdminConstant.loginErrMsg);
	}

}