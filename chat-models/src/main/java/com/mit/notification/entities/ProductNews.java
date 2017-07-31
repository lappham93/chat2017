package com.mit.notification.entities;

import java.util.HashMap;
import java.util.Map;

import com.mit.notification.enums.NewsType;

public class ProductNews extends News {
	public static final int TYPE = NewsType.PRODUCT.getValue();

	private long productId;
	private String msg;
	private long thumb;

	public ProductNews() {
		super(TYPE);
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
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
		data.put("productId", productId + "");
		return data;
	}
}
