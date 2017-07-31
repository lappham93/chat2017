package com.mit.message.services;

import com.google.common.collect.Lists;
import com.mit.http.exception.SequenceException;
import com.mit.message.bodies.DeleteUserBody;
import com.mit.message.bodies.MessageBody;
import com.mit.message.entities.LastMessage;
import com.mit.message.entities.Message;
import com.mit.message.enums.MessageStatus;
import com.mit.message.enums.UserStatus;
import com.mit.message.repositories.LastMessageRepo;
import com.mit.message.repositories.MessageRepo;
import com.mit.message.responses.LastMessageResponse;
import com.mit.message.responses.MessageResponse;
import com.mit.message.responses.MessageStatusResponse;
import com.mit.message.responses.UserStatusResponse;
import com.mit.session.services.OnlineUserService;
import com.mit.session.services.ProfileCacheService;
import com.mit.user.entities.Profile;
import com.mit.user.repositories.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hung on 6/23/2017.
 */

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private LastMessageRepo lastMessageRepo;
    @Autowired
    private ProfileRepo profileRepo;
    @Autowired
    private ProfileCacheService profileCacheService;
    @Autowired
    private OnlineUserService onlineUserService;

    public MessageResponse addMessage(MessageBody messageBody) throws SequenceException {
        long profileId = messageBody.getFromUserId();

        Message message = new Message();
        List<Long> refIds = Arrays.asList(profileId, messageBody.getToUserId());
        message.setRefIds(refIds);
        message.setContent(messageBody.getContent());
        message.setContentType(messageBody.getContentType());
        message.setStatus(MessageStatus.SENT.getValue());
        messageRepo.save(message);

        LastMessage lastMessage = lastMessageRepo.get(refIds);
        if (lastMessage == null) {
            lastMessage = new LastMessage();
            lastMessage.setRefIds(refIds);
        }

        lastMessage.setMessageId(message.getId());
        if (lastMessage.getRefId() == messageBody.getToUserId()) {
            lastMessage.setNewCount(lastMessage.getNewCount() + 1);
        } else {
            lastMessage.setNewCount(1);
            lastMessage.setRefId(messageBody.getToUserId());
        }
        lastMessage.setHideRefIds(Collections.emptyList());
        lastMessageRepo.save(lastMessage);

        MessageResponse messageResponse = new MessageResponse(message, profileCacheService.getProfile(refIds.get(0)), profileCacheService.getProfile(refIds.get(1)));

        return messageResponse;
    }

    public Map<String, Object> getMessage(long profileId, long messageId, int count, boolean newer) {
        Map<String, Object> rs = new HashMap<>();

        boolean hasMore = false;
        List<LastMessage> lastMessages;
        if (newer) {
            lastMessages = lastMessageRepo.getListFromMessageId(profileId, messageId, count + 1);
        } else {
            lastMessages = lastMessageRepo.getListToMessageId(profileId, messageId, count + 1);
        }

        if (lastMessages.size() > count) {
            hasMore = true;
            lastMessages = lastMessages.subList(0, count);
        }

        if (newer) {
            lastMessages = Lists.reverse(lastMessages);
        }

        List<Long> messageIds = new ArrayList<>(lastMessages.size());
        List<Long> userIds = new ArrayList<>(lastMessages.size());

        for (LastMessage lastMessage: lastMessages) {
            messageIds.add(lastMessage.getMessageId());

            long otherRefId = lastMessage.getRefIds().get(0).equals(profileId) ? lastMessage.getRefIds().get(1) : lastMessage.getRefIds().get(0);
            userIds.add(otherRefId);
        }

        Map<Long, Message> messages = messageRepo.getMapByIds(messageIds);
        Map<Long, Profile> profileMap = profileRepo.getMapByIds(userIds);

        List<LastMessageResponse> messageResps = new ArrayList<LastMessageResponse>(lastMessages.size());

        for (LastMessage lastMessage: lastMessages) {
            long otherRefId = lastMessage.getRefIds().get(0).equals(profileId) ? lastMessage.getRefIds().get(1) : lastMessage.getRefIds().get(0);
            if (messages.get(lastMessage.getMessageId()) != null && profileMap.get(otherRefId) != null) {
                int userStatus = UserStatus.OFFLINE.getValue();
                if (onlineUserService.isOnline(otherRefId + "")) {
                    userStatus = UserStatus.ONLINE.getValue();
                }
                messageResps.add(new LastMessageResponse(messages.get(lastMessage.getMessageId()), lastMessage, profileMap.get(otherRefId), userStatus));
            }
        }

        rs.put("messages", messageResps);
        rs.put("hasMore", hasMore);

        return rs;
    }

    public Map<String, Object> getUserMessage(long profileId, long otherId, long messageId, int count, boolean newer) throws SequenceException {
        Map<String, Object> rs = new HashMap<>();

        boolean hasMore = false;
        List<MessageResponse> messageResps;
        List<Message> messages;
        List<Long> profileIds;

        if (otherId > 0) {
            List<Long> refIds = Arrays.asList(profileId, otherId);
            if (newer) {
                messages = messageRepo.getListFromMessageId(refIds, profileId, messageId, count + 1);
            } else {
                messages = messageRepo.getListToMessageId(refIds, profileId, messageId, count + 1);
            }
            profileIds = refIds;
        } else {
            if (newer) {
                messages = messageRepo.getListFromMessageId(profileId, messageId, count + 1);
            } else {
                messages = messageRepo.getListToMessageId(profileId, messageId, count + 1);
            }

            Set<Long> profileIdSet = new HashSet<>();
            for (Message message: messages) {
                profileIdSet.addAll(message.getRefIds());
            }
            profileIds = new ArrayList<>(profileIdSet);
        }

        if (messages.size() > count) {
            hasMore = true;
            messages = messages.subList(0, count);
        }

        if (!newer) {
            messages = Lists.reverse(messages);
        }

        Map<Long, Profile> profileMap = profileRepo.getMapByIds(profileIds);
//        Profile profile = profileMap.get(profileId);
//        Calendar calendar = Calendar.getInstance();
//        if (!StringUtils.isEmpty(profile.getTimeZone())) {
//            calendar.setTimeZone(TimeZone.getTimeZone(profile.getTimeZone()));
//        }

//        Map<Long, List<Message>> messageGroup = new HashMap<>();
//        for (Message message: messages) {
//            calendar.setTime(message.getCreatedDate());
//            ZonedDateTimeUtils.clearTime(calendar);
//            List<Message> messageInGroup = messageGroup.get(calendar.getTimeInMillis());
//            if (messageInGroup == null) {
//                messageInGroup = new LinkedList<>();
//                messageGroup.put(calendar.getTimeInMillis(), messageInGroup);
//            }
//
//            messageInGroup.add(message);
//        }
//
//        messageResps = new ArrayList<>(messages.size() + messageGroup.size());
//
//        for (Map.Entry<Long, List<Message>> messageEntry: messageGroup.entrySet()) {
//            messageResps.add(new MessageResponse(MessageType.TIMELINE.getValue(), new Date(messageEntry.getKey())));
//
//            for (Message message: messageEntry.getValue()) {
//                messageResps.add(new MessageResponse(message, profileMap.get(message.getRefIds().get(0)), profileMap.get(message.getRefIds().get(1))));
//            }
//        }

        messageResps = new ArrayList<>(messages.size());

        for (Message message: messages) {
            messageResps.add(new MessageResponse(message, profileMap.get(message.getRefIds().get(0)), profileMap.get(message.getRefIds().get(1))));
        }

//        LastMessage lastMessage = lastMessageRepo.get(refIds);
//        if (lastMessage != null && profileId == lastMessage.getUnreadRefId()) {
//            lastMessage.setUnreadRefId(0);
//            lastMessageRepo.save(lastMessage);
//        }

        rs.put("messages", messageResps);
        rs.put("hasMore", hasMore);

        return rs;
    }

    public List<MessageStatusResponse> getMessageStatus(long profileId, long messageId, Date lastUpdate) {
        List<Message> messages = messageRepo.getLastUpdateStatus(profileId, messageId, lastUpdate, 1000);

        List<MessageStatusResponse> messageResps = new ArrayList<>(messages.size());

        for (Message message: messages) {
            messageResps.add(new MessageStatusResponse(message, profileId));
        }

        return messageResps;
    }

    public void deleteUser(DeleteUserBody body) {
        messageRepo.hideUsers(body.getFromUserId(), body.getToUserIds());
        lastMessageRepo.resetNewCount(body.getFromUserId(), body.getToUserIds());
        lastMessageRepo.hideUsers(body.getFromUserId(), body.getToUserIds());
    }

    public List<UserStatusResponse> getUserStatus(long curUserId, List<Long> userIds) {
        List<UserStatusResponse> responses = new ArrayList<>(userIds.size());
        List<Long> offlineUserIds = new ArrayList<>(userIds.size() + 1);
        for (long userId: userIds) {
            int status = UserStatus.OFFLINE.getValue();
            if (onlineUserService.isOnline(userId + "")) {
                status = UserStatus.ONLINE.getValue();
            } else {
                offlineUserIds.add(userId);
            }
            responses.add(new UserStatusResponse(userId, status));
        }

        Map<Long, Profile> profileMap = profileRepo.getMapLastOnlineByIds(offlineUserIds);
        Set<Long> banUserIds = null;
        Profile curProfile = profileRepo.getById(curUserId);
        if (curProfile != null && curProfile.getBanUserIds() != null) {
            banUserIds = new HashSet<>(curProfile.getBanUserIds());
        }
        for (UserStatusResponse response: responses) {
            if (response.getStatus() == UserStatus.OFFLINE.getValue()) {
                Profile profile = profileMap.get(response.getUserId());
                if (profile != null && profile.getLastOnline() != null) {
                    response.setLastOnline(profile.getLastOnline());
                }
            }
            if (banUserIds != null && banUserIds.contains(response.getUserId())) {
                response.setBan(true);
            }
        }

        return responses;
    }
}
