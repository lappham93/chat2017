package com.mit.app;

import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.messaging.simp.stomp.DefaultStompSession;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.StompSubProtocolHandler;
import org.springframework.web.socket.messaging.SubProtocolHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.mit.http.session.SessionManager;
import com.mit.session.SessionManagerImpl;
import com.mit.session.entities.UserSession;
import com.mit.socket.handlers.WebSocketConnectHandler;
import com.mit.socket.handlers.WebSocketDisconnectHandler;
import com.mit.socket.interceptors.HttpHandshakeInterceptor;
import com.mit.user.entities.Profile;

/**
 * Created by Hung Le on 2/28/17.
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    Logger logger = LoggerFactory.getLogger("requestLogging");

    @Autowired
    Environment env;

    @Autowired
    private MessageChannel clientOutboundChannel;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");

        StompBrokerRelayRegistration stompBrokerRelayRegistration = config.enableStompBrokerRelay("/topic", "/queue");
        stompBrokerRelayRegistration.setRelayHost(env.getProperty("rabbitmq.host"));
        stompBrokerRelayRegistration.setRelayPort(env.getProperty("rabbitmq.stompport", Integer.class));
        stompBrokerRelayRegistration.setSystemLogin(env.getProperty("rabbitmq.user"));
        stompBrokerRelayRegistration.setSystemPasscode(env.getProperty("rabbitmq.password"));
        stompBrokerRelayRegistration.setClientLogin(env.getProperty("rabbitmq.user"));
        stompBrokerRelayRegistration.setClientPasscode(env.getProperty("rabbitmq.password"));
        stompBrokerRelayRegistration.setVirtualHost(env.getProperty("rabbitmq.vh"));
//        config.setPathMatcher(new AntPathMatcher("."));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api")
                .setHandshakeHandler(new SecureHandshakeHandler(sessionGenerator(), Profile.class.getSimpleName()))
//                .addInterceptors(new HttpHandshakeInterceptor<>(sessionGenerator(), Profile.class.getSimpleName()));
                .withSockJS()
                .setWebSocketEnabled(true)
                .setStreamBytesLimit(512 * 1024)
                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(30 * 1000)
                .setInterceptors(new HttpHandshakeInterceptor<>(sessionGenerator(), Profile.class.getSimpleName()));
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 * 1024);
        registration.setMessageSizeLimit(128 * 1024);
//        registration.setDecoratorFactories(testWebSocketHandlerDecoratorFactory());
    }

    @Bean
    public WebSocketConnectHandler webSocketConnectHandler() {
        return new WebSocketConnectHandler();
    }

    @Bean
    public WebSocketDisconnectHandler webSocketDisconnectHandler() {
        return new WebSocketDisconnectHandler();
    }

    @Bean
    public SessionManager sessionGenerator() {
        SessionManager sessionGenerator = new SessionManagerImpl();
        return sessionGenerator;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
        channelRegistration.taskExecutor().corePoolSize(50);
        channelRegistration.setInterceptors(new ChannelInterceptorAdapter() {

//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//                if ("/queue/demand/tracking".equals(accessor.getDestination())) {
//                    accessor.setDestination("/queue/demand-tracking");
//                }
//                return message;
//            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

//                if (accessor.getCommand() != null) {
                    logger.info("IN " + accessor.getDetailedLogMessage(message.getPayload()));
//                }

                if (accessor.getCommand() == StompCommand.SEND) {
                    StompHeaderAccessor ackHeader = StompHeaderAccessor.create(StompCommand.RECEIPT);
                    ackHeader.setDestination(accessor.getDestination());
                    ackHeader.setSessionId(accessor.getSessionId());
                    ackHeader.setUser(accessor.getUser());
                    ackHeader.setReceiptId(accessor.getReceipt());

                    Message<byte[]> ackMessage = MessageBuilder.withPayload(DefaultStompSession.EMPTY_PAYLOAD).setHeaders(ackHeader).build();
                    clientOutboundChannel.send(ackMessage);
                }
            }
        });
    }

    @Bean
    public SubProtocolHandler stompSubProtocolHandler() {
        return new StompSubProtocolHandler();
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {
        channelRegistration.taskExecutor().corePoolSize(50);
        channelRegistration.setInterceptors(new ChannelInterceptorAdapter() {

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (accessor.getCommand() != null) {
                    logger.info("OUT " + accessor.getDetailedLogMessage(message.getPayload()));
                }
            }
        });
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
