package com.mit.http.filter;

import com.mit.http.session.SessionManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hung Le on 2/13/17.
 */public class AuthenticationWithMethodFilter<T> implements Filter {
    private final SessionManager<T> sessionManager;
    Map<String, List<String>> includes = new HashMap();
    private String className = "";

    public AuthenticationWithMethodFilter(SessionManager<T> sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String includeGets = filterConfig.getInitParameter("includeGets");
        String includeUpdates = filterConfig.getInitParameter("includeUpdates");
        className = filterConfig.getInitParameter("profileClass");
        if (includeGets != null && !includeGets.isEmpty()) {
            includes.put("GET", Arrays.asList(includeGets.split(",")));
        }

        if (includeUpdates != null && !includeUpdates.isEmpty()) {
            includes.put("UPDATE", Arrays.asList(includeUpdates.split(",")));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        String sessionKey = httpRequest.getHeader("Session");
        String uri = httpRequest.getRequestURI();

        boolean isAuthenticated = true;
        List<String> ex = getMethodExclude(httpRequest.getMethod());
        if (ex != null) {
            for (String exclude : ex) {
                if (uri.startsWith(exclude)) {
                    isAuthenticated = false;
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

    private List<String> getMethodExclude(String httpMethod) {
        if ("GET".equals(httpMethod)) {
            return includes.get("GET");
        } else if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "DELETE".equals(httpMethod)){
            return includes.get("UPDATE");
        } else {
            return null;
        }
    }

    @Override
    public void destroy() {

    }
}
