package com.mit.map.entities;

import java.util.List;

public class Tile {
	private List<Coordinate> points;
	private double square;
	private double parentSquare;

	public Tile() {
		super();
	}

	public Tile(List<Coordinate> points, double square) {
		this.points = points;
		this.square = square;
	}

	public List<Coordinate> getPoints() {
		return points;
	}

	public void setPoints(List<Coordinate> points) {
		this.points = points;
	}

	public double getSquare() {
		return square;
	}

	public void setSquare(double square) {
		this.square = square;
	}

	public double getParentSquare() {
		return parentSquare;
	}

	public void setParentSquare(double parentSquare) {
		this.parentSquare = parentSquare;
	}

}
