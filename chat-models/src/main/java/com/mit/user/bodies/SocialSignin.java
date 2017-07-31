package com.mit.user.bodies;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Hung Le on 2/15/17.
 */

@ApiModel
public class SocialSignin {
    private String token;
    private int type;
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
