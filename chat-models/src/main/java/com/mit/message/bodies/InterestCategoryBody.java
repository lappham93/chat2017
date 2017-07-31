package com.mit.message.bodies;

import java.util.List;

/**
 * Created by Hung on 6/24/2017.
 */
public class InterestCategoryBody {
    private long eventId;
    private List<Long> categoryIds;
    private boolean add;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}
