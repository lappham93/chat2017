package com.mit.user.bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Hung Le on 2/15/17.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class FBDebugTokenData {

    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("expires_at")
    private long expireTime;
    @JsonProperty("is_valid")
    private boolean isValid ;
    @JsonProperty("user_id")
    private String userId ;
    @JsonProperty("application")
    private String application;
    @JsonProperty("scopes")
    private List<String> scopes;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
}
