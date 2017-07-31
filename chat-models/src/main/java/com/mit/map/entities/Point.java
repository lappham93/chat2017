package com.mit.map.entities;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;

public class Point {
	private double x;
	private double y;

	public Point() {
		super();
	}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
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

	public Document toDocument() {
		return new Document("type", "Point").append("coordinates", Arrays.asList(x, y));
	}

	public static Point parseObject(Document doc) {
		Point point = null;
		if (doc != null) {
			List<Double> coordinates = (List<Double>) doc.get("coordinates");
			point = new Point(coordinates.get(0), coordinates.get(1));
		}
		return point;
	}

	public Point roundPoint() {
		double x = Math.round(this.x * 1E2) / 1E2;
		double y = Math.round(this.y * 1E2) / 1E2;
		return new Point(x, y);
	}

	public double dotProduct(Point p) {
		return x * p.getX() + y * p.getY();
	}

	public Point subtract(Point p) {
		return new Point(x - p.getX(), y - p.getY());
	}

	public double squareLength() {
		return x * x + y * y;
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public double length(Point p) {
		return subtract(p).length();
	}

	public Point multiply(double factor) {
		return new Point(factor * x, factor * y);
	}

	public Point multiply(double factorX, double factorY) {
		return new Point(factorX * x, factorY * y);
	}
}
