package com.mit.bodies;

import com.mit.user.entities.Speaker;
import com.mit.utils.AdminConstant;

public class SpeakerBody {
	private String firstName;
	private String lastName;
	private String job;
	private String sumStory;
	private long photo;
	private boolean isActive;
	private int updateType;

	public Speaker toSpeaker() {
		Speaker speaker = new Speaker();
		speaker.setPhoto(photo);
		updateType = AdminConstant.updateType.INFO.getValue();
		updateSpeaker(speaker);
		return speaker;
	}

	public void updateSpeaker(Speaker speaker) {
		if (speaker == null) {
			return;
		}
		if (updateType == AdminConstant.updateType.INFO.getValue()) {
			speaker.setFirstName(firstName);
			speaker.setLastName(lastName);
			speaker.setJob(job);
			speaker.setSumStory(sumStory);
			speaker.setActive(isActive);
			speaker.setStatus(isActive ? 1 : 0);
		} else if (updateType == AdminConstant.updateType.PHOTO.getValue()) {
			speaker.setPhoto(photo);
		}
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

	public long getPhoto() {
		return photo;
	}

	public void setPhoto(long photo) {
		this.photo = photo;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getUpdateType() {
		return updateType;
	}

	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}

}
