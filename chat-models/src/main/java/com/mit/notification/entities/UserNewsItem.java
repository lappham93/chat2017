package com.mit.notification.entities;

import java.util.Date;

/**
 * Created by Hung Le on 4/5/17.
 */
public class UserNewsItem {
    private long id;
    private boolean viewed;
    private Date createdDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
