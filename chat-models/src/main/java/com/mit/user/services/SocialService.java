package com.mit.user.services;

import com.mit.http.context.ApplicationContextProvider;
import com.mit.user.entities.SocialProfile;
import com.mit.user.entities.UserSocial;
import com.mit.user.repositories.SocialProfileRepo;
import com.mit.user.repositories.UserSocialRepo;
import com.mit.user.responses.LoginToken;
import com.mit.user.responses.SocialProfileResponse;

/**
 * Created by Hung Le on 2/15/17.
 */

public abstract class SocialService {
    protected UserSocialRepo userSocialRepo;

    protected String token;
    protected SocialProfile socialProfile;
    private SocialProfileRepo socialProfileRepo;

    public SocialService(String token) {
        this.token = token;

        userSocialRepo = ApplicationContextProvider.getApplicationContext().getBean(UserSocialRepo.class);
        socialProfileRepo = ApplicationContextProvider.getApplicationContext().getBean(SocialProfileRepo.class);
    }

    public String getToken() {
        return token;
    }

    public abstract SocialProfile getProfile() throws Exception;

    public UserSocial signIn() throws Exception {
        socialProfile = socialProfileRepo.getByToken(token);
        if (socialProfile == null) {
            socialProfile = getProfile();
        }

        UserSocial userSocial = getUserSocial(socialProfile.getId(), socialProfile.getType());
        if (userSocial == null) {
            socialProfileRepo.save(socialProfile);
        }

        return userSocial;
    }

    public UserSocial getUserSocial(String socialId, int type) {
        UserSocial userSocial = userSocialRepo.getUserSocial(type, socialId);
        return userSocial;
    }

    public LoginToken generateToken(int appType) throws Exception {
        LoginToken loginToken = new LoginToken();
        loginToken.setToken(token);
        loginToken.setSocialProfile(new SocialProfileResponse(socialProfile));

        return loginToken;
    }
}
