package com.mit.address.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mit.address.entities.Address;
import com.mit.common.repositories.TimeDocRepo;
import com.mit.seq.repositories.SeqIdRepo;

@Repository
public class AddressRepo extends TimeDocRepo<Address>{
	@Autowired
	MongoOperations mongoOps;

	@Autowired
	SeqIdRepo seqIdRepo;

	public Address getById(long id) {
		return mongoOps.findById(id, Address.class);
	}
	
	public Address getByPlaceId(long userId, String placeId) {
		return mongoOps.findOne(new Query(Criteria.where("userId").is(userId).and("placeId").is(placeId)), Address.class);
	}
	
	public Address getByLonLat(long userId, double lon, double lat) {
		Criteria nearCri = Criteria.where("coordinate").withinSphere(new Circle(new Point(lon, lat), new Distance(0.05, Metrics.KILOMETERS)));
		return mongoOps.findOne(new Query(Criteria.where("userId").is(userId).andOperator(nearCri)), Address.class);
	}

}
