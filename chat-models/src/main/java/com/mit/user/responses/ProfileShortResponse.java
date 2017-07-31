package com.mit.user.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mit.common.enums.ObjectType;
import com.mit.session.entities.ProfileCache;
import com.mit.user.entities.Profile;
import com.mit.utils.LinkBuilder;

/**
 * Created by Hung Le on 2/24/17.
 */
public class ProfileShortResponse {
    private long id;
    private String avatar;
    private String displayName;
    private boolean isFollowed;
	private String homeTown;
	private long avatarPhoto;

    public ProfileShortResponse(long id) {
        this.id = id;
    }

    public ProfileShortResponse(Profile profile) {
    	this.id = profile.getId();
		this.avatar = LinkBuilder.buildPhotoLink(profile.getAvatar(), ObjectType.USER.getLowerName());
		this.displayName = profile.getFirstName() + " " + profile.getLastName();
		this.homeTown = profile.getAddress() != null ? profile.getAddress().getCity() : "";
    }

    public ProfileShortResponse(long userId, ProfileCache profile) {
        this.id = userId;
        this.avatar = LinkBuilder.buildPhotoLink(profile.getAvatar(), ObjectType.USER.getLowerName());
        this.displayName = profile.getDisplayName();
        this.homeTown = "";
        this.avatarPhoto = profile.getAvatar();
    }
    
    public ProfileShortResponse(Profile profile, boolean isFollowed) {
    	this(profile);
    	this.isFollowed = isFollowed;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}

	public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

	public String getHomeTown() {
		return homeTown;
	}

	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}

	@JsonIgnore
    public long getAvatarPhoto() {
        return avatarPhoto;
    }

    public void setAvatarPhoto(long avatarPhoto) {
        this.avatarPhoto = avatarPhoto;
    }
}
