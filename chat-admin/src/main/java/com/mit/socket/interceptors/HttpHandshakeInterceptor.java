package com.mit.socket.interceptors;

import com.mit.http.session.SessionManager;
import com.mit.sessions.AdminSession;
import com.mit.utils.JsonUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Created by Hung Le on 3/6/17.
 */
public class HttpHandshakeInterceptor<T> implements HandshakeInterceptor {

    Logger logger = LoggerFactory.getLogger("requestLogging");

    private final SessionManager<T> sessionManager;
    private final String className;

    public HttpHandshakeInterceptor(SessionManager<T> sessionManager, String className) {
        this.sessionManager = sessionManager;
        this.className = className;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        return true;

        if (request instanceof ServletServerHttpRequest) {

            HttpHeaders headers = request.getHeaders();
            logger.info("Handshake headers: " + JsonUtils.Instance.toJson(headers));

            String sessionKey = AdminSession.getSessionKey(((ServletServerHttpRequest) request).getServletRequest());
            if (!StringUtils.isEmpty(sessionKey)) {
                logger.info("Handshake sessionKey: " + sessionKey);

                if (sessionKey != null && !sessionKey.isEmpty()) {
                    String cacheKey = className + "." + sessionKey;
                    T session = sessionManager.get(cacheKey);

                    if (session != null) {
                        attributes.put("sessionKey", sessionKey);
                        attributes.put("session", session);
                        return true;
                    }
                }
            }
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getBody().write("Unauthorized".getBytes());
        return false;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
    }


}
