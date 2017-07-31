package com.mit.message.bodies;

/**
 * Created by Hung on 6/24/2017.
 */
public class GetUserMessageBody {
    private long toUserId;
    private long messageId;
    private int count;
    private boolean newer;

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public boolean isNewer() {
        return newer;
    }

    public void setNewer(boolean newer) {
        this.newer = newer;
    }
}
