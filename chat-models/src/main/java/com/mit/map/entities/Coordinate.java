package com.mit.map.entities;

public class Coordinate {
	private double lon;
	private double lat;

	public Coordinate() {
		super();
	}

	public Coordinate(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
	}

	public Coordinate(org.springframework.data.geo.Point point) {
		this.lon = point.getX();
		this.lat = point.getY();
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
