package com.mit.notification.enums;

/**
 * Created by Hung Le on 4/4/17.
 */
public enum AccountNewsType {
    BECOME_PROVIDER(1);

    private int value;

    AccountNewsType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
