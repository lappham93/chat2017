package com.mit.app.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Hung Le on 2/13/17.
 */
@Document(collection = "app_key")
public class AppKey {
    @Id
    private String apiKey;
    private String secrectKey;
    private String appName;
    private String platform;
    private boolean isLocal;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecrectKey() {
        return secrectKey;
    }

    public void setSecrectKey(String secrectKey) {
        this.secrectKey = secrectKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}
