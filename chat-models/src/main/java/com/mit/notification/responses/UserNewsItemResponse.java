package com.mit.notification.responses;

import com.mit.notification.entities.UserNewsItem;

import java.util.Date;

/**
 * Created by Hung Le on 5/8/17.
 */
public class UserNewsItemResponse {
    private long id;
    private int type;
    private NewsResponse content;
    private boolean viewed;
    private Date createdDate;

    public UserNewsItemResponse(UserNewsItem userNewsItem, NewsResponse newsResponse) {
        id = userNewsItem.getId();
        type = newsResponse.getType();
        content = newsResponse;
        viewed = userNewsItem.isViewed();
        createdDate = userNewsItem.getCreatedDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NewsResponse getContent() {
        return content;
    }

    public void setContent(NewsResponse content) {
        this.content = content;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
