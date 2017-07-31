package com.mit.asset.services;

public class ProductPhotoClient extends PhotoClient {

	public static final String nameConfig = "bq_productphoto";

	private static ProductPhotoClient _instance; 
	
	public static ProductPhotoClient getInstance() {
		if (_instance == null) {
			_instance = new ProductPhotoClient();
		}
		return _instance;
	}
	
	private ProductPhotoClient() {
		super(nameConfig);
	}
}
