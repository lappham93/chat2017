package com.mit.user.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.user.entities.Speaker;

/**
 * Created by Hung Le on 2/15/17.
 */

@Repository
public class SpeakerRepo extends TimeDocRepo<Speaker> {
	
	public int totalAll() {
		Criteria criteria = Criteria.where("isDeleted").is(false);
		return (int)mongoOps.count(new Query(criteria), Speaker.class);
	}
	
	public Speaker getById(String userId) {
		return mongoOps.findById(userId, Speaker.class);
	}
	
	public List<Speaker> getList(boolean onlyActive) {
		Criteria criteria = Criteria.where("isDeleted").is(false);
		if (onlyActive) {
			criteria.and("isActive").is(true);
		}
		Sort sort = new Sort(Direction.ASC, "lastName", "firstName");
		return mongoOps.find(new Query(criteria).with(sort), Speaker.class);
	}

	public List<Speaker> getSlice(int from, int count) {
		Criteria creteria = Criteria.where("isDeleted").is(false);
		Sort sort = new Sort(new Order(Direction.DESC, "updatedDate"));
		return mongoOps.find(new Query(creteria).with(sort).skip(from).limit(count), Speaker.class);
	}
	 public void delete(long id) {
		Update update = new Update().set("isDeleted", true);
		Query query = new Query(Criteria.where("id").is(id));
		mongoOps.updateFirst(query, update, Speaker.class);
	}
}
