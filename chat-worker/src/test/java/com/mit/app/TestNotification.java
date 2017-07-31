package com.mit.app;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.mit.app.enums.AppType;
import com.mit.fcm.sender.FCMSender;
import com.mit.notification.bodies.Notification;
import com.mit.notification.entities.DeviceInfo;
import com.mit.notification.entities.WebNews;
import com.mit.notification.repositories.DeviceInfoRepo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Le on 5/8/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestNotification {
    @Value("${firebase.client.key}")
    private String clientFirebaseKey;
    @Autowired
    private DeviceInfoRepo deviceInfoRepo;

    @Test
    public void pushNotification() throws Exception {
        WebNews webNews = new WebNews();
        webNews.setId(1L);
        webNews.setMsg("Google");
        webNews.setUrl("https://www.google.com/");

        Notification notification = webNews.buildSingleTargetNotification(0, AppType.CLIENT.getValue());

        List<DeviceInfo> devices = deviceInfoRepo.getAll();
         List<String> regTokens = new ArrayList<String>(devices.size());
        for (DeviceInfo device: devices) {
            if (!StringUtils.isEmpty(device.getToken())) {
                regTokens.add(device.getToken());
            }
        }

        Sender sender = new FCMSender(clientFirebaseKey);
        Message.Builder messageBuilder = new Message.Builder()
                .collapseKey(notification.getId() + "")
                .timeToLive(3)
                .delayWhileIdle(true)
                .addData("type", notification.getType() + "")
                .addData("id", notification.getId() + "")
                .priority(Message.Priority.HIGH)
                .notification(new com.google.android.gcm.server.Notification.Builder("").body(notification.getMessage()).title("Homber").badge(1).build());

        for (String key: notification.getData().keySet()) {
            messageBuilder.addData(key, notification.getData().get(key));
        }

        Message message = messageBuilder.build();

        MulticastResult results = sender.send(message, regTokens, 1);
        System.out.println("Result: " + results.toString());
    }
}
