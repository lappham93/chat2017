package com.mit.user.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.user.entities.User;

/**
 * Created by Hung Le on 2/15/17.
 */

@Repository
public class UserRepo extends TimeDocRepo<User> {
	@Autowired
	MongoOperations mongoOps;

	@Autowired
	SeqIdRepo seqIdRepo;

	public User getUserById(String userId) {
		return mongoOps.findById(userId, User.class);
	}

	public User getUserByUserName(String userName) {
		return mongoOps.findOne(Query.query(Criteria.where("userName").is(userName)), User.class);
	}
	
	 public void delete(long id) {
		Query query = new Query(Criteria.where("id").is(id));
		mongoOps.remove(query, User.class);
	}
}
