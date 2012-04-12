package com.simplemad.android.dao;

import java.util.List;

import com.simplemad.android.util.SQLiteType;
import com.simplemad.android.util.TableHelper;
import com.simplemad.bean.Advertisement;

public abstract class AdvertisementDAO {

	static final String TABLE_NAME = "advertisement";
	
	static final String[] COLUMNS_NAMES = new String[]{"ad_id", "times", 
													   "adType", "startDate", 
													   "endDate", "price", 
													   "file", "previewFile", 
													   "waitingTime", "ad_name", 
													   "isMessage", "mobile", 
													   "isSubmited", "fileExtendedName",
													   "previewFileExtendedName", "isDownloading",
													   "isCancelDownload", "isDownloaded",
													   "isOpened", "url",
													   "isSharable", "isShared",
													   "sharingPrice"};
	
	static final String[] COLUMNS_TYPES = new String[]{SQLiteType.TEXT, SQLiteType.INTEGER, 
													   SQLiteType.INTEGER, SQLiteType.TIMESTAMP, 
													   SQLiteType.TIMESTAMP, SQLiteType.INTEGER,
													   SQLiteType.TEXT, SQLiteType.TEXT,
													   SQLiteType.INTEGER, SQLiteType.TEXT,
													   SQLiteType.BOOLEAN, SQLiteType.TEXT,
													   SQLiteType.BOOLEAN, SQLiteType.TEXT,
													   SQLiteType.TEXT, SQLiteType.BOOLEAN,
													   SQLiteType.BOOLEAN, SQLiteType.BOOLEAN,
													   SQLiteType.BOOLEAN, SQLiteType.TEXT,
													   SQLiteType.BOOLEAN, SQLiteType.BOOLEAN,
													   SQLiteType.INTEGER};
	
	public static String createTableSQL() {
		return TableHelper.generateCreateTableSQL(TABLE_NAME, COLUMNS_NAMES, COLUMNS_TYPES);
	}
	
	public static String updateTableSQL() {
		StringBuffer buffer = new StringBuffer();
		return buffer.toString();
	}
	
	public abstract boolean add(Advertisement advertisement);
	
	public abstract boolean update(Advertisement advertisement);
	
	public abstract boolean delete(Advertisement advertisement);
	
	public abstract boolean delete(List<Advertisement> advertisementList);
	
	public abstract Advertisement find(String id, long mobile);
	
	public abstract List<Advertisement> findByMobile(long mobile);
	
}
