package com.mit.notification.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mit.app.enums.AppType;
import com.mit.message.responses.MessageResponse;
import com.mit.notification.bodies.MultiTargetNotification;
import com.mit.notification.bodies.SingleTargetNotification;
import com.mit.notification.bodies.ViewNewsBody;
import com.mit.notification.entities.ChatNews;
import com.mit.notification.entities.MessageNews;
import com.mit.notification.entities.News;
import com.mit.notification.entities.ProductNews;
import com.mit.notification.entities.UserNews;
import com.mit.notification.entities.UserNewsItem;
import com.mit.notification.entities.WebNews;
import com.mit.notification.repositories.NewsRepo;
import com.mit.notification.repositories.UserNewsRepo;
import com.mit.notification.responses.MessageNewsResponse;
import com.mit.notification.responses.NewsResponse;
import com.mit.notification.responses.ProductNewsResponse;
import com.mit.notification.responses.UserNewsItemResponse;
import com.mit.notification.responses.UserNewsResponse;
import com.mit.notification.responses.WebNewsResponse;
import com.mit.rabbitmq.RabbitRoutingKey;
import com.mit.user.entities.Profile;
import com.mit.user.repositories.ProfileRepo;

/**
 * Created by Hung Le on 4/5/17.
 */

@Service
public class NewsService {

    private final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private UserNewsRepo userNewsRepo;
    @Autowired
    private NewsRepo newsRepo;
    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    @Autowired
    private ProfileRepo profileRepo;

    public Map<String, Object> getListNews(long userId, int from, int count) {
        Map<String, Object> rs = new HashMap<>();

        UserNews userNews = userNewsRepo.getById(userId, from, count + 1);

        boolean hasMore = false;

        List<UserNewsItemResponse> newsResponses;
        if (userNews != null && !CollectionUtils.isEmpty(userNews.getItems())) {
            if (userNews.getItems().size() > count) {
                userNews.setItems(userNews.getItems().subList(0, count));
                hasMore = true;
            }

            List<Long> newsIds = new ArrayList<>(userNews.getItems().size());
            for (UserNewsItem item: userNews.getItems()) {
                newsIds.add(item.getId());
            }
            Map<Long, News> newsMap = newsRepo.getMapByIds(newsIds);

            List<Long> userIds = new ArrayList<>();
            Map<Long, Profile> profileMap = profileRepo.getMapByIds(userIds);

            newsResponses = new ArrayList<>(userNews.getItems().size());
            for (UserNewsItem userNewsItem: userNews.getItems()) {
                News news = newsMap.get(userNewsItem.getId());

                if (news != null) {
                    newsResponses.add(new UserNewsItemResponse(userNewsItem, buildResponse(news, profileMap)));
                }
            }

            userNewsRepo.resetNewCount(userId);
        } else {
            newsResponses = Collections.emptyList();
        }

        UserNewsResponse userNewsResponse = new UserNewsResponse(newsResponses, userNewsRepo.getNewCount(userId));
        rs.put("news", userNewsResponse);
        rs.put("hasMore", hasMore);

        return rs;
    }

    public UserNewsItemResponse getNews(long userId, long id) {
        UserNews userNews = userNewsRepo.getByNewsId(userId, id);

        if (userNews != null && !CollectionUtils.isEmpty(userNews.getItems())) {
            News news = newsRepo.getById(id);
            Map<Long, Profile> profileMap = Collections.emptyMap();
            return new UserNewsItemResponse(userNews.getItems().get(0), buildResponse(news, profileMap));
        }

        return null;
    }

    public UserNewsItemResponse getProviderNews(long userId, long id) {
        UserNews userNews = userNewsRepo.getProviderByNewsId(userId, id);

        if (userNews != null && !CollectionUtils.isEmpty(userNews.getProviderItems())) {
            News news = newsRepo.getById(id);
            Map<Long, Profile> profileMap = Collections.emptyMap();
            return new UserNewsItemResponse(userNews.getProviderItems().get(0), buildResponse(news, profileMap));
        }

        return null;
    }

    public void viewNews(ViewNewsBody body) {
        if (!CollectionUtils.isEmpty(body.getIds())) {
            rabbitMessagingTemplate.convertAndSend(RabbitRoutingKey.NEWS_VIEW, body);
        }
    }

    public void updateView(ViewNewsBody body) {
        if (!CollectionUtils.isEmpty(body.getIds())) {
            userNewsRepo.updateView(body.getUserId(), body.getIds());
        }
    }

    public void viewProviderNews(ViewNewsBody body) {
        if (!CollectionUtils.isEmpty(body.getIds())) {
            rabbitMessagingTemplate.convertAndSend(RabbitRoutingKey.NEWS_PROVIDER_VIEW, body);
        }
    }

    public void updateProviderView(ViewNewsBody body) {
        if (!CollectionUtils.isEmpty(body.getIds())) {
            userNewsRepo.updateProviderView(body.getUserId(), body.getIds());
        }
    }

    public int getNewCount(long userId) {
        return userNewsRepo.getNewCount(userId);
    }

    public int getProviderNewCount(long userId) {
        return userNewsRepo.getProviderNewCount(userId);
    }

    public NewsResponse buildResponse(News news, Map<Long, Profile> profileMap) {
        if (news instanceof MessageNews) {
            return new MessageNewsResponse((MessageNews)news);
        } else if (news instanceof WebNews) {
            return new WebNewsResponse((WebNews)news);
        } else if (news instanceof ProductNews) {
        	return new ProductNewsResponse((ProductNews) news);
        }

        return new NewsResponse(news);
    }
    
    public void notifyWeb(String url, String msg, long thumb, List<Long> userIds, int ...appTypes) {
    	WebNews news = new WebNews();
        news.setUrl(url);
        news.setMsg(msg);
        news.setThumb(thumb);
        news.setActive(true);
        try {
            newsRepo.save(news);
            UserNewsItem userNews = new UserNewsItem();
            userNews.setId(news.getId());
            userNews.setCreatedDate(news.getCreatedDate());
            for (int appType: appTypes) {
            	if (appType == AppType.CLIENT.getValue()) {
            		userNewsRepo.addItemToUserList(userIds, userNews);
            	} else if (appType == AppType.PROVIDER.getValue()) {
            		userNewsRepo.addProviderItemToUserList(userIds, userNews);
            	}
            	MultiTargetNotification notification = news.buildMultiDestNotification(userIds, appType);
            	rabbitMessagingTemplate.convertAndSend(RabbitRoutingKey.NOTIFY_MULTI, notification);
            }
        } catch (Exception ex) {
            logger.info("error notify web", ex);
        }
    }
    
    public void notifyChatMessage(MessageResponse message) {
        ChatNews news = new ChatNews();
        news.setMsg("You have a new message from " + message.getFromUser().getDisplayName());
        news.setThumb(message.getFromUser().getAvatarPhoto());
        news.setFromUserId(message.getFromUser().getId());
        news.setActive(true);
        try {
            SingleTargetNotification notification = news.buildSingleTargetNotification(message.getToUser().getId(), AppType.CLIENT.getValue());
            rabbitMessagingTemplate.convertAndSend(RabbitRoutingKey.NOTIFY_SINGLE, notification);
        } catch (Exception ex) {
            logger.info("error notify chat message", ex);
        }
    }
    
    public void notifyProduct(long productId, String msg, long thumb, List<Long> userIds) {
    	ProductNews news = new ProductNews();
        news.setProductId(productId);
        news.setMsg(msg);
        news.setThumb(thumb);
        news.setActive(true);
        try {
            newsRepo.save(news);
            UserNewsItem userNews = new UserNewsItem();
            userNews.setId(news.getId());
            userNews.setCreatedDate(news.getCreatedDate());
    		userNewsRepo.addItemToUserList(userIds, userNews);
        	MultiTargetNotification notification = news.buildMultiDestNotification(userIds, AppType.CLIENT.getValue());
        	rabbitMessagingTemplate.convertAndSend(RabbitRoutingKey.NOTIFY_MULTI, notification);
        	logger.info("notify product: sent message");
        } catch (Exception ex) {
            logger.info("error notify product", ex);
        }
    }
    
}
