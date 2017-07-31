package com.mit.message.bodies;

/**
 * Created by Hung on 6/24/2017.
 */
public class GetMessageBody {
    private long messageId;
    private int count;
    private boolean newer;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isNewer() {
        return newer;
    }

    public void setNewer(boolean newer) {
        this.newer = newer;
    }
}
