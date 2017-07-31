package com.mit.user.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mit.common.entities.ActiveTimeDoc;

@Document(collection = "speaker")
public class Speaker extends ActiveTimeDoc<Long> {
	@Id
	private long id;
	private String firstName;
	private String lastName;
	private String job;
	private String sumStory;
	private long photo;
	private int status;

	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
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

	public long getPhoto() {
		return photo;
	}

	public void setPhoto(long photo) {
		this.photo = photo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
