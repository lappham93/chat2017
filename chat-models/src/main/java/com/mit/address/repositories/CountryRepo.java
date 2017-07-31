package com.mit.address.repositories;

import com.mit.address.entities.Country;
import com.mit.seq.repositories.SeqIdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepo {
    @Autowired
    MongoOperations mongoOps;

    @Autowired
    SeqIdRepo seqIdRepo;
    
    public Country getById(long id) {
        return mongoOps.findById(id, Country.class);
    }
    
    public Country getByCode(String isoCode) {
    	Criteria criteria = Criteria.where("isoCode").is(isoCode);
        return mongoOps.findOne(new Query(criteria), Country.class);
    }
    
    public List<Country> getList() {
    	Sort sort = new Sort(new Order(Direction.ASC, "name"));
        List<Country> comments = mongoOps.find(new Query().with(sort), Country.class);
        return comments;
    }
    
    public void insert(Country comment) {
        mongoOps.insert(comment);
    }

    public void save(Country user) {
        mongoOps.save(user);
    }
    
    public void insertBatch(List<Country> countries) {
        mongoOps.insertAll(countries);
    }
    
}
