package com.mit.user.entities;

public class UserLocation {
	private long id;
	private double x;
	private double y;
	private double lat;
	private double lon;
	private double distance;
	
	public UserLocation() {
	}

	public UserLocation(long id, double lat, double lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}
	
	public UserLocation(long id, double x, double y, double lat, double lon, double distance) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.lat = lat;
		this.lon = lon;
		this.distance = distance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
