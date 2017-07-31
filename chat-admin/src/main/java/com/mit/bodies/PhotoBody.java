package com.mit.bodies;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PhotoBody {
	private MultipartFile[] photos;
	private int type;

	@ApiModelProperty(required = true)
	public MultipartFile[] getPhotos() {
		return photos;
	}

	public void setPhotos(MultipartFile[] photos) {
		this.photos = photos;
	}

	@ApiModelProperty(required = true)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
