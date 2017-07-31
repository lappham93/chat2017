package com.mit.app.entities;

import com.mit.caches.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hung Le on 3/20/17.
 */

@Service
public class AppConstCache {
    @Autowired
    private Cache cache;

    private static final String KEY_PREFIX = "mit.appconst.";
    private static final int EXPIRE_TIME = 24 * 60 * 60;

    public Object get(String key) {
        key = getAppConstKey(key);
        return cache.get(key);
    }

    public void add(String key, Object value) {
        key = getAppConstKey(key);
        cache.add(key, value, EXPIRE_TIME);
    }

    public void delete(String key) {
        key = getAppConstKey(key);
        cache.delete(key);
    }

    private String getAppConstKey(String key) {
        return KEY_PREFIX + key;
    }
}
