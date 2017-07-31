package com.mit.message.enums;

/**
 * Created by Hung on 7/14/2017.
 */
public enum MessageContentType {
    TEXT(1);
    private int value;

    MessageContentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
