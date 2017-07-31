package com.mit.session.repositories;

import com.mit.session.entities.RefreshToken;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Hung Le on 2/24/17.
 */

@Repository
public class RefreshTokenRepo {
    @Autowired
    MongoOperations mongoOps;

    public RefreshToken getByToken(String token) {
        return mongoOps.findOne(new Query(Criteria.where("token").is(token)), RefreshToken.class);
    }

    public void save(RefreshToken refreshToken) {
        mongoOps.save(refreshToken);
    }

    public int remove(RefreshToken refreshToken) {
        WriteResult writeResult = mongoOps.remove(refreshToken);
        return writeResult.getN();
    }

    public int remove(String refreshToken) {
        WriteResult writeResult = mongoOps.remove(new Query(Criteria.where("token").is(refreshToken)), RefreshToken.class);
        return writeResult.getN();
    }

    public int removeByUserId(long userId) {
        WriteResult writeResult = mongoOps.remove(new Query(Criteria.where("userId").is(userId)), RefreshToken.class);
        return writeResult.getN();
    }

    public int removeByUserIdAndAppType(long userId, int appType) {
        WriteResult writeResult = mongoOps.remove(new Query(Criteria.where("userId").is(userId).and("appType").is(appType)), RefreshToken.class);
        return writeResult.getN();
    }
}