package com.mit.message.enums;

/**
 * Created by Hung on 7/14/2017.
 */
public enum MessageType {
    MESSAGE(1), TIMELINE(2);
    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
