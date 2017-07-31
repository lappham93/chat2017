package com.mit.session.enums;

/**
 * Created by Hung Le on 2/24/17.
 */
public enum TokenType {
    ACCESS_TOKEN(1), REFRESH_TOKEN(2);

    private int value;

    TokenType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
