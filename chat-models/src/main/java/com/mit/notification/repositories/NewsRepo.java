package com.mit.notification.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.notification.entities.News;

/**
 * Created by Hung Le on 4/5/17.
 */

@Repository
public class NewsRepo extends TimeDocRepo<News> {
    @Autowired
    protected MongoOperations mongoOps;

    public News getById(long id) {
        return mongoOps.findById(id, News.class);
    }

    public List<News> getByIds(List<Long> ids) {
        return mongoOps.find(new Query(Criteria.where("id").in(ids)), News.class);
    }

    public Map<Long, News> getMapByIds(List<Long> ids) {
        List<News> newsList = getByIds(ids);
        Map<Long, News> newsMap = new HashMap<>();

        for (News news: newsList) {
            newsMap.put(news.getId(), news);
        }

        return newsMap;
    }
}
