package com.mit.message.responses;

import com.mit.message.entities.LastMessage;
import com.mit.message.entities.Message;
import com.mit.user.entities.Profile;
import com.mit.user.responses.ProfileShortResponse;

import java.util.Date;

public class LastMessageResponse {

	private long id;
	private int type;
	private ProfileShortResponse other;
	private String content;
	private int contentType;
	private int newCount;
	private int status;
	private int userStatus;
	private Date createdDate;
	private Date updatedDate;

	public LastMessageResponse(Message message, LastMessage lastMessage, Profile profile, int userStatus) {
		id = message.getId();
		other = new ProfileShortResponse(profile);
		content = message.getContent();
		contentType = message.getContentType();
//		isRead = profile.getId() ==lastMessage.getUnreadRefId();
		if (lastMessage.getRefId() != profile.getId()) {
			newCount = lastMessage.getNewCount();
		}
		status = lastMessage.getStatus();
		createdDate = message.getCreatedDate();
		updatedDate = message.getUpdatedDate();
		this.userStatus = userStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ProfileShortResponse getOther() {
		return other;
	}

	public void setOther(ProfileShortResponse other) {
		this.other = other;
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

	public int getNewCount() {
		return newCount;
	}

	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
}
