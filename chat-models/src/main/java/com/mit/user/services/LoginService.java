package com.mit.user.services;

import com.mit.app.enums.AppType;
import com.mit.asset.services.PhotoService;
import com.mit.common.enums.ObjectType;
import com.mit.notification.repositories.DeviceInfoRepo;
import com.mit.session.SessionManagerImpl;
import com.mit.session.entities.RefreshToken;
import com.mit.user.bodies.LogoutBody;
import com.mit.user.bodies.SocialSignup;
import com.mit.user.entities.AdminProfile;
import com.mit.user.entities.Profile;
import com.mit.user.entities.User;
import com.mit.user.entities.UserSocial;
import com.mit.user.enums.ProfileType;
import com.mit.user.enums.UserStatus;
import com.mit.user.repositories.AdminProfileRepo;
import com.mit.user.repositories.ProfileRepo;
import com.mit.user.repositories.UserRepo;
import com.mit.user.repositories.UserSocialRepo;
import com.mit.user.responses.LoginToken;
import com.mit.user.responses.ProfileResponse;
import com.mit.user.responses.RefreshTokenResponse;
import com.mit.utils.LinkBuilder;
import com.mit.utils.ZonedDateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hung Le on 2/20/17.
 */

@Service
public class LoginService {
    @Autowired
    private ProfileRepo profileRepo;
    
    @Autowired
    private AdminProfileRepo adminProfileRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserSocialRepo userSocialRepo;

    @Autowired
    private SessionManagerImpl sessionManagerImpl;

    @Autowired
    private DeviceInfoRepo deviceInfoRepo;
    
    @Autowired
    private PhotoService photoService;

    public LoginToken socialLogin(String token, int type) throws Exception {
        return socialLogin(token, type, AppType.CLIENT.getValue());
    }

    public LoginToken socialLogin(String token, int type, int appType) throws Exception {
        SocialService service = SocialFactory.getInstance(type, token);
        UserSocial userSocial = service.signIn();
        LoginToken loginToken = service.generateToken(appType);

        if (userSocial != null) {
            Profile profile = profileRepo.getById(userSocial.getUserId());
            if (appType == AppType.CLIENT.getValue() && banClient(profile)) {
        		return new LoginToken();
            }

            loginToken.setProfile(new ProfileResponse(profile));

//            logoutAllDevice(userSocial.getUserId(), appType);
            RefreshTokenResponse refreshTokenResponse = generateTokens(profile, appType);
            if (refreshTokenResponse == null) {
                return null;
            }

            loginToken.setSessionKey(refreshTokenResponse.getSessionKey());
            loginToken.setRefreshToken(refreshTokenResponse.getRefreshToken());

            loginToken.setApiSocket(LinkBuilder.buildApiSocketLink());
            loginToken.setUploadSocket(LinkBuilder.buildUploadSocketLink());

            return loginToken;
        }

        return loginToken;
    }

    public LoginToken socialRegister(LoginToken loginToken, SocialSignup socialSignup) throws Exception {
        User user = new User();
        userRepo.save(user);

        UserSocial userSocial = loginToken.getSocialProfile().toUserSocial();
        userSocial.setUserId(user.getId());
        userSocialRepo.save(userSocial);
        
        Profile profile = socialSignup.toProfile();
        profile.setId(user.getId());
        profile.setProfileType(ProfileType.CLIENT.getValue());
        profile.setStatus(UserStatus.ACTIVE.getValue());
        profile.setNewed(true);
        if (!StringUtils.isEmpty(loginToken.getSocialProfile().getAvatar())) {
        	byte[] data = photoService.parseThumbnailData(loginToken.getSocialProfile().getAvatar());
        	if (data != null) {
        		long avatarId = photoService.saveDataPhoto(data, loginToken.getSocialProfile().getAvatar(), ObjectType.USER);
        		if (avatarId > 0) {
        			profile.setAvatar(avatarId);
        		}
        	}
        }
        profileRepo.save(profile);
       
        loginToken.setProfile(new ProfileResponse(profile));

        RefreshTokenResponse refreshTokenResponse = generateTokens(profile, AppType.CLIENT.getValue());
        if (refreshTokenResponse == null) {
            return null;
        }

        loginToken.setSessionKey(refreshTokenResponse.getSessionKey());
        loginToken.setRefreshToken(refreshTokenResponse.getRefreshToken());

        loginToken.setApiSocket(LinkBuilder.buildApiSocketLink());

        return loginToken;
    }

    public RefreshTokenResponse generateTokens(Profile profile, int appType) {
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();

        String sessionKey = sessionManagerImpl.generateSession(profile, appType);
        if (sessionKey != null) {
            refreshTokenResponse.setSessionKey(sessionKey);

            String refreshToken = sessionManagerImpl.generateRefreshToken(profile, appType);
            if (refreshToken != null) {
                refreshTokenResponse.setRefreshToken(refreshToken);
            } else {
                return null;
            }
        } else {
            return null;
        }

        return refreshTokenResponse;
    }

    public LoginToken refreshToken(String refreshToken, int appType) {
        LoginToken loginToken = null;
        RefreshToken token = sessionManagerImpl.getByRefreshToken(refreshToken);

        if (token == null || ZonedDateTimeUtils.toDate(ZonedDateTimeUtils.now()).compareTo(token.getExpireDate()) >= 0) {
            return null;
        }

        Profile profile = profileRepo.getById(token.getUserId());
        if (appType == AppType.CLIENT.getValue() && banClient(profile)) {
    		return new LoginToken();
        }
        RefreshTokenResponse refreshTokenResponse = generateTokens(profile, token.getAppType());
        if (refreshTokenResponse != null) {
            sessionManagerImpl.expireRefreshToken(token);

            loginToken = new LoginToken();
            loginToken.setProfile(new ProfileResponse(profile));
            loginToken.setRefreshToken(refreshTokenResponse.getRefreshToken());
            loginToken.setSessionKey(refreshTokenResponse.getSessionKey());

            loginToken.setApiSocket(LinkBuilder.buildApiSocketLink());
            loginToken.setUploadSocket(LinkBuilder.buildUploadSocketLink());
        }

        return loginToken;
    }
    
    public RefreshTokenResponse refreshTokenAdmin(String refreshToken) {
        RefreshToken token = sessionManagerImpl.getByRefreshToken(refreshToken);

        if (token == null || ZonedDateTimeUtils.toDate(ZonedDateTimeUtils.now()).compareTo(token.getExpireDate()) >= 0) {
            return null;
        }

        AdminProfile profile = adminProfileRepo.getById(token.getUserId());
        RefreshTokenResponse refreshTokenResponse = generateTokens(profile, token.getAppType());
        if (refreshTokenResponse != null) {
            sessionManagerImpl.expireRefreshToken(token);
        }

        return refreshTokenResponse;
    }

    public void logout(String sessionKey, LogoutBody body) {
        if (!body.isLogoutAllDevices()) {
            sessionManagerImpl.expireSession(sessionKey, Profile.class);
            sessionManagerImpl.expireRefreshToken(body.getRefreshToken());
            if (!StringUtils.isEmpty(body.getDeviceId())) {
                deviceInfoRepo.deleteById(body.getDeviceId());
            }
        } else {
            RefreshToken token = sessionManagerImpl.getByRefreshToken(body.getRefreshToken());
            logoutAllDevice(token.getUserId());
        }
    }

    public void logoutAllDevice(long userId) {
        sessionManagerImpl.expireAllUserSession(userId, Profile.class);
        sessionManagerImpl.expireAllRefreshToken(userId);
        deviceInfoRepo.deleteByUser(userId);
    }

    public void logoutAllDevice(long userId, int appType) {
        sessionManagerImpl.expireAllUserSession(userId, appType, Profile.class);
        sessionManagerImpl.expireAllRefreshToken(userId, appType);
        deviceInfoRepo.deleteByUserAndAppType(userId, appType);
    }
    
    public boolean banClient(Profile profile) {
    	return profile != null && (profile.getStatus() == UserStatus.INACTIVE.getValue() || profile.getStatus() == UserStatus.BAN.getValue());
    }
}
