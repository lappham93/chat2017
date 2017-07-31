package com.mit.app.repositories;

import com.mit.app.entities.AppKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Hung Le on 2/13/17.
 */
@Repository
public class AppKeyRepo {
    @Autowired
    MongoOperations mongoOps;

    public void save(AppKey appKey) {
        mongoOps.save(appKey);
    }

    public AppKey getByApiKey(String apiKey) {
        return mongoOps.findOne(new Query(Criteria.where("apiKey").is(apiKey)), AppKey.class);
    }

    public List<AppKey> getAllKey() {
        return mongoOps.find(new Query(), AppKey.class);
    }
}
