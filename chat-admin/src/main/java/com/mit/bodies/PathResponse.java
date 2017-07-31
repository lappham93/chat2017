package com.mit.bodies;

public class PathResponse {
	private String ib;
	private String ie;
	
	public PathResponse(String ib, String ie) {
		this.ib = ib;
		this.ie = ie;
	}

	public String getIb() {
		return ib;
	}

	public void setIb(String ib) {
		this.ib = ib;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

}
