package com.mit.message.enums;

/**
 * Created by Hung Le on 6/26/17.
 */
public enum MessageStatus {
    DELETED(0), FAILED(1), SENDING(2), SENT(3), DELIVERED(4), SEEN(5), LOCAL_DELETED(6);
    private int value;

    MessageStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
