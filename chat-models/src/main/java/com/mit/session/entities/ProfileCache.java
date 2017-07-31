package com.mit.session.entities;

import com.mit.user.entities.Profile;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hung on 6/25/2017.
 */
public class ProfileCache {
    private long avatar;
    private String displayName;
    private Set<Long> banUserIds;
    private boolean disableNotifyChat;

    public ProfileCache() {
    }

    public ProfileCache(Profile profile) {
        this.avatar = profile.getAvatar();
        this.displayName = profile.getFirstName() + " " + profile.getLastName();
        if (profile.getBanUserIds() != null) {
            this.banUserIds = new HashSet<>(profile.getBanUserIds());
        }
        this.disableNotifyChat = profile.isDisableNotifyChat();
    }

    public long getAvatar() {
        return avatar;
    }

    public void setAvatar(long avatar) {
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Set<Long> getBanUserIds() {
        return banUserIds;
    }

    public void setBanUserIds(Set<Long> banUserIds) {
        this.banUserIds = banUserIds;
    }

    public boolean isDisableNotifyChat() {
        return disableNotifyChat;
    }

    public void setDisableNotifyChat(boolean disableNotifyChat) {
        this.disableNotifyChat = disableNotifyChat;
    }
}
