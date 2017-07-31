package com.mit.notification.enums;

/**
 * Created by Hung Le on 5/4/17.
 */
public enum SocialNewsType {
    LIKE_FEED(1, "liked your feed", "liked your friend's feed"),
    COMMENT(2, "commented on your feed", "also commented on this feed"),
    LIKE_COMMENT(3, "liked your comment", "liked your comment"),
    REPLY(4, "replied your comment", "replied your comment"),
    LIKE_REPLY(5, "liked your comment", "liked your comment"),
    POST_FEED(6, "posted a feed ", "posted a feed");

    private int value;
    private String message;
    private String omessage;

    private SocialNewsType(int value, String message, String omessage) {

        this.value = value;
        this.message = message;
        this.omessage = omessage;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getOtherMessage() {
        return omessage;
    }

    public static SocialNewsType getSocialNewsType(int value) {
        for (SocialNewsType type : SocialNewsType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
