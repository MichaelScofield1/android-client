package com.simplemad.android.util;

public interface SQLiteType {

	public static final String NULL = "NULL";
	
	public static final String TEXT = "TEXT";
	
	public static final String INTEGER = "INTEGER";
	
	public static final String REAL = "REAL";
	
	public static final String BLOB = "BLOB";
	
	public static final String SMALLINT = "SMALLINT";
	
	public static final String BOOLEAN = "BOOLEAN";
	
	public static final String FLOAT = "FLOAT";
	
	public static final String DOUBLE = "DOUBLE";
	
	/*2010-08-01*/
	public static final String DATE = "DATE";
	
	/*hh:mm:ss*/
	public static final String TIME = "TIME";
	
	/*yyyy-mm-dd hh:mm:ss:ms*/
	public static final String TIMESTAMP = "TIMESTAMP";
	
	/*CHARACTER(n) n < 254*/
	public static final String CHAR = "CHARACTER";
	
	/*VARCHAR(n) n < 400*/
	public static final String VARCHAR = "VARCHAR";
	
	public static final String NVARCHAR = "NVARCHAR";
	
	/*NUMERIC(p,s)*/
	public static final String NUMERIC = "NUMERIC";
	
}
