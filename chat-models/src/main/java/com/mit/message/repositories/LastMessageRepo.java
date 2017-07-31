package com.mit.message.repositories;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.message.entities.LastMessage;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LastMessageRepo extends TimeDocRepo<LastMessage> {

    @Autowired
    MongoOperations mongoOps;
    
    public LastMessage get(List<Long> refIds) {
    	Criteria crit = Criteria.where("refIds").all(refIds);
    	return mongoOps.findOne(new Query(crit), LastMessage.class);
    }
    
    public List<LastMessage> getListToMessageId(long refId, long messageId, int count) {
    	Criteria criteria = Criteria.where("refIds").is(refId).and("hideRefIds").ne(refId);
    	if (messageId > 0) {
    		criteria.and("messageId").lt(messageId);
    	}
    	Sort sort = new Sort(Direction.DESC, "messageId");
    	return mongoOps.find(new Query(criteria).with(sort).limit(count), LastMessage.class);
    }

    public List<LastMessage> getListFromMessageId(long refId, long messageId, int count) {
        Criteria criteria = Criteria.where("refIds").is(refId).and("hideRefIds").ne(refId);
        if (messageId > 0) {
            criteria.and("messageId").gt(messageId);
        }
        Sort sort = new Sort(Direction.ASC, "messageId");
        return mongoOps.find(new Query(criteria).with(sort).limit(count), LastMessage.class);
    }
    
    public List<LastMessage> getList(long refId, int from, int size) {
    	Criteria criteria = Criteria.where("refIds").is(refId);
    	Sort sort = new Sort(Direction.DESC, "messageId");
    	return mongoOps.find(new Query(criteria).with(sort).skip(from).limit(size), LastMessage.class);
    }

    public int resetNewCount(long fromUserId, List<Long> toUserIds) {
        Criteria criteria = new Criteria().andOperator(Criteria.where("refIds").is(fromUserId), Criteria.where("refIds").in(toUserIds))
                .and("hideRefIds").ne(fromUserId).and("refId").is(fromUserId).and("newCount").gt(0);
        Update update = new Update();
        update.push("newCount", 0);
        WriteResult rs = mongoOps.updateMulti(new Query(criteria), update, LastMessage.class);
        return rs.getN();
    }

    public int hideUsers(long fromUserId, List<Long> toUserIds) {
        Criteria criteria = new Criteria().andOperator(Criteria.where("refIds").is(fromUserId), Criteria.where("refIds").in(toUserIds)).and("hideRefIds").ne(fromUserId);
        Update update = new Update();
        update.push("hideRefIds", fromUserId);
        WriteResult rs = mongoOps.updateMulti(new Query(criteria), update, LastMessage.class);
        return rs.getN();
    }
}
