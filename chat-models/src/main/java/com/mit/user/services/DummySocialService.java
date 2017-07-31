package com.mit.user.services;

import com.mit.http.exception.UnsupportSocialTypeException;
import com.mit.user.entities.SocialProfile;

/**
 * Created by Hung Le on 2/15/17.
 */
public class DummySocialService extends SocialService {
    public DummySocialService(String token) {
        super(token);
    }

    @Override
    public SocialProfile getProfile() throws Exception {
        throw new UnsupportSocialTypeException();
    }
}
