package com.mit.user.repositories;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mit.common.repositories.TimeDocRepo;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.user.entities.Profile;
import com.mit.user.enums.ProfileType;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by Hung Le on 2/15/17.
 */

@Repository
public class ProfileRepo extends TimeDocRepo<Profile>{
    @Autowired
    MongoOperations mongoOps;

    @Autowired
    SeqIdRepo seqIdRepo;

    public Profile getById(long profileId) {
        return mongoOps.findById(profileId, Profile.class);
    }
    
    public int count(String query, boolean isActiveStatus, List<Long> profileIds) {
    	Criteria criteria = new Criteria();
    	if (isActiveStatus) {
    		criteria = criteria.and("status").gt(0);
    	}
    	if (profileIds != null) {
    		criteria = criteria.and("id").in(profileIds);
    	}
    	if (!StringUtils.isEmpty(query)) {
    		String phoneNumber = "+" + query;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
    	        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, null);
    	        phoneNumber = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    	        phoneNumber = phoneNumber.replaceAll("\\+", "");
            } catch (Exception e) {
            	phoneNumber = query;
            }
    		criteria = criteria.orOperator(Criteria.where("email").regex(query, "i"), Criteria.where("phone").regex(phoneNumber, "i"), Criteria.where("firstName").regex(query, "i"), Criteria.where("lastName").regex(query, "i"));
    	}
    	return (int) mongoOps.count(new Query(criteria), Profile.class);
    }
    
    public int count(boolean isActiveStatus, List<Long> profileIds) {
    	Criteria criteria = new Criteria();
    	if (isActiveStatus) {
    		criteria = criteria.and("status").gt(0);
    	}
    	if (profileIds != null) {
    		criteria = criteria.and("id").in(profileIds);
    	}
    	return (int) mongoOps.count(new Query(criteria), Profile.class);
    }
    
    public List<Profile> getProviderList() {
    	Query query = new Query(Criteria.where("profileType").is(ProfileType.PROVIDER.getValue()));
    	return mongoOps.find(query, Profile.class);
    }
    
    public List<Long> getProviderIdsList() {
    	List<Profile> profiles = getProviderList();
        List<Long> ids = new LinkedList<>();
        if (!CollectionUtils.isEmpty(profiles)) {
        	for (Profile profile: profiles) {
        		ids.add(profile.getId());
        	}
        }
        return ids;
    }

    public Map<Long, Profile> getMapProviderInGMT(int gmt) {
        Query query = new Query(Criteria.where("profileType").is(ProfileType.PROVIDER.getValue()).and("gmt").is(gmt));
        List<Profile> profiles = mongoOps.find(query, Profile.class);
        Map<Long, Profile> profileMap = new HashMap<>();
        for (Profile profile: profiles) {
            profileMap.put(profile.getId(), profile);
        }

        return profileMap;
    }
    
    public List<Profile> getByIds(List<Long> profileIds) {
        List<Profile> profiles = mongoOps.find(new Query(Criteria.where("id").in(profileIds)), Profile.class);
        return profiles;
    }
    
    public List<Profile> getByIds(List<Long> profileIds, int count, int from) {
    	Sort sort = new Sort(Direction.ASC, "createdDate");
        List<Profile> profiles = mongoOps.find(new Query(Criteria.where("id").in(profileIds)).with(sort).skip(from).limit(count), Profile.class);
        return profiles;
    }
    
    public List<Profile> getSlice(String query, int from, int count, String fieldSort, boolean sortAcs, boolean isActiveStatus) { 
    	Criteria criteria = new Criteria();
    	if (isActiveStatus) {
    		criteria = Criteria.where("status").gt(0);
    	}
    	if (!StringUtils.isEmpty(query)) {
    		String phoneNumber = "+" + query;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
    	        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, null);
    	        phoneNumber = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    	        phoneNumber = phoneNumber.replaceAll("\\+", "");
            } catch (Exception e) {
            	phoneNumber = query;
            }
    		criteria = criteria.orOperator(Criteria.where("email").regex(query, "i"), Criteria.where("phone").regex(phoneNumber, "i"), Criteria.where("firstName").regex(query, "i"), Criteria.where("lastName").regex(query, "i"));
    	}
    	Sort sort = new Sort(new Order(sortAcs ? Direction.ASC : Direction.DESC, fieldSort));
        List<Profile> profiles = mongoOps.find(new Query(criteria).with(sort).skip(from).limit(count), Profile.class);
        return profiles;
    }

    public List<Profile> getListRewardPayable() {
        List<Profile> profiles = mongoOps.find(new Query(Criteria.where("isRewardPayable").in(true)), Profile.class);
        return profiles;
    }

    public Map<Long, Profile> getMapByIds(List<Long> profileIds, int from, int count) {
        List<Profile> profiles = getByIds(profileIds, count, from);
        Map<Long, Profile> profileMap = new HashMap<Long, Profile>();
        for (Profile profile : profiles) {
            profileMap.put(profile.getId(), profile);
        }

        return profileMap;
    }

    public Map<Long, Profile> getMapByIds(List<Long> profileIds) {
        List<Profile> profiles = getByIds(profileIds);
        Map<Long, Profile> profileMap = new HashMap<Long, Profile>();
        for (Profile profile : profiles) {
            profileMap.put(profile.getId(), profile);
        }

        return profileMap;
    }

    public Map<Long, Profile> getMapLastOnlineByIds(List<Long> profileIds) {
        return getMapByIds(profileIds, Arrays.asList("lastOnline"));
    }
    
    public List<Profile> getListActive() {
        List<Profile> profiles = mongoOps.find(new Query(Criteria.where("status").gt(0)), Profile.class);
        return profiles;
    }
    
    public List<Long> getListIdsActive() {
        List<Profile> profiles = getListActive();
        List<Long> ids = new LinkedList<>();
        if (!CollectionUtils.isEmpty(profiles)) {
        	for (Profile profile: profiles) {
        		ids.add(profile.getId());
        	}
        }
        return ids;
    }

    public int updateAvatar(long profileId, long photoId) {
        Update update = new Update();
        update.set("avatar", photoId);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }

    public int updateCover(long profileId, long photoId) {
        Update update = new Update();
        update.set("cover", photoId);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }
    
    public int updateCardDefault(long profileId, long deletedCard, long candidateCard) {
        Update update = new Update();
        update.set("defaultCardId", candidateCard);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId).and("defaultCardId").is(deletedCard)), update, Profile.class);
        return rs.getN();
    }
    
    public int updateCardDefault(long profileId, long newDefaultCard) {
        Update update = new Update();
        update.set("defaultCardId", newDefaultCard);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }
    
    public void delete(long id) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("status", 0);
		mongoOps.updateFirst(query, update, Profile.class);
	}
    
    public void hardDelete(long id) {
		Query query = new Query(Criteria.where("id").is(id));
		mongoOps.remove(query, Profile.class);
	}
    
    public int updateStatus(long profileId, int status) {
        Update update = new Update();
        update.set("status", status);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }

    public int updateBanUserIds(long profileId, List<Long> banUserIds) {
        Update update = new Update();
        update.set("banUserIds", banUserIds);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }

    public int updateLastOnline(long profileId, Date lastOnline) {
        Update update = new Update();
        update.set("lastOnline", lastOnline);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }

    public int updateInterestCategoryIds(long profileId, List<Long> interestCategoryIds) {
        Update update = new Update();
        update.set("interestCategoryIds", interestCategoryIds);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }

    public int addToSetInterestEventId(long profileId, long eventId) {
        Update update = new Update();
        update.addToSet("interestEventIds", eventId);
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(profileId)), update, Profile.class);
        return rs.getN();
    }
}
