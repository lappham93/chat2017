package com.mit.app;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * Created by Hung Le on 2/13/17.
 */
public class MyUrlRewriteFilter extends UrlRewriteFilter {

    private static final String CONFIG_LOCATION = "classpath:urlrewriter.xml";


    //Override the loadUrlRewriter method, and write your own implementation
    @Override
    protected void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
        try {
            //Create a UrlRewrite Conf object with the injected resource
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(CONFIG_LOCATION);
            String content = IOUtils.toString(resource.getInputStream());
            System.out.println("the content of resources:" + content);

            Conf conf = new Conf(filterConfig.getServletContext(), resource.getInputStream(), resource.getFilename(), "@@yourOwnSystemId@@");
            checkConf(conf);
        } catch (Exception ex) {
            throw new ServletException("Unable to load URL rewrite configuration file from " + CONFIG_LOCATION, ex);
        }
    }
}
