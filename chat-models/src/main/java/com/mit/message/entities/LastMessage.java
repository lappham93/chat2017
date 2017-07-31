package com.mit.message.entities;

import com.mit.common.entities.TimeDoc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "last_message")
public class LastMessage extends TimeDoc<Long> {
    @Id
    private long id;
    @Indexed
    private List<Long> refIds;
    @Indexed
    private long messageId;
    private long refId;
    private int newCount;
    private int status;
    private List<Long> hideRefIds;
    
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<Long> getRefIds() {
		return refIds;
	}
	
	public void setRefIds(List<Long> refIds) {
		this.refIds = refIds;
	}
	
	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getRefId() {
		return refId;
	}

	public void setRefId(long refId) {
		this.refId = refId;
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

	public List<Long> getHideRefIds() {
		return hideRefIds;
	}

	public void setHideRefIds(List<Long> hideRefIds) {
		this.hideRefIds = hideRefIds;
	}
}
