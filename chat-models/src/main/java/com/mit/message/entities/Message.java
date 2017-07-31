package com.mit.message.entities;

import com.mit.common.entities.TimeDoc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "message")
public class Message extends TimeDoc<Long> {
    @Id
    private long id;
    private long userId;
    @Indexed
    private List<Long> refIds;
    private String content;
	private int contentType;
	private List<Long> hideRefIds;
	private boolean isEdited;
	private int status;

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

	public List<Long> getHideRefIds() {
		return hideRefIds;
	}

	public void setHideRefIds(List<Long> hideRefIds) {
		this.hideRefIds = hideRefIds;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
