package com.mit.user.responses;

import com.mit.common.enums.ObjectType;
import com.mit.user.entities.Speaker;
import com.mit.utils.LinkBuilder;

public class SpeakerResponse {
	private long id;
	private String name;
	private String job;
	private String sumStory;
	private String photo;

	public SpeakerResponse(Speaker speaker) {
		this.id = speaker.getId();
		this.name = speaker.getFullName();
		this.job = speaker.getJob();
		this.sumStory = speaker.getSumStory();
		this.photo = LinkBuilder.buildPhotoLink(speaker.getPhoto(), ObjectType.USER.getLowerName());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
