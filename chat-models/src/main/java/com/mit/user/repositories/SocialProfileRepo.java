package com.mit.user.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mit.http.exception.SequenceException;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.user.entities.SocialProfile;
import com.mongodb.WriteResult;

/**
 * Created by Hung Le on 2/15/17.
 */

@Repository
public class SocialProfileRepo {
    @Autowired
    MongoOperations mongoOps;

    @Autowired
    SeqIdRepo seqIdRepo;

    public SocialProfile getByToken(String token) {
        return mongoOps.findOne(new Query(Criteria.where("token").is(token)), SocialProfile.class);
    }

    public int removeByToken(String token) {
        WriteResult writeResult = mongoOps.remove(Query.query(Criteria.where("token").is(token)), SocialProfile.class);
        return writeResult.getN();
    }

    public void save(SocialProfile socialProfile) throws SequenceException {
    	if (socialProfile.get_id() <= 0) {
    		socialProfile.set_id(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(socialProfile.getClass())));
    	}
        mongoOps.save(socialProfile);
    }
    
    public void deleteById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		mongoOps.remove(query, SocialProfile.class);
	}
}
