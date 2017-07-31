package com.mit.user.entities;

import com.mit.address.entities.Address;
import com.mit.common.entities.TimeDoc;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Hung Le on 2/13/17.
 */

@Document(collection = "profile")
public class Profile extends TimeDoc<Long> {
    @Id
    private long id;
    private long avatar;
    private long cover;
    private int profileType;
    private int status;
    private String firstName;
    private String lastName;
    private int gender;
    private String birthDay;
    private String timeZone;
    private Address address;
    private String phone;
    private String email;
    private List<Long> banUserIds;
    private Date lastOnline;
    private boolean disableNotifyChat;
    private List<Long> interestCategoryIds;
    private Set<Long> interestEventIds;

    public Profile() {
    }

    public Profile(int profileType, String firstName, String lastName, int gender, String birthDay,
                   String timeZone, Address address, String phone, List<Double> location) {
        this.profileType = profileType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDay = birthDay;
        this.timeZone = timeZone;
        this.address = address;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAvatar() {
        return avatar;
    }

    public void setAvatar(long avatar) {
        this.avatar = avatar;
    }

    public long getCover() {
        return cover;
    }

    public void setCover(long cover) {
        this.cover = cover;
    }

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<Long> getBanUserIds() {
        return banUserIds;
    }

    public void setBanUserIds(List<Long> banUserIds) {
        this.banUserIds = banUserIds;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isDisableNotifyChat() {
        return disableNotifyChat;
    }

    public void setDisableNotifyChat(boolean disableNotifyChat) {
        this.disableNotifyChat = disableNotifyChat;
    }

    public List<Long> getInterestCategoryIds() {
        return interestCategoryIds;
    }

    public void setInterestCategoryIds(List<Long> interestCategoryIds) {
        this.interestCategoryIds = interestCategoryIds;
    }

    public Set<Long> getInterestEventIds() {
        return interestEventIds;
    }

    public void setInterestEventIds(Set<Long> interestEventIds) {
        this.interestEventIds = interestEventIds;
    }
}
