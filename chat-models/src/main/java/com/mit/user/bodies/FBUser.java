package com.mit.user.bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mit.user.entities.SocialProfile;
import com.mit.user.enums.Gender;
import com.mit.user.enums.SocialType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Hung Le on 2/20/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class FBUser {
    private String id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private FBPicture picture;
    private String birthday;
    private String gender;

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

    public FBPicture getPicture() {
        return picture;
    }

    public void setPicture(FBPicture picture) {
        this.picture = picture;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public SocialProfile toSocialProfile() {
        SocialProfile socialProfile = new SocialProfile();
        socialProfile.setId(getId());
        socialProfile.setType(SocialType.FACEBOOK.getType());
        socialProfile.setFirstName(getFirstName());
        socialProfile.setLastName(getLastName());

        if (getPicture() != null && getPicture().getData() != null && !StringUtils.isEmpty(getPicture().getData().getUrl())) {
            socialProfile.setAvatar(getPicture().getData().getUrl());
        }

        if (!StringUtils.isEmpty(getBirthday())) {
            socialProfile.setBirthday(birthday.replace("/", "-"));
        }

        int gender = 0;
        if (getGender().equals("male")) {
            gender = Gender.MALE.getValue();
        } else if (getGender().equals("female")) {
            gender = Gender.FEMALE.getValue();
        }
        socialProfile.setGender(gender);

        return socialProfile;
    }
}
