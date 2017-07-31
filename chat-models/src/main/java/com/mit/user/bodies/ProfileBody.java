package com.mit.user.bodies;

import org.apache.commons.lang3.StringUtils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mit.address.entities.Address;
import com.mit.user.entities.Profile;
import com.mit.user.enums.Gender;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ProfileBody {
	private String firstName;
	private String lastName;
	private int gender;
	private String birthDay;
	private Address address;
	private String email;
	private String homeTown;
	private String phone;
	private String countryCode;
	private String timeZone;
	private Boolean disableNotifyChat;

	public void updateProfile(Profile profile) {
		if (!StringUtils.isEmpty(firstName)) {
			profile.setFirstName(firstName);
		}
		if (!StringUtils.isEmpty(lastName)) {
			profile.setLastName(lastName);
		}
		if (gender == Gender.MALE.getValue() || gender == Gender.FEMALE.getValue()) {
			profile.setGender(gender);
		}
		if (!StringUtils.isEmpty(birthDay)) {
			profile.setBirthDay(birthDay);
		}
		if (address != null) {
			profile.setAddress(address);
		}
		if (!StringUtils.isEmpty(email)) {
			profile.setEmail(email);
		}
		if (!StringUtils.isEmpty(homeTown)) {
			Address address = profile.getAddress();
			if (address == null) {
				address = new Address();
			}
			address.setCity(homeTown);
			profile.setAddress(address);
		}
		if (!StringUtils.isEmpty(phone)) {
			String phoneNumber = countryCode + phone;
	        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
	        try {
		        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, null);
		        profile.setPhone(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
	        } catch (Exception e) {
	        	profile.setPhone(phoneNumber);
	        }
		}
		if (!StringUtils.isEmpty(timeZone)) {
			profile.setTimeZone(timeZone);
		}
		if (disableNotifyChat != null) {
			profile.setDisableNotifyChat(disableNotifyChat);
		}
	}
	
	@ApiModelProperty()
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@ApiModelProperty()
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ApiModelProperty(value = "MALE(1), FEMALE(2)")
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	@ApiModelProperty()
	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	@ApiModelProperty()
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@ApiModelProperty()
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Boolean getDisableNotifyChat() {
		return disableNotifyChat;
	}

	public void setDisableNotifyChat(Boolean disableNotifyChat) {
		this.disableNotifyChat = disableNotifyChat;
	}
}
