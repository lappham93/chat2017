package com.mit.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Hung Le on 2/13/17.
 */
public class IpUtils {
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    public static boolean isLANNetwork(String ipAddress) {
        SubnetUtils subnet10 = new SubnetUtils("10.0.0.0", "255.0.0.0");
        if (ipAddress.startsWith("192.168") || ipAddress.startsWith("172.16") || ipAddress.startsWith("192.168")) {
            return true;
        }

        return false;
    }
}
