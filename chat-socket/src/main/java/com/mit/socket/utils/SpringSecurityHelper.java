package com.mit.socket.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Hung Le on 3/13/17.
 */
public class SpringSecurityHelper {
    private static final Logger logger = LoggerFactory.getLogger(SpringSecurityHelper.class);

    public static void setAuthentication(Authentication authentication) {
        getSecurityContext().setAuthentication(authentication);
    }

    /**
     * Retrieves the Principal from the spring security context. Null if Principal is not logged in.
     *
     * @return Return value
     */
    public static UserDetails getUserDetails() {
        UserDetails result = null;
        SecurityContext sc     = SecurityContextHolder.getContext();

        if (sc != null) {
            Authentication authentication = sc.getAuthentication();

            if (authentication != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if ((principal != null) && (principal instanceof UserDetails)) {
                    result = (UserDetails) principal;
                } else {
                    if (logger.isWarnEnabled()) {
                        logger.warn("No Principal present in Authentication");
                    }
                }
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn("No Authentication present in SecurityContext");
                }
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("No SecurityContext present");
            }
        }

        return result;
    }

    public static Authentication getAuthentication() {
        Authentication result = null;
        SecurityContext sc     = SecurityContextHolder.getContext();

        if (sc != null) {
            result = sc.getAuthentication();
        }

        return result;
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
}
