package com.mit.asset.services;

public class UserPhotoClient extends PhotoClient {

	public static final String nameConfig = "bq_userphoto";

	private static UserPhotoClient _instance; 
	
	public static UserPhotoClient getInstance() {
		if (_instance == null) {
			_instance = new UserPhotoClient();
		}
		return _instance;
	}
	
	private UserPhotoClient() {
		super(nameConfig);
	}
}
