package com.mit.user.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum AdminType {
	UNKNOW(0), SYSTEM_ADMIN(1), REGION_MANAGER(2);
	
	private int value;
	
	private AdminType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
