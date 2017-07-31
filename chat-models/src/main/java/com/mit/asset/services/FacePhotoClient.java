package com.mit.asset.services;

public class FacePhotoClient extends PhotoClient {

	public static final String nameConfig = "bq_facephoto";

	private static FacePhotoClient _instance; 
	
	public static FacePhotoClient getInstance() {
		if (_instance == null) {
			_instance = new FacePhotoClient();
		}
		return _instance;
	}
	
	public FacePhotoClient() {
		super(nameConfig);
	}
}
