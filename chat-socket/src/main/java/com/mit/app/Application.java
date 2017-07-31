package com.mit.app;

import com.mit.app.entities.AppKey;
import com.mit.app.http.RequestVerifyImpl;
import com.mit.app.repositories.AppKeyRepo;
import com.mit.caches.Cache;
import com.mit.caches.RedisCache;
import com.mit.http.context.ApplicationContextProvider;
import com.mit.http.filter.AuthenticationFilter;
import com.mit.http.filter.LoggingFilter;
import com.mit.session.SessionManagerImpl;
import com.mit.session.entities.UserSession;
import com.mit.session.services.OnlineUserService;
import com.mit.user.entities.Profile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hung Le on 2/28/17.
 */
@SpringBootApplication
@EnableMongoAuditing
@Lazy
@ComponentScan(basePackages={"com.mit.app", "com.mit.app", "com.mit.user", "com.mit.address", "com.mit.seq",
        "com.mit.session", "com.mit.biz", "com.mit.service", "com.mit.socket", "com.mit.conts",
        "com.mit.react", "com.mit.asset", "com.mit.social", "com.mit.facebook", "com.mit.stat",
        "com.mit.notification", "com.mit.upload", "com.mit.session", "com.mit.message", "com.mit.navigation",
        "com.mit.event", "com.mit.banner"})
public class Application {
    public static Environment EnvConfig;
    public static ApplicationContext AppCtx;


//    @Bean
//    public SessionManager sessionGenerator() {
//        SessionManager sessionGenerator = new SessionManagerImpl();
//        return sessionGenerator;
//    }

//    @Bean
//    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        return stringRedisSerializer;
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisJsonSerializer() {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisJsonSerializer =
                new GenericJackson2JsonRedisSerializer();
        return genericJackson2JsonRedisJsonSerializer;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
                                                       StringRedisSerializer stringRedisSerializer,
                                                       GenericJackson2JsonRedisSerializer genericJackson2JsonRedisJsonSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setExposeConnection(true);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisJsonSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public Cache redisCache(RedisTemplate<String, Object> redisTemplate) {
        Cache cache = new RedisCache(redisTemplate);
        return cache;
    }

    @Bean
    public FilterRegistrationBean swaggerFilter() throws Exception {
        FilterRegistrationBean registration = new FilterRegistrationBean(new MyUrlRewriteFilter());
        registration.addUrlPatterns("/swagger/*");
        registration.addUrlPatterns("/chat/*");
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }

    @Bean
    public FilterRegistrationBean registrationLogging() {
        Filter filter = new LoggingFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.addUrlPatterns("/*");
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }

//    @Bean
//    public FilterRegistrationBean verifyRequest(RequestVerifyImpl requestVerify) {
//        Filter filter = new VerificationFilter(requestVerify);
//        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
//        registration.addUrlPatterns("/*");
//        registration.setDispatcherTypes(DispatcherType.REQUEST);
//        return registration;
//    }

//    @Bean
//    public FilterRegistrationBean authenticationRequest(SessionManagerImpl sessionManager) {
//        AuthenticationFilter<UserSession> filter = new AuthenticationFilter<>(sessionManager);
//        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
//        registration.addUrlPatterns("/*");
//        registration.addInitParameter("excludePatterns", "/swagger");
//        registration.addInitParameter("profileClass", Profile.class.getSimpleName());
//        registration.setDispatcherTypes(DispatcherType.REQUEST);
//        return registration;
//    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        ApplicationContextProvider appCtx = new ApplicationContextProvider();
        appCtx.setApplicationContext(ctx);
        AppCtx = ctx;
        EnvConfig = ctx.getEnvironment();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            AppKeyRepo appKeyRepo = ctx.getBean(AppKeyRepo.class);
            RequestVerifyImpl requestVerify = ctx.getBean(RequestVerifyImpl.class);
            List<AppKey> appKeys = appKeyRepo.getAllKey();
            for (AppKey appKey : appKeys) {
                requestVerify.addApiKeyToCache(appKey);
            }
            }
        }, 0, TimeUnit.HOURS.toMillis(1));

        OnlineUserService onlineUserService = ctx.getBean(OnlineUserService.class);
        onlineUserService.clearSession();
    }
}
