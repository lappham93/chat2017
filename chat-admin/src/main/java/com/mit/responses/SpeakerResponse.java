package com.mit.responses;

import com.mit.common.enums.ObjectType;
import com.mit.user.entities.Speaker;
import com.mit.utils.AdminUtils;
import com.mit.utils.LinkBuilder;
import com.mit.utils.MIdNoise;

public class SpeakerResponse extends ActiveTime {
	private String id;
	private String firstName;
	private String lastName;
	private String job;
	private String sumStory;
	private String photo;

	public SpeakerResponse(Speaker product) {
		this.id = MIdNoise.enNoiseLId(product.getId());
		this.firstName = product.getFirstName();
		this.lastName = product.getLastName();
		this.job = product.getJob();
		this.sumStory = product.getSumStory();
		this.photo = LinkBuilder.buildPhotoLink(product.getPhoto(), ObjectType.USER.getLowerName());
		this.isActive = product.isActive();
		this.updatedDate = AdminUtils.date2String(product.getUpdatedDate());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getSumStory() {
		return sumStory;
	}

	public void setSumStory(String sumStory) {
		this.sumStory = sumStory;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
