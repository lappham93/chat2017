package com.mit.app.http;

import com.mit.app.entities.AppKey;
import com.mit.caches.Cache;
import com.mit.http.RequestVerificationManager;
import com.mit.utils.HmacDigestUtils;
import com.mit.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hung Le on 2/13/17.
 */

@Service
public class RequestVerifyImpl implements RequestVerificationManager {
    Logger logger = LoggerFactory.getLogger("requestLogging");

    private String _cachePrefix = "homber.appkey.";
    Map<String, AppKey> appKeyMap = new HashMap<>();

    @Autowired
    private Cache cache;

    @Value("${system.test.signature}")
    private String testSign;

    @Override
    public boolean isAllowRequest(String apiKey, String data, String sign) {
        boolean isAllow = false;
        if (sign.equals(testSign)) {
            isAllow = true;
        } else {
            AppKey appKey = getAppKey(apiKey);
            if (appKey != null) {
                if (!appKey.isLocal()) {
                    String serverSign = HmacDigestUtils.sign(appKey.getSecrectKey(), data);
                    logger.info("apiKey -- " + apiKey + " -- secretKey -- " + appKey.getSecrectKey() + " -- serverSign -- " + serverSign);
                    if (serverSign.equals(sign)) {
                        isAllow = true;
                    }
                } else {
                    isAllow = true;
                }
            }
        }

        return isAllow;
    }

    public void addApiKeyToCache(AppKey appKey) {
        String data = JsonUtils.Instance.toJson(appKey);
        cache.add(getKey(appKey.getApiKey()), data);
    }

    public AppKey getAppKey(String apiKey) {
        AppKey appKey = appKeyMap.get(apiKey);
        if (appKey == null) {
            String cacheAppkey = (String) cache.get(getKey(apiKey));
            if (!StringUtils.isEmpty(cacheAppkey))
                appKey = JsonUtils.Instance.getObject(AppKey.class, cacheAppkey);
        }

        return appKey;
    }

    private String getKey(String key) {
        return _cachePrefix + key;
    }

    @Override
    public String getAppName(String apiKey) {
        AppKey appKey = getAppKey(apiKey);
        if (appKey != null) {
            return appKey.getAppName();
        }

        return null;
    }
}
