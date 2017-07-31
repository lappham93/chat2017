package com.mit.user.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.user.entities.AdminProfile;

/**
 * Created by Hung Le on 2/15/17.
 */

@Repository
public class AdminProfileRepo extends TimeDocRepo<AdminProfile>{
    @Autowired
    MongoOperations mongoOps;

    @Autowired
    SeqIdRepo seqIdRepo;

    public int count(int adminType) {
    	return (int) mongoOps.count(new Query(Criteria.where("adminType").is(adminType).and("isDeleted").is(false)), AdminProfile.class);
    }
    
    public AdminProfile getById(long profileId) {
        return mongoOps.findById(profileId, AdminProfile.class);
    }

    public List<AdminProfile> getByIds(List<Long> profileIds) {
        List<AdminProfile> profiles = mongoOps.find(new Query(Criteria.where("id").in(profileIds)), AdminProfile.class);
        return profiles;
    }
    
    public List<AdminProfile> getSlice(int adminType, int count, int from) {
    	Sort sort = new Sort(Direction.DESC, "createdDate");
        return mongoOps.find(new Query(Criteria.where("adminType").is(adminType).and("isDeleted").is(false)).with(sort).skip(from).limit(count), AdminProfile.class);
    }

    public Map<Long, AdminProfile> getMapByIds(List<Long> profileIds) {
        List<AdminProfile> profiles = getByIds(profileIds);
        Map<Long, AdminProfile> profileMap = new HashMap<Long, AdminProfile>();
        for (AdminProfile profile : profiles) {
            profileMap.put(profile.getId(), profile);
        }

        return profileMap;
    }

    public AdminProfile updateStatus(long id, int status) {
    	Query query = new Query(Criteria.where("id").is(id));
    	Update update = new Update().set("status", status);
    	return mongoOps.findAndModify(query, update, AdminProfile.class);
    }
}
