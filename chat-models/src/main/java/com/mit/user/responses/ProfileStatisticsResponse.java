package com.mit.user.responses;

public class ProfileStatisticsResponse {
	private ProfileShortResponse user;
	private int numFollow;
	private int numFollowed;
	private int numFeed;
	private int ratingTotal;
	private double ratingPoint;
	private int serveTotal;
	private long providerRegisterTime;

	public ProfileShortResponse getUser() {
		return user;
	}

	public void setUser(ProfileShortResponse user) {
		this.user = user;
	}

	public int getNumFollow() {
		return numFollow;
	}

	public void setNumFollow(int numFollow) {
		this.numFollow = numFollow;
	}

	public int getNumFollowed() {
		return numFollowed;
	}

	public void setNumFollowed(int numFollowed) {
		this.numFollowed = numFollowed;
	}

	public int getNumFeed() {
		return numFeed;
	}

	public void setNumFeed(int numFeed) {
		this.numFeed = numFeed;
	}

	public int getRatingTotal() {
		return ratingTotal;
	}

	public void setRatingTotal(int ratingTotal) {
		this.ratingTotal = ratingTotal;
	}

	public double getRatingPoint() {
		return ratingPoint;
	}

	public void setRatingPoint(double ratingPoint) {
		this.ratingPoint = ratingPoint;
	}

	public int getServeTotal() {
		return serveTotal;
	}

	public void setServeTotal(int serveTotal) {
		this.serveTotal = serveTotal;
	}

	public long getProviderRegisterTime() {
		return providerRegisterTime;
	}

	public void setProviderRegisterTime(long providerRegisterTime) {
		this.providerRegisterTime = providerRegisterTime;
	}

}
