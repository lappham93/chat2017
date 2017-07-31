package com.mit.message.responses;

import com.mit.message.entities.Message;
import com.mit.message.enums.MessageType;
import com.mit.session.entities.ProfileCache;
import com.mit.user.entities.Profile;
import com.mit.user.responses.ProfileShortResponse;

import java.util.Date;

public class MessageResponse {

    private long id;
	private int type;
    private ProfileShortResponse fromUser;
	private ProfileShortResponse toUser;
    private String content;
	private int contentType;
    private Date createdDate;
    private Date updatedDate;
    private int status;

	public MessageResponse(Message message) {
		id = message.getId();
		type = MessageType.MESSAGE.getValue();
		fromUser = new ProfileShortResponse(message.getRefIds().get(0));
		toUser = new ProfileShortResponse(message.getRefIds().get(1));
		content = message.getContent();
		contentType = message.getContentType();
		status = message.getStatus();
		createdDate = message.getCreatedDate();
		updatedDate = message.getUpdatedDate();
	}

    public MessageResponse(Message message, Profile fromUser, Profile toUser) {
        id = message.getId();
		type = MessageType.MESSAGE.getValue();
		if (fromUser != null) {
			this.fromUser = new ProfileShortResponse(fromUser);
		}
		if (toUser != null) {
			this.toUser = new ProfileShortResponse(toUser);
		}
        content = message.getContent();
		contentType = message.getContentType();
		status = message.getStatus();
        createdDate = message.getCreatedDate();
        updatedDate = message.getUpdatedDate();
    }

	public MessageResponse(Message message, ProfileCache fromUser, ProfileCache toUser) {
		id = message.getId();
		type = MessageType.MESSAGE.getValue();
		if (fromUser != null) {
			this.fromUser = new ProfileShortResponse(message.getRefIds().get(0), fromUser);
		} else {
			this.fromUser = new ProfileShortResponse(message.getRefIds().get(0));
		}
		if (toUser != null) {
			this.toUser = new ProfileShortResponse(message.getRefIds().get(1), toUser);
		} else {
			this.toUser = new ProfileShortResponse(message.getRefIds().get(1));
		}
		content = message.getContent();
		contentType = message.getContentType();
		status = message.getStatus();
		createdDate = message.getCreatedDate();
		updatedDate = message.getUpdatedDate();
	}

	public MessageResponse(int type, Date createdDate) {
		this.type = type;
		this.createdDate = createdDate;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
