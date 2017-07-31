package com.mit.message.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.message.entities.Message2;
import com.mongodb.WriteResult;

@Repository
public class Message2Repo extends TimeDocRepo<Message2> {

    @Autowired
    MongoOperations mongoOps;

    public List<Message2> getMessage(List<Long> messageIds) {
        return mongoOps.find(new Query(Criteria.where("id").in(messageIds)), entityClass);
    }
    
    public List<Message2> getListToMessageId(List<Long> refIds, long refId, long messageId, int size) {
    	Criteria criteria = Criteria.where("refIds").all(refIds).and("hideRefIds").ne(refId);
    	if (messageId > 0) {
    		criteria.and("id").lt(messageId);
    	}
    	Sort sort = new Sort(Direction.DESC, "id");
    	return mongoOps.find(new Query(criteria).with(sort).limit(size), Message2.class);
    }

    public List<Message2> getListFromMessageId(List<Long> refIds, long refId, long messageId, int size) {
        Criteria criteria = Criteria.where("refIds").all(refIds).and("hideRefIds").ne(refId);
        if (messageId > 0) {
            criteria.and("id").gt(messageId);
        }
        Sort sort = new Sort(Direction.ASC, "id");
        return mongoOps.find(new Query(criteria).with(sort).limit(size), Message2.class);
    }

    public List<Message2> getListToMessageId(long refId, long messageId, int size) {
        Criteria criteria = Criteria.where("refIds").is(refId).and("hideRefIds").ne(refId);
        if (messageId > 0) {
            criteria.and("id").lt(messageId);
        }
        Sort sort = new Sort(Direction.DESC, "id");
        return mongoOps.find(new Query(criteria).with(sort).limit(size), Message2.class);
    }

    public List<Message2> getLastUpdateStatus(long refId, long messageId, Date lastUpdate, int size) {
        Criteria criteria = Criteria.where("refIds").is(refId).and("updatedDate").gt(lastUpdate);
        if (messageId > 0) {
            criteria.and("id").lte(messageId);
        }
        Sort sort = new Sort(Direction.DESC, "id");
        Query query = new Query(criteria).with(sort).limit(size);
        query.fields().include("id").include("refIds").include("hideRefIds").include("status");
        return mongoOps.find(query, Message2.class);
    }

    public List<Message2> getListFromMessageId(long refId, long messageId, int size) {
        Criteria criteria = Criteria.where("refIds").is(refId).and("hideRefIds").ne(refId);
        if (messageId > 0) {
            criteria.and("id").gt(messageId);
        }
        Sort sort = new Sort(Direction.ASC, "id");
        return mongoOps.find(new Query(criteria).with(sort).limit(size), Message2.class);
    }
    
    public List<Message2> getList(List<Long> refIds, int from, int count) {
    	Criteria criteria = Criteria.where("refIds").all(refIds);
    	Sort sort = new Sort(Direction.DESC, "id"); 
    	return mongoOps.find(new Query(criteria).with(sort).skip(from).limit(count), Message2.class);
    }

    public int updateStatus(long id, int status) {
        Update update = new Update();
        update.set("status", status);
        update.set("updatedDate", new Date());
        WriteResult rs = mongoOps.updateFirst(new Query(Criteria.where("id").is(id)), update, Message2.class);
        return rs.getN();
    }

    public int hideUsers(long fromUserId, List<Long> toUserIds) {
        Criteria criteria = new Criteria().andOperator(Criteria.where("refIds").is(fromUserId), Criteria.where("refIds").in(toUserIds))
                .and("hideRefIds").ne(fromUserId);
        Update update = new Update();
        update.push("hideRefIds", fromUserId);
        update.set("updatedDate", new Date());
        WriteResult rs = mongoOps.updateMulti(new Query(criteria), update, Message2.class);
        return rs.getN();
    }

}
