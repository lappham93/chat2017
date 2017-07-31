package com.mit.message.responses;

/**
 * Created by Hung Le on 7/17/17.
 */
public class CheckBanUserResponse {
    private long userId;
    private boolean ban;

    public CheckBanUserResponse(long userId, boolean ban) {
        this.userId = userId;
        this.ban = ban;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }
}
