package com.mit.conts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Hung Le on 2/16/17.
 */
@Component
public class Common {
    public static String DOMAIN_APP;
    public static String DOMAIN_FILE;
    public static String DOMAIN_ADMIN;
    public static String DOMAIN_API_SOCKET;
    public static String DOMAIN_UPLOAD_SOCKET;
    public static String API_PREFIX;

    @Value("${system.domain}")
    public void setDomainApp(String domain) {
        DOMAIN_APP = domain;
    }

    @Value("${system.domain-file}")
    public void setDomainFile(String domain) {
        DOMAIN_FILE = domain;
    }

    @Value("${system.domain-admin}")
    public void setDomainAdmin(String domain) {
        DOMAIN_ADMIN = domain;
    }

    @Value("${system.domain-api-socket}")
    public void setDomainApiSocket(String domain) {
        DOMAIN_API_SOCKET = domain;
    }

    @Value("${system.domain-upload-socket}")
    public void setDomainUploadSocket(String domain) {
        DOMAIN_UPLOAD_SOCKET = domain;
    }

    @Value("${system.api-prefix}")
    public void setApiPrefix(String apiPrefix) {
        API_PREFIX = apiPrefix;
    }
}
