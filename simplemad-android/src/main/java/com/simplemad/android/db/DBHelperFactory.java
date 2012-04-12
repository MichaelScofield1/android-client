package com.simplemad.android.db;

import com.simplemad.android.SimpleMadApp;

import android.content.Context;

public class DBHelperFactory {

//	private static DBHelper dbHelper;
	
	public static synchronized DBHelper generateDBHelper() {
		return new DBHelper(SimpleMadApp.instance().getBaseContext(), SimpleMadApp.instance().getVersion());
//		if(dbHelper == null)
//			dbHelper = new DBHelper(SimpleMadApp.instance().getBaseContext(), SimpleMadApp.instance().getVersion());
//		return dbHelper;
	}
	
	public static synchronized DBHelper generateDBHelper(Context context, int version) {
		return new DBHelper(context, version);
//		if(dbHelper == null)
//			dbHelper = new DBHelper(context, version);
//		return dbHelper;
	}
}
