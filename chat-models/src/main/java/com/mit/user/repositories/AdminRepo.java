package com.mit.user.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.user.entities.Admin;

@Repository
public class AdminRepo extends TimeDocRepo<Admin>{
    @Autowired
    MongoOperations mongoOps;

    @Autowired
    SeqIdRepo seqIdRepo;
    
    
    public static final AdminRepo Instance = new AdminRepo();
	
    public Admin getById(long userId) {
        return mongoOps.findById(userId, Admin.class);
    }
    
    public Admin getByUserName(String userName) {
        return mongoOps.findOne(new Query(Criteria.where("userName").is(userName).and("status").gt(0)), Admin.class);
    }

    public void insertAdmin(Admin user) {
        mongoOps.insert(user);
    }
    
    public List<Admin> getListAdmins(int type) {
    	Query query = Query.query(Criteria.where("type").is(type));
    	return mongoOps.find(query, Admin.class);
    }
    
    public List<Admin> getListAdmins() {
    	return mongoOps.findAll(Admin.class);
    }
}
