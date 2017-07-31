package com.mit.user.responses;

import com.mit.common.enums.ObjectType;
import com.mit.user.entities.Profile;
import com.mit.user.entities.UserLocation;
import com.mit.utils.LinkBuilder;

/**
 * Created by Hung on 6/19/2017.
 */
public class UserLocationResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobilePhone;
    private String countryCode;
    private String photo;
    private double x;
    private double y;
    private double lat;
    private double lon;
    private double distance;

    public UserLocationResponse() {
    }

    public UserLocationResponse(UserLocation userLocation, Profile profile) {
        this.id = profile.getId();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.email = profile.getEmail();
        this.photo = LinkBuilder.buildPhotoLink(profile.getAvatar(), ObjectType.USER.getLowerName());
        this.x = userLocation.getX();
        this.y = userLocation.getY();
        this.lat = userLocation.getLat();
        this.lon = userLocation.getLon();
        this.distance = userLocation.getDistance();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
