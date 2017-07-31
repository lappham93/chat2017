package com.mit.sessions;

import javax.servlet.http.HttpServletRequest;

import com.mit.session.entities.UserSession;
import com.mit.user.entities.AdminProfile;

public class AdminSession {
	private static final int expireTimeInSec = 7 * 24 * 60 * 60;
	private static final String sessionKeyKey = "sessionKey";
	private static final String refreshTokenKey = "refreshToken";
	private static final String sessionKeyAttrKey = "sessionKey";
	private static final String sessionAttrKey = "session";
	private static final String profileAttrKey = "profile";
	
	public static void setSession(HttpServletRequest req, String sessionKey, String refreshToken) {
		req.getSession().setAttribute(sessionKeyKey, sessionKey);
		req.getSession().setAttribute(refreshTokenKey, refreshToken);
		req.getSession().setMaxInactiveInterval(expireTimeInSec);
	}
	
	public static void removeSession(HttpServletRequest req) {
		req.getSession().removeAttribute(sessionKeyKey);
		req.getSession().removeAttribute(refreshTokenKey);
	}
	
	public static String getSessionKey(HttpServletRequest req) {
		if (req.getSession().getAttribute(sessionKeyKey) != null) {
			return String.valueOf(req.getSession().getAttribute(sessionKeyKey));
		}
		return "";
	}
	
	public static String getRefreshToken(HttpServletRequest req) {
		if (req.getSession().getAttribute(refreshTokenKey) != null) {
			return String.valueOf(req.getSession().getAttribute(refreshTokenKey));
		}
		return "";
	}
	
	public static void setAttribute(HttpServletRequest req, String sessionKey, Object session) {
		req.setAttribute(sessionKeyAttrKey, sessionKey);
		req.setAttribute(sessionAttrKey, session);
	}
	
	public static UserSession getUserSession(HttpServletRequest req) {
		return (UserSession) req.getAttribute(sessionAttrKey);
	}
	
	public static void setProfile(HttpServletRequest req, AdminProfile profile) {
		req.setAttribute(profileAttrKey, profile);
	}
	
	public static AdminProfile getProfile(HttpServletRequest req) {
		return (AdminProfile) req.getAttribute(profileAttrKey);
	}
}
