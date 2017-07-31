package com.mit.seq.repositories;

import com.mit.http.exception.SequenceException;
import com.mit.seq.entities.SeqId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by Hung Le on 2/16/17.
 */

@Repository
public class SeqIdRepo {
    @Autowired
    private MongoOperations mongoOps;

    public long getNextSequenceId(String key) throws SequenceException {
        //get sequence id
        Query query = new Query(Criteria.where("id").is(key));

        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        options.upsert(true);

        //this is the magic happened.
        SeqId seqId = mongoOps.findAndModify(query, update, options, SeqId.class);

        //if no id, throws SequenceException
        //optional, just a way to tell user when the sequence id is failed to generate.
        if (seqId == null) {
            throw new SequenceException("Unable to get sequence id for key : " + key);
        }

        return seqId.getSeq();
    }

    public int getNextSequenceIdInt(String key) throws SequenceException {
        return (int)getNextSequenceId(key);
    }
}
