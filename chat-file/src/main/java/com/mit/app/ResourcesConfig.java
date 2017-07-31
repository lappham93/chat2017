package com.mit.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ResourcesConfig extends WebMvcConfigurerAdapter {
	private final String termFilePath = "/load/terms";
	private final String resourcesPath = "/load/static";
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(termFilePath + "/**").addResourceLocations("classpath:terms/");
        registry.addResourceHandler(resourcesPath + "/**").addResourceLocations("classpath:static/");
    }
    //http://file.saocoo.com/saocoo/load/terms/PrivacyPolicy.html
    //http://file.saocoo.com/saocoo/load/terms/TermOfService.html
}