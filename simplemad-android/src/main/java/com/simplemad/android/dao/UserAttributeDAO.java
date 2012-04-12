package com.simplemad.android.dao;

import java.util.List;

import com.simplemad.android.util.SQLiteType;
import com.simplemad.android.util.TableHelper;
import com.simplemad.bean.UserAttribute;

public abstract class UserAttributeDAO {

static final String TABLE_NAME = "user_attribute";
	
	static final String[] COLUMNS_NAMES = new String[]{"mobile", "attr_key", "attr_value", "attr_order"};
	
	static final String[] COLUMNS_TYPES = new String[]{SQLiteType.TEXT, SQLiteType.TEXT, SQLiteType.TEXT, SQLiteType.INTEGER};
	
	public static String createTableSQL() {
		return TableHelper.generateCreateTableSQL(TABLE_NAME, COLUMNS_NAMES, COLUMNS_TYPES);
	}
	
	public static String updateTableSQL() {
		StringBuffer buffer = new StringBuffer();
		return buffer.toString();
	}
	
	public abstract boolean add(UserAttribute attr);
	
	public abstract boolean delete(long mobile);
	
	public abstract List<UserAttribute> find(long mobile);
}
