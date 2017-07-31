package com.mit.user.responses;

import com.mit.user.entities.SocialProfile;
import com.mit.user.entities.UserSocial;

/**
 * Created by Hung Le on 2/20/17.
 */
public class SocialProfileResponse {
    private String id;
    private int type;
    private String firstName = "";
    private String lastName = "";
    private String birthday = "";
    private String avatar = "";
    private String email = "";
    private String phone = "";
    private String countryCode = "";
    private int gender;

    public SocialProfileResponse(){}
    
    public SocialProfileResponse(SocialProfile socialProfile) {
        id = socialProfile.getId();
        type = socialProfile.getType();
        firstName = socialProfile.getFirstName();
        lastName = socialProfile.getLastName();
        birthday = socialProfile.getBirthday();
        avatar = socialProfile.getAvatar();
        email = socialProfile.getEmail();
        phone = socialProfile.getPhone();
        countryCode = socialProfile.getCountryCode();
        gender = socialProfile.getGender();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public UserSocial toUserSocial() {
        UserSocial userSocial = new UserSocial();

        userSocial.setSocialId(getId());
        userSocial.setType(getType());

        return userSocial;
    }
}
