package com.mit.user.responses;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mit.address.entities.Address;
import com.mit.common.enums.ObjectType;
import com.mit.user.entities.Profile;
import com.mit.utils.LinkBuilder;

/**
 * Created by Hung Le on 2/16/17.
 */
public class ProfileResponse {
    private long id;
    private int profileType;
    private String avatar;
    private String cover;
    private String firstName;
    private String lastName;
    private String displayName;
    private int gender;
    private String birthDay;
    private String timeZone;
    private Address address;
    private String homeTown;
    private String phone;
    private String nationalNumber;
    private String countryCode;
    private String email;
    private int status;
    private boolean disableNotifyChat;
    
    public ProfileResponse() {}

    public ProfileResponse(Profile profile) {
        id = profile.getId();
        profileType = profile.getProfileType();
        avatar = LinkBuilder.buildPhotoLink(profile.getAvatar(), ObjectType.USER.getLowerName());
        cover = LinkBuilder.buildPhotoLink(profile.getCover(), ObjectType.USER.getLowerName());
        firstName = profile.getFirstName();
        lastName = profile.getLastName();
        displayName = firstName + " "+ lastName;
        gender = profile.getGender();
        birthDay = profile.getBirthDay();
        timeZone = profile.getTimeZone();
        address = profile.getAddress();
        homeTown = address != null ? address.getCity() : "";
        phone = profile.getPhone();
        email = profile.getEmail();
        status = profile.getStatus();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
	        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phone, null);
	        nationalNumber = numberProto.getNationalNumber() + "";
	        countryCode = "+" + numberProto.getCountryCode();
        } catch (Exception e) {
        }
        disableNotifyChat = profile.isDisableNotifyChat();
    }

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public String getHomeTown() {
		return homeTown;
	}

	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}

	public String getNationalNumber() {
		return nationalNumber;
	}

	public void setNationalNumber(String nationalNumber) {
		this.nationalNumber = nationalNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

    public boolean isDisableNotifyChat() {
        return disableNotifyChat;
    }

    public void setDisableNotifyChat(boolean disableNotifyChat) {
        this.disableNotifyChat = disableNotifyChat;
    }
}
