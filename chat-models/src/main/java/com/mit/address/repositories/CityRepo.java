package com.mit.address.repositories;

import java.util.Collection;
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

import com.mit.address.entities.City;
import com.mit.http.exception.SequenceException;
import com.mit.seq.repositories.SeqIdRepo;

@Repository
public class CityRepo {
	@Autowired
	MongoOperations mongoOps;

	@Autowired
	SeqIdRepo seqIdRepo;
	
	public int count(Collection<Long> stateIds, boolean onlyActive) {
		Criteria criteria = new Criteria();
		if (stateIds != null) {
			criteria.and("stateId").in(stateIds);
		}
		if (onlyActive) {
			criteria.and("status").gt(0);
		}
		Query query = new Query(criteria);
		return (int)mongoOps.count(query, City.class);
	}
	
	public List<City> getByStateId(long stateId) {
		Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
		return mongoOps.find(new Query(Criteria.where("stateId").is(stateId)).with(sort), City.class);
	}
	
	public List<City> getActiveByStateId(long stateId) {
		Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
		return mongoOps.find(new Query(Criteria.where("stateId").is(stateId).and("status").gt(0)).with(sort), City.class);
	}
	
	public List<City> getSlice(Collection<Long> stateIds, boolean onlyActive, int from, int count) {
		Criteria criteria = new Criteria();
		if (stateIds != null) {
			criteria.and("stateId").in(stateIds);
		}
		if (onlyActive) {
			criteria.and("status").gt(0);
		}
		Sort sort = new Sort(new Order(Direction.DESC, "updatedDate"), new Order(Direction.ASC, "stateId"), new Order(Direction.ASC, "name"));
		Query query = new Query(criteria);
		return mongoOps.find(query.with(sort).skip(from).limit(count), City.class);
	}
	
	public City getById(long id) {
		return mongoOps.findById(id, City.class);
	}

	public List<City> getByIds(List<Long> ids) {
		Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
		List<City> cities = mongoOps.find(new Query(Criteria.where("id").in(ids)).with(sort), City.class);
		return cities;
	}
	
	public List<City> getAll() {
		return mongoOps.findAll(City.class);
	}

	public void insert(City comment) {
		mongoOps.insert(comment);
	}
	
	public void updateStatus(long id, int status) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("status", status);
		mongoOps.updateFirst(query, update, City.class);
	}

	public void save(City user) throws SequenceException {
		if (user.getId() <= 0) {
			user.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(user.getClass())));
		}

		mongoOps.save(user);
	}

	public void insertBatch(List<City> cities) throws SequenceException {
		for (City city : cities) {
			if (city.getId() <= 0) {
				city.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(city.getClass())));
			}
		}

		mongoOps.insertAll(cities);
	}

}
