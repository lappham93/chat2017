package com.mit.session.entities;

import com.mit.caches.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hung Le on 2/13/17.
 */
@Service
public class SessionCache {
    @Autowired
    private Cache cache;

    private final String _prefixSession = "mmd.session.";
    private final String _prefixSessionUser = "mmd.session.user.";

    public boolean addSession(String token, UserSession userSession, int expireTime) {
        return cache.add(getSessionKey(token), userSession, expireTime);
    }

    public boolean deleteSession(String token) {
        return cache.delete(getSessionKey(token));
    }

    public UserSession getSession(String token) {
        return (UserSession)cache.get(getSessionKey(token));
    }

    public void addUserSession(String userId, String token, int expireTime) {
        String userSessionKey = getUserSessionKey(userId);
        List<String> tokens = (List)cache.get(userSessionKey);
        if (tokens != null && !tokens.isEmpty()) {
            Iterator<String> tokenI = tokens.iterator();
            while (tokenI.hasNext()) {
                String tk = tokenI.next();
                if (getSession(tk) == null) {
                    tokenI.remove();
                }
            }
        } else {
        	tokens = new LinkedList<>();
        }
        tokens.add(token);
        cache.add(userSessionKey, tokens, expireTime);
    }

    public void removeUserSession(String userId, String token) {
        String userSessionKey = getUserSessionKey(userId);
        List<String> tokens = (List)cache.get(userSessionKey);
        if (tokens != null && !tokens.isEmpty()) {
            Iterator<String> tokenI = tokens.iterator();
            while (tokenI.hasNext()) {
                String tk = tokenI.next();
                if (getSession(tk) == null || token.equals(tk)) {
                    tokenI.remove();
                }
            }
            cache.add(userSessionKey, tokens);
        }
    }

    public void deleteUserSession(String userId, String token) {
        String userSessionKey = getUserSessionKey(userId);
        List<String> tokens = (List)cache.get(userSessionKey);
        if (tokens != null && !tokens.isEmpty()) {
            deleteSession(token);
            tokens.remove(token);
            cache.add(userSessionKey, tokens);
        }
    }

    public void deleteAllUserSession(String userId) {
        String userSessionKey = getUserSessionKey(userId);
        List<String> tokens = (List)cache.get(userSessionKey);
        if (tokens != null && !tokens.isEmpty()) {
            for (String token: tokens) {
                deleteSession(token);
            }
            cache.delete(userSessionKey);
        }
    }

    public void deleteAllUserSession(String userId, int appType) {
        String userSessionKey = getUserSessionKey(userId);
        List<String> tokens = (List)cache.get(userSessionKey);
        if (tokens != null && !tokens.isEmpty()) {
            Iterator<String> tokenI = tokens.iterator();
            while (tokenI.hasNext()) {
                String token = tokenI.next();
                UserSession userSession = getSession(token);
                if (userSession == null || userSession.getAppType() == appType) {
                    if (userSession != null) {
                        deleteSession(token);
                    }

                    tokenI.remove();
                }
            }

            cache.add(userSessionKey, tokens);
        }
    }

    private String getSessionKey(String token) {
        return _prefixSession + token;
    }

    private String getUserSessionKey(String userId) {
        return _prefixSessionUser + userId;
    }
}
