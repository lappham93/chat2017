package com.mit.upload.repositories;

import com.mit.http.exception.SequenceException;
import com.mit.seq.repositories.SeqIdRepo;
import com.mit.upload.entities.UploadTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by Hung on 3/24/2017.
 */

@Repository
public class UploadTempRepo {

    @Autowired
    MongoOperations mongoOps;
    @Autowired
    SeqIdRepo seqIdRepo;

    public UploadTemp getById(long id) {
        return mongoOps.findById(id, UploadTemp.class);
    }

    public UploadTemp updateData(long id, int index, long tempFile, int dateLength) {
        Update update = new Update();
        update.set("tempFiles." + index, tempFile)
                .set("check." + index, true)
                .inc("totalComplete", 1)
                .inc("sizeComplete", dateLength);
        UploadTemp uploadTemp = mongoOps.findAndModify(new Query(Criteria.where("id").is(id)), update, new FindAndModifyOptions().returnNew(true), UploadTemp.class);
        return uploadTemp;
    }

    public void save(UploadTemp uploadTemp) throws SequenceException {
        if (uploadTemp.getId() <= 0) {
            uploadTemp.setId(seqIdRepo.getNextSequenceId(mongoOps.getCollectionName(uploadTemp.getClass())));
        }
        mongoOps.save(uploadTemp);
    }
}
