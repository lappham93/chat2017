package com.mit.user.enums;

public enum ProfileType {
	ADMIN(1), CLIENT(2), PROVIDER(3), BOTH(4);
	
	private int value;
	
	private ProfileType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ProfileType getProfileType(int value) {
		for (ProfileType type : ProfileType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return CLIENT;
	}
}
