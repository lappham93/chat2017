package com.mit.http.filter;

import com.mit.http.session.SessionManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hung Le on 2/13/17.
 */
public class AuthenticationFilter<T> implements Filter {
	private Logger logger = LoggerFactory.getLogger("AuthenticationFilter");
    private final SessionManager<T> sessionManager;
    List<String> excludes = new ArrayList<>();
    private String className = "";

    public AuthenticationFilter(SessionManager<T> sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludePatterns = filterConfig.getInitParameter("excludePatterns");
        className = filterConfig.getInitParameter("profileClass");
        if (excludePatterns != null && !excludePatterns.isEmpty()) {
            excludes.addAll(Arrays.asList(excludePatterns.split(",")));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        String sessionKey = httpRequest.getHeader("Session");
        String uri = httpRequest.getRequestURI();
        logger.info("Session = " + sessionKey);
        
        boolean isAuthenticated = false;
        if (uri.equals("/")) {
            isAuthenticated = true;
        } else {
            for (String exclude : excludes) {
                if (uri.startsWith(exclude)) {
                    isAuthenticated = true;
                    break;
                }
            }
        }

        if (isAuthenticated) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if (sessionKey != null && !sessionKey.isEmpty()) {
                String cacheKey = className + "." + sessionKey;
                T session = sessionManager.get(cacheKey);

                if (session != null) {
                    httpRequest.setAttribute("session", session);
                    httpRequest.setAttribute("sessionKey", sessionKey);
                    filterChain.doFilter(httpRequest, servletResponse);
                    return;
                }
            }

            HttpServletResponse response = ((HttpServletResponse)servletResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("Unauthorized");
        }
    }

    @Override
    public void destroy() {

    }
}
