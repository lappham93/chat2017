package com.mit.caches;

import java.util.Date;

/**
 * Created by Hung Le on 2/13/17.
 */
public interface Cache {

    boolean add(String key, Object data);
    boolean add(String key, Object data, int expire);
    boolean add(String key, Object data, Date expire);
    Object get(String key);
    boolean expire(String key, int expire);
    boolean expire(String key, Date expire);
    boolean delete(String key);
    long incAtomic(String key);
    long getAtomic(String key);
    long decAtomic(String key);
    void clear(String keyPrefix);
}
