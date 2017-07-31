package com.mit.common.repositories;

import com.mit.common.entities.ActiveTimeDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActiveTimeDocRepo<T extends ActiveTimeDoc<Long>> extends TimeDocRepo<T> {
	@Autowired
	protected MongoOperations mongoOps;

	public int totalAll(Criteria criteria, boolean onlyActive) {
		criteria = criteria.and("isDeleted").is(false);
		if (onlyActive) {
			criteria.and("isActive").is(true);
		}
		return (int) mongoOps.count(new Query(criteria), entityClass);
	}

	public List<T> getListByIds(List<Long> ids) {
		return mongoOps.find(new Query(Criteria.where("id").in(ids).and("isActive").is(true).and("isDeleted").is(false)), entityClass);
	}

	public List<T> getList() {
		return getList(true);
	}

	public List<T> getList(boolean onlyActive) {
		return getList(null, onlyActive, null);
	}

	public List<T> getList(Criteria criteria) {
		return getList(criteria, true);
	}

	public List<T> getList(Criteria criteria, boolean onlyActive) {
		return getList(criteria, onlyActive, null);
	}

	public List<T> getList(Criteria criteria, Sort sort) {
		return getList(criteria, true, sort);
	}

	public List<T> getList(Criteria criteria, boolean onlyActive, Sort sort) {
		if (criteria == null) {
			criteria = new Criteria();
		}
		if (onlyActive) {
			criteria = criteria.and("isActive").is(true);
		}
		criteria = criteria.and("isDeleted").is(false);
		Query query = new Query(criteria);
		if (sort != null) {
			query.with(sort);
		}
		return mongoOps.find(query, entityClass);
	}

	public void delete(long id) {
		Update update = new Update().set("isDeleted", true);
		Query query = new Query(Criteria.where("id").is(id));
		mongoOps.updateFirst(query, update, entityClass);
	}

	public List<T> getSlice(Criteria criteria, int offset, int count) {
		return getSlice(criteria, true, offset, count, null);
	}

	public List<T> getSlice(Criteria criteria, int offset, int count, Sort sort) {
		return getSlice(criteria, true, offset, count, sort);
	}

	public List<T> getSlice(Criteria criteria, boolean onlyActive, int offset, int count) {
		return getSlice(criteria, onlyActive, offset, count, null);
	}

	public List<T> getSlice(Criteria criteria, boolean onlyActive, int offset, int count, Sort sort) {
		if (criteria == null) {
			criteria = new Criteria();
		}
		if (onlyActive) {
			criteria = criteria.and("isActive").is(true);
		}
		criteria = criteria.and("isDeleted").is(false);
		Query query = new Query(criteria).skip(offset).limit(count);
		if (sort != null) {
			query.with(sort);
		}
		return mongoOps.find(query, entityClass);
	}
}
