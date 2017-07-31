package com.mit.asset.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mit.asset.entities.Link;
import com.mit.common.repositories.TimeDocRepo;
import com.mit.seq.repositories.SeqIdRepo;

@Repository
public class LinkRepo extends TimeDocRepo<Link>{
	@Autowired
	MongoOperations mongoOps;

	@Autowired
	SeqIdRepo seqIdRepo;

	public Link getById(long id) {
		return mongoOps.findById(id, Link.class);
	}

	public List<Link> getByIds(List<Long> ids) {
		List<Link> comments = mongoOps.find(new Query(Criteria.where("id").in(ids)), Link.class);
		return comments;
	}

}
