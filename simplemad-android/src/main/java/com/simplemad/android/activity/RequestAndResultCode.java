package com.simplemad.android.activity;

public final class RequestAndResultCode {

	public static final class RequestCode {
		
		public static final int REQUEST_ADVERTISEMENT_PLAY = 1;
		
		public static final int REQUEST_REMOTE_ADVERTISEMENT_LIST_FOR_SERVER_NOTIFICATION = 2;
		
		public static final int REQUEST_USER_STATISTICS_MODIFY = 3;
		
		public static final int REQUEST_USER_EXCHANGE_MONEY = 4;
		
		public static final int REQUEST_USER_PROTOCOL = 5;
		
		public static final int REQUEST_SHARE = 10;
		
	}
	
	public static final class ResultCode {
		
		public static final int RESULT_ADVERTISEMENT_PLAY_OK = 1;
		
		public static final int RESULT_ADVERTISEMENT_PLAY_CANCEL = 2;
		
		public static int RESULT_REMOTE_ADVERTISEMENT_LIST_FOR_SERVER_NOTIFICATION = 3;
		
		public static final int RESULT_USER_EXCHANGE_MONEY_OK = 4;
		
		public static final int RESULT_USER_EXCHANGE_MONEY_FAIL = 5;
		
		public static final int RESULT_USER_PROTOCOL_OK = 6;
	}
}
