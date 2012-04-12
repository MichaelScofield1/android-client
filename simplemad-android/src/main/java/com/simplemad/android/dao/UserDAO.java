package com.simplemad.android.dao;

import com.simplemad.android.util.SQLiteType;
import com.simplemad.android.util.TableHelper;
import com.simplemad.bean.User;

public abstract class UserDAO {

	static final String TABLE_NAME = "user";
	
	static final String[] COLUMNS_NAMES = new String[]{"mobile", "userName", "money", "registerDate"};
	
	static final String[] COLUMNS_TYPES = new String[]{SQLiteType.INTEGER, SQLiteType.TEXT, SQLiteType.INTEGER, SQLiteType.TIMESTAMP};
	
	public static String createTableSQL() {
		return TableHelper.generateCreateTableSQL(TABLE_NAME, COLUMNS_NAMES, COLUMNS_TYPES);
	}
	
	public static String updateTableSQL() {
		StringBuffer buffer = new StringBuffer();
		return buffer.toString();
	}
	
	public abstract boolean add(User user);
	
	public abstract boolean update(User user);
	
	public abstract boolean delete(User user);
	
	public abstract User find(long mobile);
}
