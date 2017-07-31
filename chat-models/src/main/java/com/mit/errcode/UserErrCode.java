package com.mit.errcode;

public class UserErrCode {
	public static final int SUCCESS = 0;
	public static final int SERVER_ERR = -1;
	
	public static final int APPLICATION_PENDING = -101;
	public static final int BANK_ACCOUNT_INVALID = -102;
	public static final int ZIP_CODE_INVALID = -103;
	public static final int ADDRESS_INVALID = -104;
	public static final int PERSONAL_INFO_INVALID = -105;
	public static final int APPLICATION_STATUS_INVALID = -106;
	public static final int PAYMENT_METHOD_NOT_AVAILABLE = -107;
	public static final int PAYMENT_ERROR = -108;
}
