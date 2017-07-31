package com.mit.http.filter;

import com.mit.http.RequestVerificationManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Hung Le on 2/13/17.
 */
public class VerificationFilter implements Filter {
    Logger logger = LoggerFactory.getLogger("requestLogging");
    private final int QUERY_DATA_LENGTH = 10;
    private RequestVerificationManager manager;

    public VerificationFilter(RequestVerificationManager manager) {
        this.manager = manager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        String apiKey = httpRequest.getHeader("Api-Key");
        String sign = httpRequest.getHeader("Signature");
        String requestTime = httpRequest.getHeader("Request-Time");

        String data = null;
        String queryString = httpRequest.getQueryString();
        if (queryString == null || queryString.length() == 0) {
            queryString = httpRequest.getRequestURI();
        }

        if (QUERY_DATA_LENGTH >= queryString.length()) {
            data = queryString;
        } else {
            data = StringUtils.right(queryString, QUERY_DATA_LENGTH);
        }

        data = requestTime + data;
        logger.info("data -- " + data + " -- sign  --- " + sign);
        if(manager.isAllowRequest(apiKey, data, sign)) {
            filterChain.doFilter(httpRequest, servletResponse);
        } else {
            HttpServletResponse response = ((HttpServletResponse)servletResponse);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().print("Bad Request");
            return;
        }

    }

    @Override
    public void destroy() {

    }
}
