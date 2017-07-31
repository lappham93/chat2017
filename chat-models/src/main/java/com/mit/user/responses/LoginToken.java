package com.mit.user.responses;

/**
 * Created by Hung Le on 2/15/17.
 */
public class LoginToken {

    private SocialProfileResponse socialProfile;
    private ProfileResponse profile;
    private String token;
    private String sessionKey;
    private long expireTime;
    private String refreshToken;
    private String apiSocket;
    private String uploadSocket;

    public SocialProfileResponse getSocialProfile() {
        return socialProfile;
    }

    public void setSocialProfile(SocialProfileResponse socialProfile) {
        this.socialProfile = socialProfile;
    }

    public ProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(ProfileResponse profile) {
        this.profile = profile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getApiSocket() {
        return apiSocket;
    }

    public void setApiSocket(String apiSocket) {
        this.apiSocket = apiSocket;
    }

    public String getUploadSocket() {
        return uploadSocket;
    }

    public void setUploadSocket(String uploadSocket) {
        this.uploadSocket = uploadSocket;
    }
}
