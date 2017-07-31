package com.mit.notification.repositories;

import com.mit.http.exception.SequenceException;
import com.mit.notification.entities.DeviceInfo;
import com.mit.seq.repositories.SeqIdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Hung Le on 4/4/17.
 */

@Repository
public class DeviceInfoRepo {
    @Autowired
    protected MongoOperations mongoOps;

    @Autowired
    protected SeqIdRepo seqIdRepo;

    public List<DeviceInfo> getByUserId(long userId, int appType) {
        return mongoOps.find(new Query(Criteria.where("userId").is(userId).and("appType").is(appType)), DeviceInfo.class);
    }

    public List<DeviceInfo> getByUserIds(List<Long> userIds, int appType) {
        return mongoOps.find(new Query(Criteria.where("userId").in(userIds).and("appType").is(appType)), DeviceInfo.class);
    }

    public List<DeviceInfo> getAll() {
        return mongoOps.find(new Query(), DeviceInfo.class);
    }

    public void save(DeviceInfo deviceInfo) throws SequenceException {
        mongoOps.save(deviceInfo);
    }

    public void deleteById(String id) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        mongoOps.remove(deviceInfo);
    }

    public void deleteByUser(long userId) {
        mongoOps.remove(new Query(Criteria.where("userId").is(userId)), DeviceInfo.class);
    }

    public void deleteByUserAndAppType(long userId, int appType) {
        mongoOps.remove(new Query(Criteria.where("userId").is(userId).and("appType").is(appType)), DeviceInfo.class);
    }
}
