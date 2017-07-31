package com.mit.user.repositories;

import com.mit.http.exception.SequenceException;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.user.entities.UserSocial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Hung Le on 2/15/17.
 */

@Repository
public class UserSocialRepo {
    @Autowired
    MongoOperations mongoOps;

    @Autowired
    SeqIdRepo seqIdRepo;
    
    public UserSocial getByUserId(long userId) {
    	return mongoOps.findOne(new Query(Criteria.where("userId").is(userId)), UserSocial.class);
    }

    public UserSocial getUserSocial(int type, String socialId) {
        UserSocial userSocial = mongoOps.findOne(new Query(Criteria.where("type").is(type).and("socialId").is(socialId)), UserSocial.class);
        return userSocial;
    }

    public void deleteSocialId(int type, String socialId) {
        mongoOps.remove(Query.query(Criteria.where("type").is(type).and("socialId").is(socialId)), UserSocial.class);
    }

    public void save(UserSocial userSocial) throws SequenceException {
        if (userSocial.getId() <= 0) {
            userSocial.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(userSocial.getClass())));
        }

        mongoOps.save(userSocial);
    }
}
