package com.simplemad.android.dao;

import java.util.List;

import com.simplemad.android.util.SQLiteType;
import com.simplemad.android.util.TableHelper;
import com.simplemad.bean.UserAccount;


public abstract class UserAccountDAO {
	
	static final String TABLE_NAME = "user_account";
	
	static final String[] COLUMNS_NAMES = new String[]{"password", "loginTime", "remLoginStatus", "remPassword", "logined", "mobile"};
	
	static final String[] COLUMNS_TYPES = new String[]{SQLiteType.TEXT, SQLiteType.TIMESTAMP, SQLiteType.BOOLEAN, SQLiteType.BOOLEAN, SQLiteType.BOOLEAN, SQLiteType.TEXT};
	
	public static String createTableSQL() {
		return TableHelper.generateCreateTableSQL(TABLE_NAME, COLUMNS_NAMES, COLUMNS_TYPES);
	}
	
	public static String updateTableSQL() {
		StringBuffer buffer = new StringBuffer();
		return buffer.toString();
	}
	
	public abstract UserAccount find(long mobile);
	
	public abstract UserAccount first();
	
	public abstract List<UserAccount> getAllUserAccount();
	
	public abstract List<UserAccount> getAllUserAccountByLoginTime();
	
	public abstract boolean add(UserAccount userAccount);
	
	public abstract boolean delete(UserAccount userAccount);
	
	public abstract boolean update(UserAccount userAccount);
	
}
