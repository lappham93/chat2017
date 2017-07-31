package com.mit.user.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Created by Hung Le on 2/15/17.
 */
@JsonFormat(shape = Shape.OBJECT)
public enum SocialType {
    ACCOUNT_KIT(1), FACEBOOK(2), GOOGLE(3);

    private int type;

    SocialType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static SocialType getByType(int type) {
        for (SocialType scType : SocialType.values()) {
            if (scType.getType() == type) {
                return scType;
            }
        }
        return null;
    }
}
