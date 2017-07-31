package com.mit.notification.entities;

import com.mit.notification.enums.NewsType;

import java.util.HashMap;
import java.util.Map;


public class ChatNews extends News {
	public static final int TYPE = NewsType.CHAT.getValue();

	private long fromUserId;
	private String msg;
	private long thumb;

	public ChatNews() {
		super(TYPE);
	}

	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getThumb() {
		return thumb;
	}

	public void setThumb(long thumb) {
		this.thumb = thumb;
	}

	@Override
	public Map<String, String> getData() {
		Map<String, String> data = new HashMap<>();
		data.put("fromUserId", getFromUserId() + "");
		data.put("createdDate", getCreatedDate().getTime() + "");

		return data;
	}
}
