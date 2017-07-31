package com.mit.message.bodies;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public class DeleteUserBody {

    private long fromUserId;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Long> toUserIds;

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public List<Long> getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(List<Long> toUserIds) {
        this.toUserIds = toUserIds;
    }
}
