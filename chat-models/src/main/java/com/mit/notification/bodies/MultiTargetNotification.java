package com.mit.notification.bodies;

import java.util.List;

public class MultiTargetNotification extends Notification {

	private List<Long> userIds;

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}
