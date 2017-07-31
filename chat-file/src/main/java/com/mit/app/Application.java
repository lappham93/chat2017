package com.mit.app;

import com.mit.caches.Cache;
import com.mit.caches.RedisCache;
import com.mit.http.context.ApplicationContextProvider;
import com.mit.http.session.SessionManager;
import com.mit.session.SessionManagerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.DispatcherType;

/**
 * Created by Hung Le on 2/13/17.
 */

@SpringBootApplication
@Lazy
@ComponentScan(basePackages={"com.mit.app", "com.mit.conts", "com.mit.seq", "com.mit.file", "com.mit.asset",
        "com.mit.user", "com.mit.address", "com.mit.service", "com.mit.stat", "com.mit.react", "com.mit.facebook",
		"com.mit.notification", "com.mit.social", "com.mit.session", "com.mit.navigation", "com.mit.event",
		"com.mit.banner"})
public class Application {
    public static Environment EnvConfig;
    public static ApplicationContext AppCtx;

    @Bean
    public SessionManager sessionGenerator() {
        SessionManager sessionGenerator = new SessionManagerImpl();
        return sessionGenerator;
    }

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

    @Bean
    public FilterRegistrationBean swaggerFilter() throws Exception {
        FilterRegistrationBean registration = new FilterRegistrationBean(new MyUrlRewriteFilter());
        registration.addUrlPatterns("/swagger/*");
        registration.addUrlPatterns("/bq/*");
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }

//    @Bean
//    public FilterRegistrationBean registrationLogging() {
//        Filter filter = new LoggingFilter();
//        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
//        registration.addUrlPatterns("/*");
//        registration.setDispatcherTypes(DispatcherType.REQUEST);
//        return registration;
//    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        ApplicationContextProvider appCtx = new ApplicationContextProvider();
        appCtx.setApplicationContext(ctx);
        AppCtx = ctx;
        EnvConfig = ctx.getEnvironment();
    }
}
