package com.mit.notification.bodies;

public class SingleTargetNotification extends Notification {

	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
