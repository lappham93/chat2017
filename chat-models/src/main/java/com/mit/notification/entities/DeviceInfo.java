package com.mit.notification.entities;

import com.mit.common.entities.TimeDoc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Hung Le on 4/4/17.
 */

@Document(collection = "device_info")
public class DeviceInfo extends TimeDoc<String> {

    @Id
    private String id;
    private long userId;
    private int appType;
    private String model;
    private Display display;
    private String os;
    private String token;

    public static String buildId(String deviceId, int appType) {
    	return deviceId + appType;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
