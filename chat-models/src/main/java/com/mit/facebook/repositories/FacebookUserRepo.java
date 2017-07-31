package com.mit.facebook.repositories;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.facebook.entities.FacebookUser;
import com.mit.http.exception.SequenceException;

@Repository
public class FacebookUserRepo extends TimeDocRepo<FacebookUser> {
	
	public FacebookUser getById(long id) {
		return mongoOps.findById(id, FacebookUser.class);
	}
	
	public FacebookUser getByUserId(long userId) {
		return mongoOps.findOne(new Query(Criteria.where("userId").is(userId)), FacebookUser.class);
	}
	
	public void upsert(long userId, String facebookId) throws SequenceException {
		Query query = new Query(Criteria.where("userId").is(userId)); 
		Update update = new Update();
		update.set("facebookId", facebookId);
		update.setOnInsert("id", seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(FacebookUser.class)));
		mongoOps.findAndModify(query, update, FindAndModifyOptions.options().upsert(true), FacebookUser.class);
	}
}
