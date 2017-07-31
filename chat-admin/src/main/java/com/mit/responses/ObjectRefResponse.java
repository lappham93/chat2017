package com.mit.responses;

import com.mit.common.entities.ObjectRef;
import com.mit.common.enums.ObjectType;

public class ObjectRefResponse {
	private ObjectType type;
	private Object object;

	public ObjectRefResponse(ObjectRef objectRef, Object object) {
		this.type = ObjectType.getType(objectRef.getType());
		this.object = object;
	}

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
