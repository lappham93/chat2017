package com.mit.notification.responses;

import com.mit.common.enums.ObjectType;
import com.mit.notification.entities.ProductNews;
import com.mit.utils.LinkBuilder;

/**
 * Created by Hung Le on 4/4/17.
 */
public class ProductNewsResponse extends NewsResponse {
	private long productId;
	private String msg;
	private String thumb;

	public ProductNewsResponse(ProductNews productNews) {
		super(productNews);
		this.productId = productNews.getProductId();
		this.msg = productNews.getMsg();
		this.thumb = LinkBuilder.buildPhotoLink(productNews.getThumb(), ObjectType.PRODUCT.getLowerName());
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

}
