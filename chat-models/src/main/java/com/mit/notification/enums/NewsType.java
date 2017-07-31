package com.mit.notification.enums;

/**
 * Created by Hung Le on 4/4/17.
 */
public enum NewsType {
	WEB(1), WELCOME(4), PRODUCT(5), MESSAGE(6), SOCIAL(7), CHAT(8);

    private int value;

    NewsType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
