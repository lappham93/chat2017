package com.mit.notification.entities;

import com.mit.notification.enums.NewsType;

import java.util.HashMap;
import java.util.Map;


public class MessageNews extends News {
	public static final int TYPE = NewsType.MESSAGE.getValue();

	private String msg;
	private long thumb;

	public MessageNews() {
		super(TYPE);
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

		return data;
	}
}
