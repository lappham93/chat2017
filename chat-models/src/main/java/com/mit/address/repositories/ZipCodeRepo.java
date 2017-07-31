package com.mit.address.repositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.mit.address.entities.ZipCode;
import com.mit.http.exception.SequenceException;
import com.mit.seq.repositories.SeqIdRepo;
import com.mongodb.BasicDBObject;

@Repository
public class ZipCodeRepo {
	@Autowired
	MongoOperations mongoOps;

	@Autowired
	SeqIdRepo seqIdRepo;

	public ZipCode getById(long id) {
		return mongoOps.findById(id, ZipCode.class);
	}
	
	public ZipCode getByCode(String code) {
		return mongoOps.findOne(new Query(Criteria.where("code").is(code)), ZipCode.class);
	}
	
	public List<ZipCode> search(String key) {
		Sort sort = new Sort(new Order(Direction.ASC, "code"));
		List<ZipCode> zipCodes = mongoOps.find(new Query(Criteria.where("code").regex(key, "i")).with(sort),
				ZipCode.class);
		return zipCodes;
	}

	public List<ZipCode> getList(String countryCode) {
		Sort sort = new Sort(new Order(Direction.ASC, "code"));
		List<ZipCode> zipCodes = mongoOps.find(new Query(Criteria.where("countryCode").is(countryCode)).with(sort),
				ZipCode.class);
		return zipCodes;
	}
	
	public List<ZipCode> getListByState(long stateId) {
		Sort sort = new Sort(new Order(Direction.ASC, "code"));
		List<ZipCode> zipCodes = mongoOps.find(new Query(Criteria.where("stateId").is(stateId)).with(sort),
				ZipCode.class);
		return zipCodes;
	}
	
	public List<ZipCode> getListByStates(List<Long> stateIds) {
		List<ZipCode> zipCodes = mongoOps.find(new Query(Criteria.where("stateId").in(stateIds)),
				ZipCode.class);
		return zipCodes;
	}

	public List<ZipCode> getListByCity(long cityId) {
		Sort sort = new Sort(new Order(Direction.ASC, "code"));
		List<ZipCode> zipCodes = mongoOps.find(new Query(Criteria.where("cityId").is(cityId)).with(sort),
				ZipCode.class);
		return zipCodes;
	}

	public List<ZipCode> getListByCities(List<Long> cityIds) {
		Sort sort = new Sort(new Order(Direction.ASC, "name"));
		List<ZipCode> zipCodes = mongoOps.find(new Query(Criteria.where("cityId").in(cityIds)),
				ZipCode.class);
		return zipCodes;
	}
	
	public List<ZipCode> getListByCodes(Collection<String> codes) {
		Sort sort = new Sort(new Order(Direction.ASC, "name"));
		List<ZipCode> zipCodes = mongoOps.find(new Query(Criteria.where("code").in(codes)).with(sort),
				ZipCode.class);
		return zipCodes;
	}

	public List<ZipCode> getListNear(com.mit.map.entities.Coordinate coordinate, double maxDistance, int limit) {
		BasicDBObject basicDBObject = new BasicDBObject("coordinate", new BasicDBObject("$nearSphere",new BasicDBObject("$geometry", new BasicDBObject("type", "Point").append("coordinates",Arrays.asList(coordinate.getLon(), coordinate.getLat()))).append("$maxDistance", maxDistance)));
		List<ZipCode> zipCodes = mongoOps.find(new BasicQuery(basicDBObject).limit(limit), ZipCode.class);
		return zipCodes;
	}
	
	public ZipCode getNearest(com.mit.map.entities.Coordinate coordinate) {
		List<ZipCode> zipCodes = getListNear(coordinate, 100000, 1);
		if (!CollectionUtils.isEmpty(zipCodes)) {
			return zipCodes.get(0);
		}
		return null;
	}
	
	public void insert(ZipCode zipCode) {
		mongoOps.insert(zipCode);
	}

	public void save(ZipCode zipCode) throws SequenceException {
		if (zipCode.getId() <= 0) {
			zipCode.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(zipCode.getClass())));
		}

		mongoOps.save(zipCode);
	}

	public void insertBatch(List<ZipCode> zipCodes) throws SequenceException {
		for (ZipCode zipCode : zipCodes) {
			if (zipCode.getId() <= 0) {
				zipCode.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(zipCode.getClass())));
			}
		}

		mongoOps.insertAll(zipCodes);
	}
}
