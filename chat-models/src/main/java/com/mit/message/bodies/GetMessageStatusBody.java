package com.mit.message.bodies;

import java.util.Date;

/**
 * Created by Hung on 6/24/2017.
 */
public class GetMessageStatusBody {
    private long messageId;
    private Date lastUpdate;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
