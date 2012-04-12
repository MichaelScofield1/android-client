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
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.AdvertisementType;

public class AdvertisementDAOImpl extends AdvertisementDAO {

	private DBHelper helper;
	
	public AdvertisementDAOImpl() {
		helper = DBHelperFactory.generateDBHelper();
	}
	@Override
	public boolean add(Advertisement advertisement) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		long rowId = db.insert(TABLE_NAME, null, getContentValues(advertisement));
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
	
	private ContentValues getContentValues(Advertisement advertisement) {
		ContentValues values = new ContentValues();
		int index = 0;
		values.put(COLUMNS_NAMES[index++], advertisement.getId());
		values.put(COLUMNS_NAMES[index++], advertisement.getTimes());
		values.put(COLUMNS_NAMES[index++], advertisement.getAdType().name());
		values.put(COLUMNS_NAMES[index++], DateUtil.date2String(advertisement.getStartDate()));
		values.put(COLUMNS_NAMES[index++], DateUtil.date2String(advertisement.getEndDate()));
		values.put(COLUMNS_NAMES[index++], advertisement.getPrice());
		values.put(COLUMNS_NAMES[index++], advertisement.getFile());
		values.put(COLUMNS_NAMES[index++], advertisement.getPreviewFile());
		values.put(COLUMNS_NAMES[index++], advertisement.getWaitingTime());
		values.put(COLUMNS_NAMES[index++], advertisement.getName());
		values.put(COLUMNS_NAMES[index++], advertisement.isMessage());
		values.put(COLUMNS_NAMES[index++], advertisement.getMobile());
		values.put(COLUMNS_NAMES[index++], advertisement.isSubmited());
		values.put(COLUMNS_NAMES[index++], advertisement.getFileExtendedName());
		values.put(COLUMNS_NAMES[index++], advertisement.getPreviewFileExtendedName());
		values.put(COLUMNS_NAMES[index++], advertisement.isDownloading());
		values.put(COLUMNS_NAMES[index++], advertisement.isCancelDownload());
		values.put(COLUMNS_NAMES[index++], advertisement.isDownloaded());
		values.put(COLUMNS_NAMES[index++], advertisement.isOpened());
		values.put(COLUMNS_NAMES[index++], advertisement.getUrl());
		values.put(COLUMNS_NAMES[index++], advertisement.isSharable());
		values.put(COLUMNS_NAMES[index++], advertisement.isShared());
		values.put(COLUMNS_NAMES[index++], advertisement.getSharingPrice());
		
		return values;
	}

	@Override
	public boolean update(Advertisement advertisement) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		int rowId = db.update(TABLE_NAME, getContentValues(advertisement), COLUMNS_NAMES[0] + " = ? and " + COLUMNS_NAMES[11] + " = ? ", new String[]{String.valueOf(advertisement.getId()), String.valueOf(advertisement.getMobile())});
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
	public boolean delete(Advertisement advertisement) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		db.delete(TABLE_NAME, COLUMNS_NAMES[0] + " = ? and " + COLUMNS_NAMES[11] + " = ? ", new String[]{String.valueOf(advertisement.getId()), String.valueOf(advertisement.getMobile())});
		db.setTransactionSuccessful();
		DBHelperUtil.end(db);
		DBHelperUtil.close(db);
		return true;
	}

	@Override
	public Advertisement find(String id, long mobile) {
		Advertisement advertisement = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, COLUMNS_NAMES, COLUMNS_NAMES[0] + " = ? and " + COLUMNS_NAMES[11] + " = ? ", new String[]{id, String.valueOf(mobile)}, null, null, null);
		while(cursor.moveToNext()) {
			advertisement = generateAdvertisement(cursor);
			break;
		}
		DBHelperUtil.close(cursor, db);
		return advertisement;
	}

	@Override
	public List<Advertisement> findByMobile(long mobile) {
		List<Advertisement> advertisementList = new ArrayList<Advertisement>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, COLUMNS_NAMES, COLUMNS_NAMES[11] + " = ? ", new String[]{String.valueOf(mobile)}, null, null, null);
		while(cursor.moveToNext()) {
			advertisementList.add(generateAdvertisement(cursor));
		}
		DBHelperUtil.close(cursor, db);
		return advertisementList;
	}
	
	private Advertisement generateAdvertisement(Cursor cursor) {
		Advertisement advertisement = new Advertisement();
		int index = 0;
		advertisement.setId(cursor.getString(index++));
		advertisement.setTimes(cursor.getInt(index++));
		advertisement.setAdType(AdvertisementType.valueOf(cursor.getString(index++)));
		advertisement.setStartDate(DateUtil.string2Date(cursor.getString(index++)));
		advertisement.setEndDate(DateUtil.string2Date(cursor.getString(index++)));
		advertisement.setPrice(cursor.getLong(index++));
		advertisement.setFile(cursor.getString(index++));
		advertisement.setPreviewFile(cursor.getString(index++));
		advertisement.setWaitingTime(cursor.getInt(index++));
		advertisement.setName(cursor.getString(index++));
		advertisement.setMessage(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setMobile(cursor.getLong(index++));
		advertisement.setSubmited(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setFileExtendedName(cursor.getString(index++));
		advertisement.setPreviewFileExtendedName(cursor.getString(index++));
		advertisement.setDownloading(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setCancelDownload(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setDownloaded(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setOpened(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setUrl(cursor.getString(index++));
		advertisement.setSharable(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setShared(BooleanUtil.toBoolean(cursor.getString(index++)));
		advertisement.setSharingPrice(cursor.getLong(index++));
		return advertisement;
	}
	
	@Override
	public boolean delete(List<Advertisement> advertisementList) {
		SQLiteDatabase db = helper.getReadableDatabase();
		DBHelperUtil.begin(db);
		for(Advertisement advertisement : advertisementList) {
			db.delete(TABLE_NAME, COLUMNS_NAMES[0] + " = ? and " + COLUMNS_NAMES[11] + " = ? ", new String[]{String.valueOf(advertisement.getId()), String.valueOf(advertisement.getMobile())});
		}
		db.setTransactionSuccessful();
		DBHelperUtil.end(db);
		DBHelperUtil.close(db);
		return true;
	}

}
