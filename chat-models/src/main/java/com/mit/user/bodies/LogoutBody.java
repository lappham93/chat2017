package com.mit.user.bodies;

/**
 * Created by Hung Le on 3/21/17.
 */
public class LogoutBody {
    private String deviceId;
    private String refreshToken;
    private boolean logoutAllDevices;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isLogoutAllDevices() {
        return logoutAllDevices;
    }

    public void setLogoutAllDevices(boolean logoutAllDevices) {
        this.logoutAllDevices = logoutAllDevices;
    }
}
