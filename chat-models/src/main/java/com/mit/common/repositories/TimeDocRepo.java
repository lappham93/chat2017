package com.mit.common.repositories;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.mit.common.entities.TimeDoc;
import com.mit.http.exception.SequenceException;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.user.entities.Profile;

@Repository
public class TimeDocRepo<T extends TimeDoc<Long>> {
	@Autowired
	protected MongoOperations mongoOps;

	@Autowired
	protected SeqIdRepo seqIdRepo;

	protected Class<T> entityClass;

	public Class<T> getGenericClass() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();

		@SuppressWarnings("unchecked")
		Class<T> ret = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		return ret;
	}

	@PostConstruct
	public void init() {
		entityClass = getGenericClass();
	}
	
	public void insert(T user) {
		user.setNewed(true);
		mongoOps.insert(user);
	}

	public void save(T user) throws SequenceException {
		long id = (long) user.getId();
		if (id <= 0) {
			user.setNewed(true);
			user.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(user.getClass())));
		}

		mongoOps.save(user);
	}

	public void insertBatch(List<T> objs) throws SequenceException {
		for (T obj : objs) {
			if (obj.getId() <= 0) {
				obj.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(obj.getClass())));
			}
		}

		mongoOps.insertAll(objs);
	}

	public T getById(long id) {
		return mongoOps.findById(id, entityClass);
	}

	public List<T> getListByIds(List<Long> ids) {
		return mongoOps.find(new Query(Criteria.where("id").in(ids)), entityClass);
	}

	public List<T> getListByIds(List<Long> ids, List<String> fields) {
		Query query = new Query(Criteria.where("id").in(ids));
		if (!CollectionUtils.isEmpty(fields)) {
			Field queryField = query.fields();
			for (String field: fields) {
				queryField.include(field);
			}
		}
		return mongoOps.find(query, entityClass);
	}

	public Map<Long, T> getMapByIds(List<Long> ids) {
		List<T> entities = getListByIds(ids);
		return list2Map(entities);
	}

	public Map<Long, T> getMapByIds(List<Long> ids, List<String> fields) {
		List<T> entities = getListByIds(ids, fields);
		return list2Map(entities);
	}

	public Map<Long, T> list2Map(List<T> entities) {
		Map<Long, T> entityMap = new HashMap<>();
		for (T entity: entities) {
			entityMap.put(entity.getId(), entity);
		}

		return entityMap;
	}
	
	public int count(Date fromTime) {
    	Criteria criteria = new Criteria("createdDate").gte(fromTime);
    	return (int) mongoOps.count(new Query(criteria), Profile.class);
    }
	
	public void delete(long id) {
		Update update = new Update().set("isDeleted", true);
		mongoOps.updateFirst(new Query(Criteria.where("id").is(id)), update, entityClass);
	}
	
	public void hardDelete(long id) {
		mongoOps.remove(new Query(Criteria.where("id").is(id)), entityClass);
	}
	
	protected Criteria initCriteria() {
		return Criteria.where("isDeleted").is(false);
	}
	
	protected Sort sortDefault() {
		return new Sort(new Order(Direction.DESC, "updatedDate"));
	}
}
