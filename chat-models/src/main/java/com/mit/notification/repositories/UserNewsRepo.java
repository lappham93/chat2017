package com.mit.notification.repositories;

import com.mit.common.repositories.TimeDocRepo;
import com.mit.notification.entities.UserNews;
import com.mit.notification.entities.UserNewsItem;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hung Le on 4/5/17.
 */

@Repository
public class UserNewsRepo extends TimeDocRepo<UserNews> {
    @Autowired
    protected MongoOperations mongoOps;

    public int addItem(long userId, UserNewsItem item) {
        Update update = new Update();
        update.push("items").atPosition(0).each(item);
        update.inc("newCount", 1);
        Query query = new Query(Criteria.where("id").is(userId));
        WriteResult writeRs = mongoOps.upsert(query, update, UserNews.class);
        return writeRs.getN();
    }

    public int addProviderItem(long userId, UserNewsItem item) {
        Update update = new Update();
        update.push("providerItems").atPosition(0).each(item);
        update.inc("providerNewCount", 1);
        Query query = new Query(Criteria.where("id").is(userId));
        WriteResult writeRs = mongoOps.upsert(query, update, UserNews.class);
        return writeRs.getN();
    }

    public int addItemSocial(long userId, UserNewsItem item) {
        clearItem(userId, item.getId());
        return addItem(userId, item);
    }

    public int clearItem(long id, long itemId) {
        Update update = new Update();
        update.pull("items", new BasicDBObject("id", itemId));

        WriteResult writeRs = mongoOps.updateFirst(new Query(Criteria.where("id").is(id)), update, UserNews.class);
        return writeRs.getN();
    }

    public long addItems(long userId, List<UserNewsItem> items) {
        Update update = new Update();
        update.push("items").atPosition(0).each(items);
        update.inc("newCount", items.size());
        Query query = new Query(Criteria.where("id").is(userId));
        WriteResult writeRs = mongoOps.upsert(query, update, UserNews.class);
        return writeRs.getN();
    }

    public long addItemToUserList(List<Long> userIds, UserNewsItem item) {
        BulkOperations bulkOps = mongoOps.bulkOps(BulkOperations.BulkMode.UNORDERED, UserNews.class);

        BasicDBObject dbObjectItem = new BasicDBObject();
        mongoOps.getConverter().write(item, dbObjectItem);
        dbObjectItem.remove("_class");
        BasicDBObject dbObject = new BasicDBObject("$push", new BasicDBObject("items", new BasicDBObject("$each", Arrays.asList(dbObjectItem)).append("$position", 0)))
                .append("$inc", new BasicDBObject("newCount", 1));
        Update update = Update.fromDBObject(dbObject);
//        update.push("items").atPosition(0).each(item);
//        update.inc("newCount", 1);
        for (long userId : userIds) {
            Query query = new Query(Criteria.where("_id").is(userId));
            bulkOps.upsert(query, update);
        }

        BulkWriteResult bulkWriteRs = bulkOps.execute();
        return bulkWriteRs.getMatchedCount();
    }
    
    public long addProviderItemToUserList(List<Long> userIds, UserNewsItem item) {
        BulkOperations bulkOps = mongoOps.bulkOps(BulkOperations.BulkMode.UNORDERED, UserNews.class);

        BasicDBObject dbObjectItem = new BasicDBObject();
        mongoOps.getConverter().write(item, dbObjectItem);
        dbObjectItem.remove("_class");
        BasicDBObject dbObject = new BasicDBObject("$push", new BasicDBObject("providerItems", new BasicDBObject("$each", Arrays.asList(dbObjectItem)).append("$position", 0)))
                .append("$inc", new BasicDBObject("newCount", 1));
        Update update = Update.fromDBObject(dbObject);
//        update.push("items").atPosition(0).each(item);
//        update.inc("newCount", 1);
        for (long userId : userIds) {
            Query query = new Query(Criteria.where("_id").is(userId));
            bulkOps.upsert(query, update);
        }

        BulkWriteResult bulkWriteRs = bulkOps.execute();
        return bulkWriteRs.getMatchedCount();
    }

    public UserNews getById(long userId, int skip, int limit) {
        Query query = new Query(Criteria.where("id").is(userId));
        query.fields().slice("items", skip, limit);

        return mongoOps.findOne(query, UserNews.class);
    }

    public UserNews getProviderById(long userId, int skip, int limit) {
        Query query = new Query(Criteria.where("id").is(userId));
        query.fields().slice("providerItems", skip, limit);

        return mongoOps.findOne(query, UserNews.class);
    }

    public UserNews getByNewsId(long userId, long newsId) {
        Query query = new Query(Criteria.where("id").is(userId).and("items.id").is(newsId));
        query.fields().elemMatch("items", Criteria.where("id").is(newsId));

        return mongoOps.findOne(query, UserNews.class);
    }

    public UserNews getProviderByNewsId(long userId, long newsId) {
        Query query = new Query(Criteria.where("id").is(userId).and("providerItems.id").is(newsId));
        query.fields().elemMatch("providerItems", Criteria.where("id").is(newsId));

        return mongoOps.findOne(query, UserNews.class);
    }

    public int getNewCount(long userId) {
        Query query = new Query(Criteria.where("id").is(userId));
        query.fields().include("newCount");
        UserNews userNews = mongoOps.findOne(query, UserNews.class);

        if (userNews != null) {
            return userNews.getNewCount();
        }

        return 0;
    }

    public int getProviderNewCount(long userId) {
        Query query = new Query(Criteria.where("id").is(userId));
        query.fields().include("providerNewCount");
        UserNews userNews = mongoOps.findOne(query, UserNews.class);

        if (userNews != null) {
            return userNews.getProviderNewCount();
        }

        return 0;
    }

    public void resetNewCount(long userId) {
        Update update = new Update();
        update.set("newCount", 0);

        Query query = new Query(Criteria.where("id").is(userId));
        mongoOps.updateFirst(query, update, UserNews.class);
    }

    public void resetProviderNewCount(long userId) {
        Update update = new Update();
        update.set("providerNewCount", 0);

        Query query = new Query(Criteria.where("id").is(userId));
        mongoOps.updateFirst(query, update, UserNews.class);
    }

    public void updateView(long userId, List<Long> ids) {
        BulkOperations bulkOps = mongoOps.bulkOps(BulkOperations.BulkMode.UNORDERED, UserNews.class);

        for (long id : ids) {
            Update update = new Update();
            update.set("items.$.viewed", true);
            Query query = new Query(Criteria.where("_id").is(userId).and("items._id").is(id));
            bulkOps.updateOne(query, update);
        }

        BulkWriteResult bulkWriteRs = bulkOps.execute();
        int modifiedCount = bulkWriteRs.getModifiedCount();

        if (modifiedCount > 0) {
            Update update = new Update();
            update.inc("newCount", -modifiedCount);

            Query query = new Query(Criteria.where("id").is(userId).and("newCount").gte(modifiedCount));
            mongoOps.updateFirst(query, update, UserNews.class);
        }
    }

    public void updateProviderView(long userId, List<Long> ids) {
        BulkOperations bulkOps = mongoOps.bulkOps(BulkOperations.BulkMode.UNORDERED, UserNews.class);

        for (long id : ids) {
            Update update = new Update();
            update.set("providerItems.$.viewed", true);
            Query query = new Query(Criteria.where("_id").is(userId).and("providerItems._id").is(id));
            bulkOps.updateOne(query, update);
        }

        BulkWriteResult bulkWriteRs = bulkOps.execute();
    }
    
}
