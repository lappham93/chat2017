package com.mit.message.enums;

/**
 * Created by Hung Le on 6/26/17.
 */
public enum UserStatus {
    OFFLINE(0), ONLINE(1);
    private int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
