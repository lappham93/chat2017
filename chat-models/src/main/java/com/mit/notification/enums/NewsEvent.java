package com.mit.notification.enums;

/**
 * Created by Hung Le on 4/5/17.
 */
public enum NewsEvent {
    NONE(0), REGISTER(1);

    private int value;

    NewsEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
