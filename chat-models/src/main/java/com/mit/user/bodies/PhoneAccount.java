package com.mit.user.bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * Created by Hung Le on 2/15/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneAccount {

    private String id;
    private Map<String, String> phone;
    private Map<String, String> email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getPhone() {
        return phone;
    }

    public void setPhone(Map<String, String> phone) {
        this.phone = phone;
    }

    public Map<String, String> getEmail() {
        return email;
    }

    public void setEmail(Map<String, String> email) {
        this.email = email;
    }
}
