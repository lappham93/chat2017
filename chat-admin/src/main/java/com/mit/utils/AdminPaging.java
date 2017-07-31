package com.mit.utils;

import com.mit.http.ApiResponse.Paging;

public class AdminPaging extends Paging {
	private int from;

	public AdminPaging(int from, String prev, String next) {
		super(prev, next);
		this.from = from;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}
	
}
