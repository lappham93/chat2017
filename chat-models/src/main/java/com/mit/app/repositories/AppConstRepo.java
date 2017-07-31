package com.mit.app.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.mit.app.entities.AppConst;

/**
 * Created by Hung Le on 3/20/17.
 */

@Repository
public class AppConstRepo {

    @Autowired
    MongoOperations mongoOps;

    public AppConst getByKey(String key) {
        return mongoOps.findById(key, AppConst.class);
    }
    
    public List<AppConst> getAll() {
        return mongoOps.findAll(AppConst.class);
    }

    public void save(AppConst appConst, boolean newed) {
    	appConst.setNewed(newed);
        mongoOps.save(appConst);
    }
}
