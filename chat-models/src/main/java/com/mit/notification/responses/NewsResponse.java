package com.mit.notification.responses;

import com.mit.notification.entities.News;

/**
 * Created by Hung Le on 4/4/17.
 */
public class NewsResponse {
    private int type;

    public NewsResponse(News news) {
        this.type = news.getType();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
