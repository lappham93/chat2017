package com.mit.user.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Hung Le on 2/15/17.
 */

@Document(collection = "user_social")
public class UserSocial {
    @Id
    private long id;
    private String socialId;
    private long userId;
    private int type;

    public UserSocial() {
    }

    public UserSocial(String socialId, long userId, int type) {
        this.socialId = socialId;
        this.userId = userId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
