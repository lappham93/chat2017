package com.mit.address.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "country")
public class Country {
	@Id
	private String isoCode;
	private String name;
	private String phoneCode;
	private int status;

	public Country() {
		super();
	}

	public Country(String isoCode, String name) {
		super();
		this.isoCode = isoCode;
		this.name = name;
		this.status = 1;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

}
