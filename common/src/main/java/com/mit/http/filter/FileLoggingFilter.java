package com.mit.http.filter;

import com.mit.http.RequestLogging;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hung Le on 2/13/17.
 */
public class FileLoggingFilter implements Filter {
    Logger logger = LoggerFactory.getLogger("requestLogging");
    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        long startTime = System.nanoTime();
        RequestLogging logging = new RequestLogging(httpRequest, true);
        if (ServletFileUpload.isMultipartContent(httpRequest)) {
            logger.info("isMultipartContent");
            try {
                // Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();
                // Configure a repository (to ensure a secure temp location is used)
                ServletContext servletContext = filterConfig.getServletContext();
                File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
                factory.setRepository(repository);
                logging.setBody("file");
                logging.setBodyLength((int)repository.length());

                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);

                // Parse the request
                List<FileItem> items = upload.parseRequest(httpRequest);
                Map<String, FileItem> itemMap = new HashMap<>();
                for (FileItem item: items) {
                    itemMap.put(item.getFieldName(), item);
                }
                servletRequest.setAttribute("fileItems", itemMap);
            } catch (Exception e) {
                logger.error("get file error ", e);
            }
        }

        LoggingHttpServletResponseWrapper responseWrapper = new LoggingHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        try {
            filterChain.doFilter(servletRequest, responseWrapper);
        } catch (Exception e) {
            logger.error("request error ", e);
            responseWrapper.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseWrapper.getOutputStream().print("500 Internal Server Error");
        }
        servletResponse.getOutputStream().write(responseWrapper.getContentAsBytes());
        logging.setResponse(responseWrapper.getContent());
        logging.setStatus(responseWrapper.getStatus());
        logging.setSpendTime((int)(System.nanoTime() - startTime)/1000);
        logger.info(logging.toString());

    }

    @Override
    public void destroy() {

    }
}
