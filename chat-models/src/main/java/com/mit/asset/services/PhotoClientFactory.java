package com.mit.asset.services;

import com.mit.common.enums.ObjectType;

public class PhotoClientFactory {

	public static PhotoClient getPhotoClient(ObjectType objectType) {
		if (objectType == ObjectType.PRODUCT) {
			return ProductPhotoClient.getInstance();
		}
		if (objectType == ObjectType.FEED || objectType == ObjectType.SUBFEED || objectType == ObjectType.COMMENT) {
			return FeedPhotoClient.getInstance();
		}
		if (objectType == ObjectType.USER) {
			return UserPhotoClient.getInstance();
		}
		if (objectType == ObjectType.BANNER) {
			return BannerPhotoClient.getInstance();
		}
		if (objectType == ObjectType.TEMP_FILE) {
			return TempFileClient.getInstance();
		}
		if (objectType == ObjectType.CHAT) {
			return ChatPhotoClient.getInstance();
		}
		if (objectType == ObjectType.FACE) {
			return FacePhotoClient.getInstance();
		}

		return null;
	}

}
