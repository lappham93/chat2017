package com.mit.notification.responses;

import java.util.List;

/**
 * Created by Hung Le on 5/8/17.
 */
public class UserNewsResponse {
    private List<UserNewsItemResponse> news;
    private int newCount;

    public UserNewsResponse(List<UserNewsItemResponse> news, int newCount) {
        this.news = news;
        this.newCount = newCount;
    }

    public List<UserNewsItemResponse> getNews() {
        return news;
    }

    public void setNews(List<UserNewsItemResponse> news) {
        this.news = news;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }
}
