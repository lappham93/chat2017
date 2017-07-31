package com.mit.user.bodies;

/**
 * Created by Hung Le on 2/20/17.
 */
public class RefreshTokenBody {
    private String refreshToken;
    private double lon;
    private double lat;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
