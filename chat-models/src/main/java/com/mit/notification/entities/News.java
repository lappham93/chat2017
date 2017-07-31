package com.mit.notification.entities;

import com.mit.common.entities.ActiveTimeDoc;
import com.mit.notification.bodies.MultiTargetNotification;
import com.mit.notification.bodies.SingleTargetNotification;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "news")
public abstract class News extends ActiveTimeDoc<Long> {

	public static final int ACTIVE = 1;

	private long id;
	private int type;
	private int event;
	
	public News(int type) {
		this.type = type;
	}

	@Override
	public Long getId() {
		return id;
	}

    @Override
	public void setId(Long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

    public abstract String getMsg();

    public abstract Map<String, String> getData();

    public SingleTargetNotification buildSingleTargetNotification(long userId, int appType) {
        SingleTargetNotification notificationItem = new SingleTargetNotification();
        notificationItem.setId(getId());
        notificationItem.setUserId(userId);
        notificationItem.setType(getType());
        notificationItem.setAppType(appType);
        notificationItem.setMessage(getMsg());
        notificationItem.setData(getData());

        return notificationItem;
    }

    public MultiTargetNotification buildMultiDestNotification(List<Long> userIds, int appType) {
        MultiTargetNotification notificationItem = new MultiTargetNotification();
        notificationItem.setId(getId());
        notificationItem.setUserIds(userIds);
        notificationItem.setType(getType());
        notificationItem.setAppType(appType);
        notificationItem.setMessage(getMsg());
        notificationItem.setData(getData());

        return notificationItem;
    }
}
