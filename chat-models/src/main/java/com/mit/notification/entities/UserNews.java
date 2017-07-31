package com.mit.notification.entities;

import com.mit.common.entities.TimeDoc;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Hung Le on 4/5/17.
 */

@Document(collection = "user_news")
public class UserNews extends TimeDoc<Long> {
    private long id;
    private List<UserNewsItem> items;
    private int newCount;
    private List<UserNewsItem> providerItems;
    private int providerNewCount;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<UserNewsItem> getItems() {
        return items;
    }

    public void setItems(List<UserNewsItem> items) {
        this.items = items;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }

    public List<UserNewsItem> getProviderItems() {
        return providerItems;
    }

    public void setProviderItems(List<UserNewsItem> providerItems) {
        this.providerItems = providerItems;
    }

    public int getProviderNewCount() {
        return providerNewCount;
    }

    public void setProviderNewCount(int providerNewCount) {
        this.providerNewCount = providerNewCount;
    }
}
