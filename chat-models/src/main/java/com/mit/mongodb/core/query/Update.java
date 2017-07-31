package com.mit.mongodb.core.query;

/**
 * Created by Hung Le on 4/19/17.
 */
public class Update extends org.springframework.data.mongodb.core.query.Update {
    @Override
    public void addMultiFieldOperation(String operator, String key, Object value) {
        super.addMultiFieldOperation(operator, key, value);
    }
}
