package com.mit.asset.services;

public class ChatPhotoClient extends PhotoClient {

	public static final String nameConfig = "bq_chatphoto";

	private static ChatPhotoClient _instance;

	public static ChatPhotoClient getInstance() {
		if (_instance == null) {
			_instance = new ChatPhotoClient();
		}
		return _instance;
	}

	public ChatPhotoClient() {
		super(nameConfig);
	}
}
