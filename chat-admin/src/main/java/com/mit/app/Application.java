package com.mit.app;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mit.caches.Cache;
import com.mit.caches.RedisCache;
import com.mit.filters.AdminAuthenticationFilter;
import com.mit.http.context.ApplicationContextProvider;
import com.mit.http.filter.LoggingFilter;
import com.mit.http.session.SessionManager;
import com.mit.session.SessionManagerImpl;
import com.mit.session.entities.UserSession;
import com.mit.user.entities.AdminProfile;
import com.mit.user.entities.RegionManagerProfile;

/**
 * 
 * @author TinnyLe
 *
 */

@SpringBootApplication
@EnableMongoAuditing
@Lazy
@ComponentScan(basePackages = { "com.mit.app", "com.mit.controllers", "com.mit.user", "com.mit.address", "com.mit.seq",
		"com.mit.session", "com.mit.data", "com.mit.service", "com.mit.asset", "com.mit.utils",
		"com.mit.banner", "com.mit.conts",  "com.mit.react", "com.mit.social", "com.mit.facebook", "com.mit.stat",
		"com.mit.notification", "com.mit.event", "com.mit.navigation", "com.mit.google", "com.mit.admin.services", "com.mit.suggest",
		"com.mit.betaface", "com.mit.face", "com.mit.socket", "com.mit.message"})
public class Application {
	public static Environment EnvConfig;
	public static ApplicationContext AppCtx;
	@Value("${admin.prefix}")
	private String adminPrefix;
	@Value("${admin.resources.path}")
	private String resourcesPath;
	@Value("${admin.resources}")
	private String resources;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		EnvConfig = ctx.getEnvironment();
		AppCtx = ctx;
		ApplicationContextProvider appCtx = new ApplicationContextProvider();
        appCtx.setApplicationContext(ctx);
		System.out.println("Start server");
	}
	
	@Bean
	public MultipartResolver multiPartResolver() {
		return new CommonsMultipartResolver();
	}
	
	@Bean
	public SessionManager sessionGenerator() {
		SessionManager sessionGenerator = new SessionManagerImpl();
		return sessionGenerator;
	}
	
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
	public Cache cache(RedisTemplate<String, Object> redisTemplate) {
		Cache cache = new RedisCache(redisTemplate);
		return cache;
	}
	
	@Bean
	public FilterRegistrationBean registrationLogging() {
		Filter filter = new LoggingFilter();
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.addUrlPatterns("/*");
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		return registration;
	}

	@Bean
	public FilterRegistrationBean authenticationRequest(SessionManagerImpl sessionManager) {
		AdminAuthenticationFilter<UserSession> filter = new AdminAuthenticationFilter<>(sessionManager);
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.addUrlPatterns("/*");
		registration.addInitParameter("excludePatterns", StringUtils.join(new String[]{adminPrefix + "/login", adminPrefix + "/face", resourcesPath, "/favicon.ico"}, ","));
		registration.addInitParameter("profileClasses", StringUtils.join(new String[]{AdminProfile.class.getSimpleName(), RegionManagerProfile.class.getSimpleName()}, ","));
		registration.addInitParameter("redirectPath", adminPrefix + "/login");
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setOrder(1);
		return registration;
	}
	
	@Bean
	public FilterRegistrationBean swaggerFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean(new MyUrlRewriteFilter());
		registration.addUrlPatterns("/swagger/*");
		registration.addUrlPatterns(adminPrefix + "/*");
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setOrder(2);
		return registration;
	}
	
}
