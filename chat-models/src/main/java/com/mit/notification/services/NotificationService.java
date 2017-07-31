package com.mit.notification.services;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.mit.app.enums.AppType;
import com.mit.fcm.sender.FCMSender;
import com.mit.notification.bodies.DeviceInfoBody;
import com.mit.notification.bodies.MultiTargetNotification;
import com.mit.notification.bodies.Notification;
import com.mit.notification.bodies.SingleTargetNotification;
import com.mit.notification.entities.DeviceInfo;
import com.mit.notification.repositories.DeviceInfoRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
    private DeviceInfoRepo deviceInfoRepo;
	@Value("${firebase.client.key:''}")
	private String clientFirebaseKey;
    @Value("${firebase.provider.key:''}")
    private String providerFirebaseKey;

    public void addDeviceInfo(long userId, int appType, DeviceInfoBody deviceInfoBody) throws Exception {
    	deviceInfoBody.setId(DeviceInfo.buildId(deviceInfoBody.getId(), appType));
        DeviceInfo deviceInfo = deviceInfoBody.toDeviceInfo();
        deviceInfo.setUserId(userId);
        deviceInfo.setAppType(appType);
        deviceInfoRepo.save(deviceInfo);
    }

	public void pushNotification(SingleTargetNotification notification) {
        List<DeviceInfo> devices = deviceInfoRepo.getByUserId(notification.getUserId(), notification.getAppType());
        pushNotification(devices, notification);
	}

    public void pushNotification(MultiTargetNotification notification) {
        List<DeviceInfo> devices = deviceInfoRepo.getByUserIds(notification.getUserIds(), notification.getAppType());
        pushNotification(devices, notification);
    }

    public void pushNotification(List<DeviceInfo> devices, Notification notification) {
        try {
            if (CollectionUtils.isEmpty(devices)) {
            	logger.debug("device empty");
                return;
            }

            List<String> regTokens = new ArrayList<String>(devices.size());
            for (DeviceInfo device: devices) {
                if (!StringUtils.isEmpty(device.getToken())) {
                    regTokens.add(device.getToken());
                }
            }

            Sender sender;
            if (notification.getAppType() == AppType.PROVIDER.getValue()) {
                sender = new FCMSender(providerFirebaseKey);
            } else {
                sender = new FCMSender(clientFirebaseKey);
            }
            Message.Builder messageBuilder = new Message.Builder()
                    .collapseKey(notification.getId() + "")
                    .timeToLive(3)
                    .delayWhileIdle(true)
                    .addData("type", notification.getType() + "")
                    .addData("id", notification.getId() + "")
                    .priority(Message.Priority.HIGH)
                    .notification(new com.google.android.gcm.server.Notification.Builder("").body(notification.getMessage()).title("Saocoo").badge(1).build());

            for (String key: notification.getData().keySet()) {
                messageBuilder.addData(key, notification.getData().get(key));
            }

            Message message = messageBuilder.build();

            MulticastResult results = sender.send(message, regTokens, 1);
            logger.info("Result: " + results.toString());
        } catch (Exception e) {
            logger.info("Error push notification: ", e);
        }
    }
}
