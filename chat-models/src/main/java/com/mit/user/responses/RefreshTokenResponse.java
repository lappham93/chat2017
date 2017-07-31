package com.mit.user.responses;

/**
 * Created by Hung Le on 2/20/17.
 */
public class RefreshTokenResponse {
    private String refreshToken;
    private String sessionKey;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
