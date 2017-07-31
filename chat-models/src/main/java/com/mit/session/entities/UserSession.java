
package com.mit.session.entities;

import java.io.Serializable;

/**
 * Created by Hung Le on 2/13/17.
 */
public class UserSession implements Serializable {
    private long userId;
    private int appType;
    private int profileType;

    public UserSession() {
        super();
    }

    public UserSession(long userId) {
        super();
        this.userId = userId;
    }

    public UserSession(long userId, int profileType) {
        super();
        this.userId = userId;
        this.profileType = profileType;
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

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }
    
}
