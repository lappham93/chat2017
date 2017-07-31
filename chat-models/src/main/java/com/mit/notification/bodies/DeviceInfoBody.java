package com.mit.notification.bodies;

import com.mit.notification.entities.DeviceInfo;
import com.mit.notification.entities.Display;

/**
 * Created by Hung Le on 4/5/17.
 */

public class DeviceInfoBody {

    private String id;
    private String model;
    private Display display;
    private String os;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public DeviceInfo toDeviceInfo() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(getId());
        deviceInfo.setModel(getModel());
        deviceInfo.setDisplay(getDisplay());
        deviceInfo.setOs(getOs());
        deviceInfo.setToken(getToken());

        return deviceInfo;
    }
}
