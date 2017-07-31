package com.mit.user.services;

import com.mit.user.enums.SocialType;

/**
 * Created by Hung Le on 2/15/17.
 */
public class SocialFactory {

    public static SocialService getInstance(int type, String token) {
        SocialService service = null;
        if (type == SocialType.ACCOUNT_KIT.getType()) {
            service = new AccountKitService(token);
        } else if (type == SocialType.GOOGLE.getType()) {
            service = new GoogleService(token);
        } else if (type == SocialType.FACEBOOK.getType()) {
            service = new FacebookService(token);
        } else {
            service = new DummySocialService(token);
        }

        return service;
    }
}
