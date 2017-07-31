package com.mit.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.mit.caches.Cache;
import com.mit.caches.RedisCache;
import com.mit.http.context.ApplicationContextProvider;

/**
 * Created by Hung Le on 2/13/17.
 */

@SpringBootApplication
@EnableMongoAuditing
@Lazy
@ComponentScan(basePackages={"com.mit.app", "com.mit.conts", "com.mit.user", "com.mit.address", "com.mit.seq",
        "com.mit.session", "com.mit.service", "com.mit.feed", "com.mit.social", "com.mit.asset", "com.mit.react",
        "com.mit.upload", "com.mit.facebook", "com.mit.stat", "com.mit.notification", "com.mit.email",
        "com.mit.navigation", "com.mit.event", "com.mit.banner"})
public class Application {
    public static Environment EnvConfig;
    public static ApplicationContext AppCtx;
    @Value("${firebase.client.key}")
	private String clientFirebaseKey;
    @Value("${firebase.provider.key}")
    private String providerFirebaseKey;

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public Cache redisCache(RedisTemplate<String, Object> redisTemplate) {
        Cache cache = new RedisCache(redisTemplate);
        return cache;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        ApplicationContextProvider appCtx = new ApplicationContextProvider();
        appCtx.setApplicationContext(ctx);
        AppCtx = ctx;
        EnvConfig = ctx.getEnvironment();
        test();
    }
    
    public static void test() { 
//    	NewsService newsService = AppCtx.getBean(NewsService.class);
//    	FloorRepo floorRepo = AppCtx.getBean(FloorRepo.class);
////    	newsService.notifyNearestBooths(23, new Point(0.9, 0.8), floorRepo.getById(1l));
//    	newsService.notifyAdvertisement(1L, "", 0L, Arrays.asList(1L, 23L));
//    	System.out.println("abc");
////    	newsService.pushNotification(notif);
    }
}
