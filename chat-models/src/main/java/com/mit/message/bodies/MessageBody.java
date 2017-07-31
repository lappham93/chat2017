package com.mit.message.bodies;

import com.mit.message.entities.Message2;
import com.mit.message.enums.MessageStatus;

public class MessageBody {

    private long fromUserId;
    private long toUserId;
    private String content;
    private int contentType;

    public Message2 toMessage() {
    	Message2 message = new Message2();
    	message.setFromUserId(fromUserId);
    	message.setToUserId(toUserId);
    	message.setMessage(content);
    	message.setStatus(MessageStatus.SENDING.getValue());
    	
    	return message;
    }
    
    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
