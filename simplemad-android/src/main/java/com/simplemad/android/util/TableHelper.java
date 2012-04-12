package com.simplemad.android.util;

public class TableHelper {

	public static String generateCreateTableSQL(String tableName, String[] columns, String[] columnTypes) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("create table if not exists ");
		buffer.append(tableName);
		buffer.append("(");
		for(int index = 0; index < columns.length; index++) {
			buffer.append(columns[index]);
			buffer.append(" ");
			if(index >= columnTypes.length) {
				buffer.append(SQLiteType.TEXT);
			} else {
				buffer.append(columnTypes[index]);
			}
			if(index != columns.length - 1)
				buffer.append(",");
		}
		buffer.append(")");
		return buffer.toString();
	}
}
