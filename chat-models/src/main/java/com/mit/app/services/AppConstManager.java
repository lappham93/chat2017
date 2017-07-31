package com.mit.app.services;

import com.mit.app.entities.AppConst;
import com.mit.app.entities.AppConstCache;
import com.mit.app.enums.AppConstKey;
import com.mit.app.repositories.AppConstRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hung Le on 3/20/17.
 */

@Service
public class AppConstManager {

    @Autowired
    AppConstRepo appConstRepo;
    @Autowired
    AppConstCache appConstCache;

    public String getString(AppConstKey appConstKey) {
        return (String)get(appConstKey);
    }

    public Integer getInt(AppConstKey appConstKey) {
        return (Integer)get(appConstKey);
    }

    public Long getLong(AppConstKey appConstKey) {
        Object val = get(appConstKey);
        if (val instanceof Integer) {
            return ((Integer) val).longValue();
        }
        return (Long)val;
    }

    public Double getDouble(AppConstKey appConstKey) {
        return (Double) get(appConstKey);
    }

    public BigDecimal getBigDecimal(AppConstKey appConstKey) {
        return (BigDecimal)get(appConstKey);
    }

    public List<String> getListString(AppConstKey appConstKey) {
        return (List)get(appConstKey);
    }

    public List<Integer> getListInt(AppConstKey appConstKey) {
        return (List)get(appConstKey);
    }

    public List<Long> getListLong(AppConstKey appConstKey) {
        return (List)get(appConstKey);
    }

    public List<BigDecimal> getListBigDecimal(AppConstKey appConstKey) {
        return (List)get(appConstKey);
    }

    public Map<String, String> getMapString(AppConstKey appConstKey) {
        return (Map)get(appConstKey);
    }

    public Set<String> getSetString(AppConstKey appConstKey) {
        return (Set)get(appConstKey);
    }

    public Date getDate(AppConstKey appConstKey) {
        return (Date) get(appConstKey);
    }

    public Object get(AppConstKey appConstKey) {
        String key = appConstKey.getKey();
        Object value = appConstCache.get(key);
        if (value == null) {
            AppConst appConst = appConstRepo.getByKey(key);
            if (appConst != null) {
                value = appConst.getValue();
                if (!appConst.isNoCache()) {
                    appConstCache.add(key, value);
                }
            }
        }

        return value;
    }

    public void deleteCache(AppConstKey appConstKey) {
        appConstCache.delete(appConstKey.getKey());
    }
    
    public void deleteCache(String key) {
        appConstCache.delete(key);
    }
}
