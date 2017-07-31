package com.mit.message.responses;

import com.mit.message.entities.Message;
import com.mit.message.enums.MessageStatus;
import com.mit.user.responses.ProfileShortResponse;
import org.springframework.util.CollectionUtils;

/**
 * Created by Hung Le on 6/26/17.
 */
public class MessageStatusResponse {
    private long id;
    private ProfileShortResponse fromUser;
    private ProfileShortResponse toUser;
    private int status;

    public MessageStatusResponse(Message message) {
        id = message.getId();
        fromUser = new ProfileShortResponse(message.getRefIds().get(0));
        toUser = new ProfileShortResponse(message.getRefIds().get(1));
        status = message.getStatus();
    }

    public MessageStatusResponse(Message message, long refId) {
        id = message.getId();
        fromUser = new ProfileShortResponse(message.getRefIds().get(0));
        toUser = new ProfileShortResponse(message.getRefIds().get(1));
        if (!CollectionUtils.isEmpty(message.getHideRefIds()) && message.getHideRefIds().contains(refId)) {
            status = MessageStatus.LOCAL_DELETED.getValue();
        } else {
            status = message.getStatus();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProfileShortResponse getFromUser() {
        return fromUser;
    }

    public void setFromUser(ProfileShortResponse fromUser) {
        this.fromUser = fromUser;
    }

    public ProfileShortResponse getToUser() {
        return toUser;
    }

    public void setToUser(ProfileShortResponse toUser) {
        this.toUser = toUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
