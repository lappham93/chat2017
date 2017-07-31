package com.mit.caches;

import com.mit.utils.ZonedDateTimeUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hung Le on 2/13/17.
 */
public class RedisCache implements Cache {

    private RedisTemplate<String, Object> template;

    public RedisCache(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public boolean add(String key, Object data) {
        template.boundValueOps(key).set(data);
        return true;
    }

    public boolean add(String key, Object data, int expire) {
        template.boundValueOps(key).set(data, expire, TimeUnit.SECONDS);
        return true;
    }

    public boolean add(String key, Object data, Date expire) {
        ZonedDateTime now = ZonedDateTimeUtils.now();
        long expireInSecond = (expire.getTime() / 1000) - now.toEpochSecond();
        template.boundValueOps(key).set(data, expireInSecond, TimeUnit.SECONDS);
        return true;
    }

    public Object get(String key) {
        return template.boundValueOps(key).get();
    }

    public boolean expire(String key, int expire) {
        template.boundValueOps(key).expire(expire, TimeUnit.SECONDS);
        return false;
    }

    public boolean expire(String key, Date expire) {
        ZonedDateTime now = ZonedDateTimeUtils.now();
        long expireInSecond = (expire.getTime() / 1000) - now.toEpochSecond();
        template.boundValueOps(key).expire(expireInSecond, TimeUnit.SECONDS);
        return false;
    }

    public boolean delete(String key) {
        template.delete(key);
        return false;
    }

    public long incAtomic(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, template.getConnectionFactory());
        return counter.incrementAndGet();
    }

    public long decAtomic(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, template.getConnectionFactory());
        return counter.decrementAndGet();
    }

    public long getAtomic(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, template.getConnectionFactory());
        return counter.get();
    }

    public void clear(String keyPrefix) {
        String pattern = keyPrefix + "*";
        Set<String> keys = template.keys(pattern);
        for (String key: keys) {
            template.delete(key);
        }
    }
}
