package com.mit.http;

/**
 * Created by Hung Le on 2/13/17.
 */
public interface RequestVerificationManager {

    boolean isAllowRequest(String apiKey, String data, String sign);

    public String getAppName(String apiKey);
}
