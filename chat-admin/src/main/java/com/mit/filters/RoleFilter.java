package com.mit.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.mit.app.Application;
import com.mit.roles.Action;
import com.mit.roles.Privilege;
import com.mit.session.entities.UserSession;
import com.mit.sessions.AdminSession;
import com.mit.user.entities.AdminProfile;
import com.mit.user.repositories.AdminProfileRepo;
import com.mit.utils.AdminConstant;

public class RoleFilter implements Filter {
	private String unAuthorRedirect;
	private String permissionLimitRedirect;
	private List<String> excludes; 
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		unAuthorRedirect = filterConfig.getInitParameter("unAuthorRedirect");
		permissionLimitRedirect = filterConfig.getInitParameter("permissionLimitRedirect");
		String excludePatterns = filterConfig.getInitParameter("excludePatterns");
		if (!StringUtils.isEmpty(excludePatterns)) {
			excludes = new LinkedList<>(Arrays.asList(excludePatterns.split(",")));
		}
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)servletRequest;
		HttpServletResponse resp = (HttpServletResponse)servletResponse;
		String uri = req.getRequestURI();
		String method = req.getMethod();
		
		if (!CollectionUtils.isEmpty(excludes)) {
			for (String exclude : excludes) {
				if (uri.startsWith(exclude)) {
					chain.doFilter(servletRequest, servletResponse);
					return;
				}
			}
		}
		UserSession userSession = AdminSession.getUserSession(req);
		if (userSession == null) {
			resp.sendRedirect(unAuthorRedirect);
			return;
		}
		AdminProfileRepo adminProfileRepo = Application.AppCtx.getBean(AdminProfileRepo.class);
		AdminProfile profile = adminProfileRepo.getById(userSession.getUserId());
		if (profile == null) {
			resp.sendRedirect(unAuthorRedirect);
			return;
		}
		Privilege privilege = Privilege.getPrivilege().get(profile.getAdminType());
		if (privilege == null) {
			resp.sendRedirect(unAuthorRedirect);
			return;
		}
		for (Action action : privilege.getActions()) {
			if (uri.startsWith(action.getUri()) && (action.isAllMethod() || (action.getMethods() != null && action.getMethods().contains(method)))) {
				AdminSession.setProfile(req, profile);
				chain.doFilter(servletRequest, servletResponse);
				return;
			}
		}
		if (method == "GET") {
			resp.sendRedirect(permissionLimitRedirect);
		} else {
			resp.sendError(AdminConstant.errCode);
		}
	}

	@Override
	public void destroy() {
	}

}
