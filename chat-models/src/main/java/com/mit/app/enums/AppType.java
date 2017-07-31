package com.mit.app.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Created by Hung Le on 4/4/17.
 */
@JsonFormat(shape = Shape.OBJECT)
public enum AppType {
    CLIENT(1, "Client"), PROVIDER(2, "Provider");

    private int value;
    private String name;
    
    AppType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

	public String getName() {
		return name;
	}
    
}
