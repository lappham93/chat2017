package com.mit.asset.responses;

import com.mit.asset.entities.PhotoInfo;
import com.mit.common.enums.ObjectType;
import com.mit.utils.LinkBuilder;

public class PhotoResponse {
	private long id;
	private String link;
	private int width;
	private int height;

	public PhotoResponse(PhotoInfo photo) {
		this.id = photo.getPhotoId();
		this.link = LinkBuilder.buildPhotoLink(photo.getPhotoId(), ObjectType.getType(photo.getType()).getLowerName());
		this.width = photo.getWidth();
		this.height = photo.getHeight();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public long getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
