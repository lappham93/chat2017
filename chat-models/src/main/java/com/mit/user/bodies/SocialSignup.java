package com.mit.user.bodies;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mit.address.entities.Address;
import com.mit.user.entities.Profile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Hung Le on 2/20/17.
 */

@ApiModel
public class SocialSignup {
    private String token;
    private int type;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String countryCode;
    private double lon;
    private double lat;

    @ApiModelProperty(required = true)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @ApiModelProperty(value = "ACCOUNT_KIT=1, FACEBOOK=2, GOOGLE=3", required = true)
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

	public Profile toProfile() {
        Profile profile = new Profile();
        profile.setFirstName(getFirstName());
        profile.setLastName(getLastName());
        profile.setEmail(getEmail());
        String phoneNumber = getCountryCode() + getPhone();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
	        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, null);
	        profile.setPhone(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
        } catch (Exception e) {
        	profile.setPhone(phoneNumber);
        }
        

        Address address = new Address();
//        address.setCountryCode(getCountryCode());
        profile.setAddress(address);
        return profile;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
