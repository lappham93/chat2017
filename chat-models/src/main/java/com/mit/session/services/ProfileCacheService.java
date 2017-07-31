package com.mit.session.services;

import com.mit.caches.Cache;
import com.mit.session.entities.ProfileCache;
import com.mit.user.entities.Profile;
import com.mit.user.repositories.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hung on 6/25/2017.
 */

@Service
public class ProfileCacheService {
    @Autowired
    private Cache cache;
    @Autowired
    private ProfileRepo profileRepo;

    private final String _prefixProfileUser = "mmd.profile.user.";
    private final int EXPIRE_TIME = 24 * 60 * 60;

    public ProfileCache getProfile(long userId) {
        String userSessionKey = getUserProfileKey(userId);
        ProfileCache profileCache = (ProfileCache)cache.get(userSessionKey);
        if (profileCache == null) {
            Profile profile = profileRepo.getById(userId);
            if (profile != null) {
                profileCache = new ProfileCache(profile);
                cache.add(userSessionKey, profileCache, EXPIRE_TIME);
            }
        }
    	return profileCache;
    }

    public void deleteCache(long userId) {
        String userSessionKey = getUserProfileKey(userId);
        cache.delete(userSessionKey);
    }

    private String getUserProfileKey(long userId) {
        return _prefixProfileUser + userId;
    }
}
