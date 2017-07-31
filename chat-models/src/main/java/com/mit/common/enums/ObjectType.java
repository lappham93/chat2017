package com.mit.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum ObjectType {
	UNKNOW(0, "Unknow"), FACE(1, "Face"), PRODUCT(2, "Product"), COMMENT(3, "Comment"), FEED(5, "Feed"), SUBFEED(6, "SubFeed"), USER(7, "User"),
	BANNER(8, "Banner"), ADDRESS(10, "Address"), HOME(11, "Home"), NEWS(12, "News"), TEMP_FILE(13, "Temp File"), COVER(14, "Cover"), CHAT(15, "Chat");

	public static final String VALUE_DESC = "UNKNOW(0, \"Unknow\"), SERVICE(1, \"Service\"), FEED(2, \"Feed\"), " +
			"SUBFEED(3, \"Subfeed\"), COMMENT(4, \"Comment\"), USER(5, \"User\"), WELCOME(6,\n" +
			"\t\t\t\"Welcome\"), WEB(7, \"Web\"), BANNER(8, \"Banner\"), LANDSCAPING(9, \"Landscaping\"), COVER(10, \"Cover\"), DOCUMENT(11, \"Document\"),\n" +
			"\tGARDEN(12, \"Garden\"), TEMP_FILE(13, \"Temp File\")";

	private int value;

	private String name;

	private ObjectType(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	public String getLowerName() {
		return name.toLowerCase();
	}

	public static ObjectType getType(int value) {
		for (ObjectType type : ObjectType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}

		return UNKNOW;
	}

	public static ObjectType getTypeByName(String name) {
		for (ObjectType type : ObjectType.values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}

		return UNKNOW;
	}
}