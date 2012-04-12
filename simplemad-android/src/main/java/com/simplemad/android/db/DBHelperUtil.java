package com.simplemad.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelperUtil {

	public static void begin(SQLiteDatabase db) {
		db.beginTransaction();
	}
	
	public static void end(SQLiteDatabase db) {
		if(db.inTransaction())
			db.endTransaction();
	}
	
	public static void close(Cursor cursor) {
		if(!cursor.isClosed())
			cursor.close();
	}
	
	public static void close(SQLiteDatabase db) {
		if(db.isOpen())
			db.close();
	}
	
	public static void close(Cursor cursor, SQLiteDatabase db) {
		close(cursor);
		close(db);
	}
}
