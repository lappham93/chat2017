package com.mit.errcode;

public class PaymentErrCode {
	public static final int SUCCESS = 0;
	public static final int SERVER_ERR = -1;
	
	public static final int BILLING_ADDRESS_NOT_EXIST = -301;
	public static final int INVALID_CARD_NUMBER = -302;
	public static final int INVALID_CARD_CODE = -303;
	public static final int INVALID_EXPIRATION_DATE = -304;
	public static final int INVALID_PROMOTION_CODE = -305;
	public static final int PAYMENT_DECLINED = -306;
	public static final int PAYMENT_ERROR = -307;
	
	public static final int PAYMENT_UNAVAILABLE = -308;
	public static final int BRAINTREE_ERROR = -309;
	public static final int CARD_EXIST = -310;
	public static final int PERMISSION_LIMIT = -311;
	public static final int PAYMENT_METHOD_NOT_AVAILABLE = -312;
	public static final int NO_PAYMENT_METHOD = -313;
	public static final int NONCE_INVALID = -314;
	public static final int PAYMENT_AMOUNT_ERROR = -315;
	public static final int TRANSACTION_CANNOT_SETTLED = -316;
	public static final int TRANSACTION_CANNOT_VOIDED = -317;
	public static final int TRANSACTION_CANNOT_REFUNDED = -318;
	public static final int PROVIDER_PAYMENT_ERROR = -319;
	public static final int TRANSACTION_CANNOT_RELEASED = -320;
	public static final int LAST_HISTORY_IN_PROCESS = -321;
	public static final int ITEM_NOT_FOUND = -322;
	public static final int INVALID_DATE = -323;

	public static final int PROMOTION_ONLY_NEW_USER = -330;
	public static final int PROMOTION_USED_OTHER_INVITE_CODE = -331;
	public static final int PROMOTION_USE_SELF_INVITE_CODE = -332;
	public static final int PROMOTION_EXCEED_LIMIT = -333;
	public static final int PROMOTION_INVALID_TIME = -334;
	public static final int PROMOTION_EXIST = -335;
	public static final int PROMOTION_INVALID_REGION = -336;
	public static final int PROMOTION_NOT_START = -337;
	
}
