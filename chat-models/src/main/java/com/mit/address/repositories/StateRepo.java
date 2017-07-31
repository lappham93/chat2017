package com.mit.address.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mit.address.entities.State;
import com.mit.http.exception.SequenceException;
import com.mit.seq.repositories.SeqIdRepo;

@Repository
public class StateRepo {
	@Autowired
	MongoOperations mongoOps;

	@Autowired
	SeqIdRepo seqIdRepo;

	public State getById(long id) {
		return mongoOps.findById(id, State.class);
	}
	
	public State getByName(String name) {
		return mongoOps.findOne(new Query(Criteria.where("name").is(name)), State.class);
	}
	
	public int count(String countryCode, boolean onlyActive) {
		Criteria criteria = Criteria.where("countryCode").is(countryCode);
		if (onlyActive) {
			criteria.and("status").gt(0);
		}
		Query query = new Query(criteria);
		return (int)mongoOps.count(query, State.class);
	}

	public List<State> getList(String countryCode) {
		Sort sort = new Sort(new Order(Direction.ASC, "name"));
		List<State> states = mongoOps.find(new Query(Criteria.where("countryCode").is(countryCode)).with(sort),
				State.class);
		return states;
	}
	
	public List<State> getListActive(String countryCode) {
		Sort sort = new Sort(new Order(Direction.ASC, "name"));
		List<State> states = mongoOps.find(new Query(Criteria.where("countryCode").is(countryCode).and("status").gt(0)).with(sort),
				State.class);
		return states;
	}
	
	public List<State> getSlice(String countryCode, boolean onlyActive, int from, int count) {
		Criteria criteria = Criteria.where("countryCode").is(countryCode);
		if (onlyActive) {
			criteria.and("status").gt(0);
		}
		Sort sort = new Sort(new Order(Direction.DESC, "updatedDate"), new Order(Direction.ASC, "name"));
		Query query = new Query(criteria);
		return mongoOps.find(query.with(sort).skip(from).limit(count), State.class);
	}

	public void insert(State state) {
		mongoOps.insert(state);
	}

	public void save(State state) throws SequenceException {
		if (state.getId() <= 0) {
			state.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(state.getClass())));
		}

		mongoOps.save(state);
	}
	
	public void updateStatus(long id, int status) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("status", status);
		mongoOps.updateFirst(query, update, State.class);
	}

	public void insertBatch(List<State> states) throws SequenceException {
		for (State country : states) {
			if (country.getId() <= 0) {
				country.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(country.getClass())));
			}
		}
 
		mongoOps.insertAll(states);
	}
}
