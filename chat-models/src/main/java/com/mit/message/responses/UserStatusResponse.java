package com.mit.message.responses;

import java.util.Date;

/**
 * Created by Hung Le on 7/12/17.
 */
public class UserStatusResponse {
    private long userId;
    private int status;
    private Date lastOnline;
    private boolean ban;

    public UserStatusResponse(long userId, int status) {
        this.userId = userId;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }
}
