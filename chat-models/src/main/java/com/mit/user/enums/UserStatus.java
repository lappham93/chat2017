package com.mit.user.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum UserStatus {
	INACTIVE(0, "Inactive"), ACTIVE(1, "Active"), BAN(2, "Ban"); 
	
	private int value;
	private String name;
	
	private UserStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static UserStatus getStatus(int value) {
		for (UserStatus us : UserStatus.values()) {
			if (us.getValue() == value) {
				return us;
			}
		}
		return INACTIVE;
	}
}
