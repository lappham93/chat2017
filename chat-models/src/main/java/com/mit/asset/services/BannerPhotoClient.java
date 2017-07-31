package com.mit.asset.services;

public class BannerPhotoClient extends PhotoClient {

	public static final String nameConfig = "bq_bannerphoto";

	private static BannerPhotoClient _instance; 
	
	public static BannerPhotoClient getInstance() {
		if (_instance == null) {
			_instance = new BannerPhotoClient();
		}
		return _instance;
	}
	
	public BannerPhotoClient() {
		super(nameConfig);
	}
}
