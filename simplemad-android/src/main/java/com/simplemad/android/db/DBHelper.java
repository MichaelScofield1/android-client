package com.simplemad.android.db;

import com.simplemad.android.dao.AdvertisementDAO;
import com.simplemad.android.dao.UserAccountDAO;
import com.simplemad.android.dao.UserAttributeDAO;
import com.simplemad.android.dao.UserDAO;
import com.simplemad.android.util.StringUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * the helper class is for db
 * @author kamen
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Simplemad";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DBHelper(Context context, int version) {
		super(context, DATABASE_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(UserAccountDAO.createTableSQL());
		db.execSQL(AdvertisementDAO.createTableSQL());
		db.execSQL(UserDAO.createTableSQL());
		db.execSQL(UserAttributeDAO.createTableSQL());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(!StringUtil.isEmpty(UserAccountDAO.updateTableSQL()))
			db.execSQL(UserAccountDAO.updateTableSQL());
		if(!StringUtil.isEmpty(AdvertisementDAO.updateTableSQL()))
			db.execSQL(AdvertisementDAO.updateTableSQL());
		if(!StringUtil.isEmpty(UserDAO.updateTableSQL()))
			db.execSQL(UserDAO.updateTableSQL());
		if(!StringUtil.isEmpty(UserAttributeDAO.updateTableSQL()))
			db.execSQL(UserAttributeDAO.updateTableSQL());
	}

}
