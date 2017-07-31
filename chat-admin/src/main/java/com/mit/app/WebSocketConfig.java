package com.mit.app;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.SimpleBrokerRegistration;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.mit.http.session.SessionManager;
import com.mit.session.SessionManagerImpl;
import com.mit.session.entities.UserSession;
import com.mit.socket.interceptors.HttpHandshakeInterceptor;
import com.mit.user.entities.AdminProfile;
import com.mit.user.entities.Profile;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Autowired
	private Environment env;
	
	@Bean
    public SessionManager sessionGenerator() {
        SessionManager sessionGenerator = new SessionManagerImpl();
        return sessionGenerator;
    }
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		StompBrokerRelayRegistration stompBrokerRelayRegistration = registry.enableStompBrokerRelay("/topic");
		stompBrokerRelayRegistration.setRelayHost(env.getProperty("rabbitmq.host"));
        stompBrokerRelayRegistration.setRelayPort(env.getProperty("rabbitmq.stompport", Integer.class));
        stompBrokerRelayRegistration.setSystemLogin(env.getProperty("rabbitmq.user"));
        stompBrokerRelayRegistration.setSystemPasscode(env.getProperty("rabbitmq.password"));
        stompBrokerRelayRegistration.setClientLogin(env.getProperty("rabbitmq.user"));
        stompBrokerRelayRegistration.setClientPasscode(env.getProperty("rabbitmq.password"));
        stompBrokerRelayRegistration.setVirtualHost(env.getProperty("rabbitmq.vh"));
	};
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/api")
			.setHandshakeHandler(new SecureHandshakeHandler(sessionGenerator(), AdminProfile.class.getSimpleName()))
			.withSockJS()
				.setWebSocketEnabled(true)
	            .setStreamBytesLimit(512 * 1024)
	            .setHttpMessageCacheSize(1000)
	            .setDisconnectDelay(30 * 1000)
	            .setInterceptors(new HttpHandshakeInterceptor<>(sessionGenerator(), AdminProfile.class.getSimpleName()));
	}
	
	

	
	static class SecureHandshakeHandler<T extends UserSession> extends DefaultHandshakeHandler {
        private final SessionManager<T> sessionManager;
        private final String className;

        public SecureHandshakeHandler(SessionManager<T> sessionManager, String className) {
            this.sessionManager = sessionManager;
            this.className = className;
        }

        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            Principal result = null;
            HttpHeaders headers = request.getHeaders();

            logger.info("Determining user...");

            String sessionKey = headers.getFirst("Session");

            if (sessionKey != null && !sessionKey.isEmpty()) {
                String cacheKey = className + "." + sessionKey;
                final T session = sessionManager.get(cacheKey);

                if (session != null) {
                    attributes.put("sessionKey", sessionKey);
                    attributes.put("session", session);
                    result = new Principal() {
                        @Override
                        public String getName() {
                            return session.getUserId() + "";
                        }
                    };
                }
            }

            return result;
        }
    }
}
