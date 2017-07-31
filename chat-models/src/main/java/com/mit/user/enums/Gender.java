package com.mit.user.enums;

/**
 * Created by Hung Le on 2/20/17.
 */
public enum Gender {
    MALE(1), FEMALE(2);

    private int value;

    Gender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
