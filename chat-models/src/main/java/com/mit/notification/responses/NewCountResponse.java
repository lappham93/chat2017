package com.mit.notification.responses;

/**
 * Created by Hung Le on 4/5/17.
 */
public class NewCountResponse {
    private int newCount;

    public NewCountResponse(int newCount) {
        this.newCount = newCount;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }
}
