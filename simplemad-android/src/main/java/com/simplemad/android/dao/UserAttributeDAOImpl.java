package com.simplemad.android.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.simplemad.android.db.DBHelper;
import com.simplemad.android.db.DBHelperFactory;
import com.simplemad.android.db.DBHelperUtil;
import com.simplemad.bean.UserAttribute;

public class UserAttributeDAOImpl extends UserAttributeDAO {

	private DBHelper helper;
	
	public UserAttributeDAOImpl() {
		helper = DBHelperFactory.generateDBHelper();
	}
	
	@Override
	public boolean add(UserAttribute attr) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		long rowId = db.insert(TABLE_NAME, null, getContentValues(attr));
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
	public boolean delete(long mobile) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		db.delete(TABLE_NAME, COLUMNS_NAMES[0] + " = ? ", new String[]{String.valueOf(mobile)});
		db.setTransactionSuccessful();
		DBHelperUtil.end(db);
		DBHelperUtil.close(db);
		return true;
	}

	@Override
	public List<UserAttribute> find(long mobile) {
		List<UserAttribute> attrs = new ArrayList<UserAttribute>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, COLUMNS_NAMES, COLUMNS_NAMES[0] + " = ? ", new String[]{String.valueOf(mobile)}, null, null, COLUMNS_NAMES[3]);
		while(cursor.moveToNext()) {
			attrs.add(generateAttr(cursor));
		}
		DBHelperUtil.close(cursor, db);
		return attrs;
	}
	
	private UserAttribute generateAttr(Cursor cursor) {
		UserAttribute attr = new UserAttribute();
		int index = 0;
		attr.setMobile(cursor.getLong(index++));
		attr.setKey(cursor.getString(index++));
		attr.setValue(cursor.getString(index++));
		attr.setOrder(cursor.getInt(index++));
		return attr;
	}
	
	private ContentValues getContentValues(UserAttribute attr) {
		ContentValues values = new ContentValues();
		int index = 0;
		values.put(COLUMNS_NAMES[index++], attr.getMobile());
		values.put(COLUMNS_NAMES[index++], attr.getKey());
		values.put(COLUMNS_NAMES[index++], attr.getValue());
		values.put(COLUMNS_NAMES[index++], attr.getOrder());
		return values;
	}

}
