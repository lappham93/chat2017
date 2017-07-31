package com.mit.user.entities;

import java.util.Set;

import com.mit.user.enums.AdminType;

public class RegionManagerProfile extends AdminProfile {
	public static final int TYPE = AdminType.REGION_MANAGER.getValue();
	private Set<String> zipCodes;

	public RegionManagerProfile() {
		super(TYPE);
	}

	public Set<String> getZipCodes() {
		return zipCodes;
	}

	public void setZipCodes(Set<String> zipCodes) {
		this.zipCodes = zipCodes;
	}

}
