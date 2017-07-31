package com.mit.filters;

import com.mit.http.context.ApplicationContextProvider;
import com.mit.http.session.SessionManager;
import com.mit.sessions.AdminSession;
import com.mit.user.responses.RefreshTokenResponse;
import com.mit.user.services.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lappv on 2/20/17.
 */
public class AdminAuthenticationFilter<T> implements Filter {

    Logger logger = LoggerFactory.getLogger(AdminAuthenticationFilter.class);

    private final SessionManager<T> sessionManager;
    List<String> excludes = new LinkedList<>();
    private String classNames = "";
    private String redirectPath = "";
    
    public AdminAuthenticationFilter(SessionManager<T> sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludePatterns = filterConfig.getInitParameter("excludePatterns");
        classNames = filterConfig.getInitParameter("profileClasses");
        if (excludePatterns != null && !excludePatterns.isEmpty()) {
            excludes.addAll(Arrays.asList(excludePatterns.split(",")));
        }
        redirectPath = filterConfig.getInitParameter("redirectPath");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        String sessionKey = AdminSession.getSessionKey(httpRequest);
        String uri = httpRequest.getRequestURI();
        boolean isAuthenticated = false;

//        logger.info("uri: " + uri);

        for (String exclude : excludes) {
//            logger.info("exclude: " + exclude);
            if (uri.startsWith(exclude)) {
                isAuthenticated = true;
                break;
            }
        }

//        logger.info("isAuthenticated: " + isAuthenticated);
        if (isAuthenticated) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if (!StringUtils.isEmpty(sessionKey)) {
            	for (String className : classNames.split(",")) {
	                String cacheKey = className + "." + sessionKey;
	                T session = sessionManager.get(cacheKey);
	
	                if (session != null) {
	                    AdminSession.setAttribute(httpRequest, sessionKey, session);
	                    filterChain.doFilter(httpRequest, servletResponse);
	                    return;
	                }
            	}
            	
            	String refreshSessionKey = AdminSession.getRefreshToken(httpRequest);
                if (!StringUtils.isEmpty(refreshSessionKey)) {
                	LoginService loginService = ApplicationContextProvider.getApplicationContext().getBean(LoginService.class);
                	RefreshTokenResponse refreshToken = loginService.refreshTokenAdmin(refreshSessionKey);
                	if (refreshToken != null) {
                		AdminSession.setSession(httpRequest, refreshToken.getSessionKey(), refreshToken.getRefreshToken());
                		for (String className : classNames.split(",")) {
        	                String cacheKey = className + "." + refreshToken.getSessionKey();
        	                T session = sessionManager.get(cacheKey);
        	                if (session != null) {
        	                    AdminSession.setAttribute(httpRequest, refreshToken.getSessionKey(), session);
        	                    filterChain.doFilter(httpRequest, servletResponse);
        	                    return;
        	                }
                    	}
                	}
                }
            	
            }

//            logger.info("redirect uri: " + uri);
            HttpServletResponse response = ((HttpServletResponse)servletResponse);
            response.sendRedirect(redirectPath);
        }
    }

    @Override
    public void destroy() {

    }
}
