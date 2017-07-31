package com.mit.asset.services;

public class FeedPhotoClient extends PhotoClient {

	public static final String nameConfig = "bq_feedphoto";

	private static FeedPhotoClient _instance; 
	
	public static FeedPhotoClient getInstance() {
		if (_instance == null) {
			_instance = new FeedPhotoClient();
		}
		return _instance;
	}
	
	public FeedPhotoClient() {
		super(nameConfig);
	}
}
