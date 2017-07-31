package com.mit.message.bodies;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

/**
 * Created by Hung on 6/24/2017.
 */
public class MessageStatusBody {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Long> ids;
    private int status;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
