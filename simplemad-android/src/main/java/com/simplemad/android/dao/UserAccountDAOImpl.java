package com.simplemad.android.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.simplemad.android.db.DBHelper;
import com.simplemad.android.db.DBHelperFactory;
import com.simplemad.android.db.DBHelperUtil;
import com.simplemad.android.util.BooleanUtil;
import com.simplemad.android.util.DateUtil;
import com.simplemad.bean.UserAccount;

public class UserAccountDAOImpl extends UserAccountDAO {

	private DBHelper helper;
	
	public UserAccountDAOImpl() {
		helper = DBHelperFactory.generateDBHelper();
	}

	@Override
	public UserAccount find(long mobile) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, COLUMNS_NAMES, COLUMNS_NAMES[5] + " = ? ", new String[]{String.valueOf(mobile)}, null, null, null);
		while(cursor.moveToNext()) {
			return generateUserAccount(cursor);
		}
		DBHelperUtil.close(cursor, db);
		return null;
	}

	@Override
	public List<UserAccount> getAllUserAccount() {
		return getUserAccountList(false);
	}
	
	private List<UserAccount> getUserAccountList(boolean isOrderByLoginTime) {
		List<UserAccount> userAccountList = new ArrayList<UserAccount>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		if(isOrderByLoginTime) {
			cursor = db.query(TABLE_NAME, COLUMNS_NAMES, null, null, null, null, COLUMNS_NAMES[1] + " desc ");
		} else {
			cursor = db.query(TABLE_NAME, COLUMNS_NAMES, null, null, null, null, null);
		}
		while(cursor.moveToNext()) {
			userAccountList.add(generateUserAccount(cursor));
		}
		DBHelperUtil.close(cursor, db);
		return userAccountList;
	}
	
	private UserAccount generateUserAccount(Cursor cursor) {
		UserAccount userAccount = new UserAccount();
		int index = 0;
		userAccount.setPassword(cursor.getString(index++));
		userAccount.setLoginTime(DateUtil.string2Date(cursor.getString(index++)));
		userAccount.setRemLoginStatus(BooleanUtil.toBoolean(cursor.getString(index++)));
		userAccount.setRemPassword(BooleanUtil.toBoolean(cursor.getString(index++)));
		userAccount.setLogined(BooleanUtil.toBoolean(cursor.getString(index++)));
		userAccount.setMobile(cursor.getLong(index++));
		return userAccount;
	}

	@Override
	public List<UserAccount> getAllUserAccountByLoginTime() {
		return getUserAccountList(true);
	}

	@Override
	public boolean add(UserAccount userAccount) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		long rowId = db.insert(TABLE_NAME, null, getContentValues(userAccount));
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
	
	private ContentValues getContentValues(UserAccount userAccount) {
		ContentValues values = new ContentValues();
		int index = 0;
		values.put(COLUMNS_NAMES[index++], userAccount.getPassword());
		values.put(COLUMNS_NAMES[index++], DateUtil.date2String(userAccount.getLoginTime()));
		values.put(COLUMNS_NAMES[index++], userAccount.isRemLoginStatus());
		values.put(COLUMNS_NAMES[index++], userAccount.isRemPassword());
		values.put(COLUMNS_NAMES[index++], userAccount.isLogined());
		values.put(COLUMNS_NAMES[index++], userAccount.getMobile());
		return values;
	}

	@Override
	public boolean delete(UserAccount userAccount) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		db.delete(TABLE_NAME, COLUMNS_NAMES[5] + " = ? ", new String[]{String.valueOf(userAccount.getMobile())});
		db.setTransactionSuccessful();
		DBHelperUtil.end(db);
		DBHelperUtil.close(db);
		return true;
	}

	@Override
	public boolean update(UserAccount userAccount) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		int rowId = db.update(TABLE_NAME, getContentValues(userAccount), COLUMNS_NAMES[5] + " = ? ", new String[]{String.valueOf(userAccount.getMobile())});
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
	public UserAccount first() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null; 
		cursor = db.query(TABLE_NAME, COLUMNS_NAMES, null, null, null, null, COLUMNS_NAMES[1] + " desc ", "1");
		while(cursor.moveToNext()) {
			UserAccount userAccount = generateUserAccount(cursor);
			DBHelperUtil.close(cursor, db);
			return userAccount;
		}
		DBHelperUtil.close(cursor, db);
		return null;
	}

}
