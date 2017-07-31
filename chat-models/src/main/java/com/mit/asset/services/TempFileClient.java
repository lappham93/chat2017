package com.mit.asset.services;

public class TempFileClient extends PhotoClient {

	public static final String nameConfig = "saocoo_tempfile";

	private static TempFileClient _instance;

	public static TempFileClient getInstance() {
		if (_instance == null) {
			_instance = new TempFileClient();
		}
		return _instance;
	}

	public TempFileClient() {
		super(nameConfig);
	}
}
