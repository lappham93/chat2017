package com.mit.session.services;

import com.mit.caches.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hung on 6/25/2017.
 */

@Service
public class OnlineUserService {
    @Autowired
    private Cache cache;

    private final String _prefixSocketUser = "mmd.socket.user.";

    public void addUserSession(String userId, String session) {
        String userSessionKey = getUserSessionKey(userId);
        cache.incAtomic(userSessionKey);
    }

    public void deleteUserSession(String userId, String session) {
        String userSessionKey = getUserSessionKey(userId);
        cache.decAtomic(userSessionKey);
    }

    public boolean isOnline(String userId) {
        String userSessionKey = getUserSessionKey(userId);
        return cache.getAtomic(userSessionKey) > 0;
    }

    public void clearSession() {
        cache.clear(_prefixSocketUser);
    }

    private String getUserSessionKey(String userId) {
        return _prefixSocketUser + userId;
    }
}
