package com.mit.responses;

import com.mit.common.enums.ObjectType;
import com.mit.user.entities.Profile;
import com.mit.user.entities.UserSocial;
import com.mit.user.enums.ProfileType;
import com.mit.user.enums.SocialType;
import com.mit.user.enums.UserStatus;
import com.mit.utils.AdminUtils;
import com.mit.utils.LinkBuilder;
import com.mit.utils.MIdNoise;

public class CustomerResponse extends ActiveTime{
	private String id;
	private String avatar;
	private String firstName;
	private String lastName;
	private String birthDay;
	private String homeTown;
	private String phone;
	private String email;
	private ProfileType type;
	private SocialType loginType;
	private UserStatus status;
	private boolean isActive;
	private boolean isBan;

	public CustomerResponse(Profile profile, UserSocial userSocial) {
		this.id = MIdNoise.enNoiseLId(profile.getId());
		this.avatar = LinkBuilder.buildPhotoLink(profile.getAvatar(), ObjectType.USER.getLowerName());
		this.firstName = profile.getFirstName();
		this.lastName = profile.getLastName();
		this.birthDay = profile.getBirthDay();
		if (profile.getAddress() != null) {
			this.homeTown = profile.getAddress().getCity();
		}
		this.phone = profile.getPhone();
		this.email = profile.getEmail();
		this.type = ProfileType.getProfileType(profile.getProfileType());
		this.status = UserStatus.getStatus(profile.getStatus());
		this.createdDate = AdminUtils.date2String(profile.getCreatedDate());
		this.updatedDate = AdminUtils.date2String(profile.getUpdatedDate());
		this.isActive = profile.getStatus() == UserStatus.ACTIVE.getValue();
		this.isBan = profile.getStatus() == UserStatus.BAN.getValue();
		if (userSocial != null) {
			this.loginType = SocialType.getByType(userSocial.getType());
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getHomeTown() {
		return homeTown;
	}

	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ProfileType getType() {
		return type;
	}

	public void setType(ProfileType type) {
		this.type = type;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public SocialType getLoginType() {
		return loginType;
	}

	public void setLoginType(SocialType loginType) {
		this.loginType = loginType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isBan() {
		return isBan;
	}

	public void setBan(boolean isBan) {
		this.isBan = isBan;
	}

}
