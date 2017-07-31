package com.mit.message.bodies;

import java.util.List;

/**
 * Created by Hung on 6/24/2017.
 */
public class GetUserStatusBody {
    private List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
