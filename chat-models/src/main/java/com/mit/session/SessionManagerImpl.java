package com.mit.session;

import com.firebase.security.token.TokenGenerator;
import com.mit.http.session.SessionManager;
import com.mit.session.entities.RefreshToken;
import com.mit.session.entities.SessionCache;
import com.mit.session.entities.UserSession;
import com.mit.session.enums.TokenType;
import com.mit.session.repositories.RefreshTokenRepo;
import com.mit.user.entities.Profile;
import com.mit.utils.ZonedDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hung Le on 2/13/17.
 */

@Service
public class SessionManagerImpl implements SessionManager<UserSession> {
    public static final SessionManagerImpl Instance = new SessionManagerImpl();

    @Value("${firebase.secret}")
    private String firebaseSecret;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    private final int expireTime = 24 * 60 * 60;
    private final int refreshTokenExpireTime = 30 * 24 * 60 * 60;

    private String generateToken(long userId, int profileType, int tokenType) {
        Map<String, Object> authPayload = new HashMap<String, Object>();
        authPayload.put("uid", userId + "");
        authPayload.put("tokenType", tokenType);
        authPayload.put("profileType", profileType);
        authPayload.put("expireTime", System.currentTimeMillis() + expireTime * 1000);

        TokenGenerator tokenGenerator = new TokenGenerator(firebaseSecret);
        String token = tokenGenerator.createToken(authPayload);
        return token;
    }

    public String generateSession(Profile profile, int appType) {
        String token = generateToken(profile.getId(), profile.getProfileType(), TokenType.ACCESS_TOKEN.getValue());

        UserSession userSession = new UserSession();
        userSession.setUserId(profile.getId());
        userSession.setAppType(appType);
        userSession.setProfileType(profile.getProfileType());

        String tokenKey = profile.getClass().getSimpleName() + "." + token;
        boolean succ = sessionCache.addSession(tokenKey, userSession, expireTime);

        if (succ) {
            String userKey = profile.getClass().getSimpleName() + "." + profile.getId();
            sessionCache.addUserSession(userKey, tokenKey, expireTime);
            return token;
        }

        return null;
    }

    public void expireSession(String token, Class<? extends Profile> cls) {
        String tokenKey = cls.getSimpleName() + "." + token;
        UserSession userSession = sessionCache.getSession(tokenKey);
        sessionCache.deleteSession(tokenKey);
        String userKey = cls.getSimpleName() + "." + userSession.getUserId();
        sessionCache.removeUserSession(userKey, token);
    }

    public void expireAllUserSession(long userId, Class<? extends Profile> cls) {
        String userKey = cls.getSimpleName() + "." + userId;
        sessionCache.deleteAllUserSession(userKey);
    }

    public void expireAllUserSession(long userId, int appType, Class<? extends Profile> cls) {
        String userKey = cls.getSimpleName() + "." + userId;
        sessionCache.deleteAllUserSession(userKey, appType);
    }

    public String generateRefreshToken(Profile profile, int appType) {
        String token = generateToken(profile.getId(), profile.getProfileType(), TokenType.REFRESH_TOKEN.getValue());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserId(profile.getId());
        refreshToken.setAppType(appType);
        refreshToken.setExpireDate(ZonedDateTimeUtils.toDate(ZonedDateTimeUtils.now().plusSeconds(refreshTokenExpireTime)));
        refreshTokenRepo.save(refreshToken);

        return token;
    }
    
    public void updateSession(String sessionKey, UserSession userSession) {
        sessionCache.addSession(sessionKey, userSession, expireTime);
    }

    public UserSession get(String sessionKey) {
        return sessionCache.getSession(sessionKey);
    }

    public RefreshToken getByRefreshToken(String refreshToken) {
        return refreshTokenRepo.getByToken(refreshToken);
    }

    public int expireRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepo.remove(refreshToken);
    }

    public int expireRefreshToken(String refreshToken) {
        return refreshTokenRepo.remove(refreshToken);
    }

    public int expireAllRefreshToken(long userId) {
        return refreshTokenRepo.removeByUserId(userId);
    }

    public int expireAllRefreshToken(long userId, int appType) {
        return refreshTokenRepo.removeByUserIdAndAppType(userId, appType);
    }
}
