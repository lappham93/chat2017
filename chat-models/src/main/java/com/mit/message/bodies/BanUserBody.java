package com.mit.message.bodies;

/**
 * Created by Hung on 6/24/2017.
 */
public class BanUserBody {
    private long userId;
    private boolean ban;

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
