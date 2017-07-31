package com.mit.user.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mit.user.enums.ProfileType;

@Document(collection = "admin_profile")
public class AdminProfile extends Profile {
	private int adminType;
	public boolean isDeleted;
	
	public AdminProfile(int adminType) {
		super();
		this.adminType = adminType;
		this.setProfileType(ProfileType.ADMIN.getValue());
	}
	
	public boolean isActive() {
		return getStatus() == 1 && isDeleted == false;
	}

	public int getAdminType() {
		return adminType;
	}

	public void setAdminType(int adminType) {
		this.adminType = adminType;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
