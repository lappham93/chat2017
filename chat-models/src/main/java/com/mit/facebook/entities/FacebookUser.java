package com.mit.facebook.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mit.common.entities.ActiveTimeDoc;

@Document(collection = "facebook_user")
public class FacebookUser extends ActiveTimeDoc<Long>{
	@Id
	private long id;
	private long userId;
	private String facebookId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

}
