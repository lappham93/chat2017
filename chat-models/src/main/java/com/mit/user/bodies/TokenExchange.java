package com.mit.user.bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hung Le on 2/15/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenExchange {

    @JsonProperty("id")
    private String id;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_refresh_interval_sec")
    private int tokenRefeshInterval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getTokenRefeshInterval() {
        return tokenRefeshInterval;
    }

    public void setTokenRefeshInterval(int tokenRefeshInterval) {
        this.tokenRefeshInterval = tokenRefeshInterval;
    }
}
