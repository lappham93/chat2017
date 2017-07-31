package com.mit.http.filter;

import com.mit.http.ApiResponse;
import com.mit.http.RequestLogging;
import com.mit.http.exception.TokenInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Hung Le on 2/13/17.
 */
public class LoggingFilter implements Filter {
    Logger logger = LoggerFactory.getLogger("requestLogging");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LoggingHttpRequestWrapper httpRequest = new LoggingHttpRequestWrapper((HttpServletRequest) servletRequest);
        if (httpRequest.getRequestURI().equals("/favicon.ico")) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_OK);
            return;
        }
        long startTime = System.nanoTime();
        RequestLogging logging = new RequestLogging(httpRequest);
        LoggingHttpServletResponseWrapper responseWrapper = new LoggingHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        try {
            filterChain.doFilter(httpRequest, responseWrapper);
        } catch (Exception e) {
            Throwable root = e.getCause();
            if (root instanceof TokenInvalidException) {
                ApiResponse response = new ApiResponse(-1, root.getMessage());
                responseWrapper.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseWrapper.getOutputStream().print(response.toString());
            } else {
                logger.error("request error ", e);
                responseWrapper.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                responseWrapper.getOutputStream().print("500 Internal Server Error");
            }
        }
        servletResponse.getWriter().write(responseWrapper.getContent());
        logging.setResponse(responseWrapper.getContent());
        logging.setStatus(responseWrapper.getStatus());
        logging.setSpendTime((int)(System.nanoTime() - startTime)/1000);

        logger.info(logging.toString());

    }

    @Override
    public void destroy() {

    }
}
