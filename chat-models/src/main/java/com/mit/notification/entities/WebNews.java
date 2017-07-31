package com.mit.notification.entities;

import com.mit.notification.enums.NewsType;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


public class WebNews extends News {	
	public static final int TYPE = NewsType.WEB.getValue();
	
	private String url;
	private String msg;
	private long thumb;
	
	public WebNews() {
		super(TYPE);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		String url;
		try {
			url = URLDecoder.decode(getUrl(), "UTF-8");
		} catch (Exception e) {
			url = getUrl();
		}
		data.put("url", url);

		return data;
	}
}
