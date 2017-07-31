package com.mit.responses;

import com.mit.common.enums.ObjectType;

public class ObjectTypeResponse {
	private int value;
	private String name;
	private ObjectType object;
	
	public ObjectTypeResponse(ObjectType object) {
		this.value = object.getValue();
		this.name = object.getName();
		this.object = object;
	}
	
	public ObjectTypeResponse(int value) {
		this.value = value;
		this.object = ObjectType.getType(value);
		this.name = object != null ? object.getName() : "";
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectType getObject() {
		return object;
	}

	public void setObject(ObjectType object) {
		this.object = object;
	}
	
	
}
