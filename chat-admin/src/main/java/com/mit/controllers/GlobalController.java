package com.mit.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mit.roles.Privilege;
import com.mit.session.entities.UserSession;
import com.mit.sessions.AdminSession;
import com.mit.user.entities.AdminProfile;
import com.mit.user.enums.AdminType;
import com.mit.user.repositories.AdminProfileRepo;
import com.mit.utils.AdminUtils;
import com.mit.utils.LinkBuilder;

@ControllerAdvice
public final class GlobalController {
	@Value("${admin.prefix}")
	private String adminPrefix;
	@Value("${admin.title}")
	private String adminTitle;
	@Value("${admin.resources.path}")
	private String resourcesPath;
	@Value("${facebook.appId}")
	private String facebookAppId;
	@Value("${accountkit.api.version}")
	private String apiVersion;
	@Value("${accountkit.appSecret}")
    private String appSecret;
	
	@Autowired
	private AdminProfileRepo profileRepo;
	
	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest req) {
		try {
			UserSession userSession = (UserSession)AdminSession.getUserSession(req);
			AdminProfile adminProfile = profileRepo.getById(userSession.getUserId());
			if (adminProfile != null) {
				model.addAttribute("admin_name", AdminUtils.buildProfileName(adminProfile));
				model.addAttribute("admin_type", adminProfile.getAdminType());
				model.addAttribute("admin_role", Privilege.getPrivilege().get(adminProfile.getAdminType()).getRole());
			}
		} catch (Exception e){}
		
		model.addAttribute("admin_prefix", adminPrefix);
		model.addAttribute("admin_title", adminTitle);
		
		model.addAttribute("resources_path", resourcesPath);
		model.addAttribute("facebook_appId", facebookAppId);
		model.addAttribute("accountKit_api_version", apiVersion);
		model.addAttribute("app_secret", apiVersion);
		
		model.addAttribute("system_admin_type", AdminType.SYSTEM_ADMIN.getValue());
		model.addAttribute("region_manager_type", AdminType.REGION_MANAGER.getValue());
		
		model.addAttribute("socket_link", LinkBuilder.buildApiSocketLink());
	}
}
