package com.simplemad.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.simplemad.android.db.DBHelper;
import com.simplemad.android.db.DBHelperFactory;
import com.simplemad.android.db.DBHelperUtil;
import com.simplemad.android.util.DateUtil;
import com.simplemad.bean.User;

public class UserDAOImpl extends UserDAO {

	private DBHelper helper;
	
	public UserDAOImpl() {
		helper = DBHelperFactory.generateDBHelper();
	}
	
	@Override
	public boolean add(User user) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		long rowId = db.insert(TABLE_NAME, null, getContentValues(user));
		if(rowId != -1)
			db.setTransactionSuccessful();
		DBHelperUtil.end(db);
		DBHelperUtil.close(db);
		if(rowId == -1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean update(User user) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		int rowId = db.update(TABLE_NAME, getContentValues(user), COLUMNS_NAMES[0] + " = ? ", new String[]{String.valueOf(user.getMobile())});
		if(rowId != -1)
			db.setTransactionSuccessful();
		DBHelperUtil.end(db);
		DBHelperUtil.close(db);
		if(rowId == -1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean delete(User user) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		db.delete(TABLE_NAME, COLUMNS_NAMES[0] + " = ? ", new String[]{String.valueOf(user.getMobile())});
		db.setTransactionSuccessful();
		DBHelperUtil.end(db);
		DBHelperUtil.close(db);
		return true;
	}

	@Override
	public User find(long mobile) {
		User user = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, COLUMNS_NAMES, COLUMNS_NAMES[0] + " = ? ", new String[]{String.valueOf(mobile)}, null, null, null);
		while(cursor.moveToNext()) {
			user = generateUser(cursor);
			break;
		}
		DBHelperUtil.close(cursor, db);
		return user;
	}
	
	private User generateUser(Cursor cursor) {
		User user = new User();
		int index = 0;
		user.setMobile(cursor.getLong(index++));
		user.setUserName(cursor.getString(index++));
		user.setMoney(cursor.getLong(index++));
		user.setRegisterDate(DateUtil.string2Date(cursor.getString(index++)));
		return user;
	}
	
	private ContentValues getContentValues(User user) {
		ContentValues values = new ContentValues();
		int index = 0;
		values.put(COLUMNS_NAMES[index++], user.getMobile());
		values.put(COLUMNS_NAMES[index++], user.getUserName());
		values.put(COLUMNS_NAMES[index++], user.getMoney());
		values.put(COLUMNS_NAMES[index++], DateUtil.date2String(user.getRegisterDate()));
		return values;
	}

}
