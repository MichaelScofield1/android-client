package com.simplemad.android.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import android.content.Context;
import android.os.Environment;

public final class AppFileHelper {

	private static final String FILE_SEPERATOR = File.separator;
	
	public static String getFilesDir(Context context, String userName, String sourceFolderName) {
		File systemRootFolder = context.getFilesDir();
		if(!systemRootFolder.exists()) {
			systemRootFolder.mkdirs();
		}
		String userRootFolderDir = appendFile(systemRootFolder.getAbsolutePath(), userName);
		File userRootFolder = new File(userRootFolderDir);
		if(!userRootFolder.exists()) {
			userRootFolder.mkdirs();
		}
		String sourceFolderDir = appendFile(userRootFolderDir, sourceFolderName);
		File sourceFolder = new File(sourceFolderDir);
		if(!sourceFolder.exists()) {
			sourceFolder.mkdirs();
		}
		
		return sourceFolderDir;
	}
	
	public static String getExternalFilesDir(Context context, String userName, String sourceFolderName) {
		String appRootFolderDir = getExternalFilesDir(context);
		String userRootFolderDir = appendFile(appRootFolderDir, userName);
		File userRootFolder = new File(userRootFolderDir);
		if(!userRootFolder.exists()) {
			userRootFolder.mkdirs();
		}
		String sourceFolderDir = appendFile(userRootFolderDir, sourceFolderName);
		File sourceFolder = new File(sourceFolderDir);
		if(!sourceFolder.exists()) {
			sourceFolder.mkdirs();
		}
		
		return sourceFolderDir;
	}
	
	public static String getExternalFilesDir(Context context) {
		File systemRootFolder = Environment.getExternalStorageDirectory();
		if(!systemRootFolder.exists()) {
			systemRootFolder.mkdirs();
		}
		String appRootFolderDir = appendFile(systemRootFolder.getAbsolutePath(), "simplemad");
		File appRootFolder = new File(appRootFolderDir);
		if(!appRootFolder.exists()) {
			appRootFolder.mkdirs();
		}
		return appRootFolder.getAbsolutePath();
	}

	public static String appendFile(String folder, String file) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(folder);
		if(!folder.endsWith(FILE_SEPERATOR)) {
			buffer.append(FILE_SEPERATOR);
		}
		buffer.append(file);
		return buffer.toString();
	}
	
	public static byte[] readFile(String filePath) throws IOException {
		if(StringUtil.isEmpty(filePath)) {
			return null;
		} else {
			return readFile(new File(filePath));
		}
	}
	
	public static byte[] readFile(File file) throws IOException {
		if(file == null)
			return null;
		return readFile(new FileInputStream(file));
	}
	
	public static byte[] readFile(InputStream is) throws IOException {
		if(is == null)
			return null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			bis.close();
		} catch(IOException e) {
			throw e;
		}
		byte[] result = bos.toByteArray();
		bos.close();
		return result;
	}
	
	public static void saveFile(String filePath, byte[] fileContent) throws IOException {
		if(StringUtil.isEmpty(filePath)) {
			return;
		}
		File file = new File(filePath);
		saveFile(file, fileContent);
	}
	
	public static void saveResumeFile(String filePath, byte[] fileContent) throws IOException {
		if(StringUtil.isEmpty(filePath)) {
			return;
		}
		File file = new File(filePath);
		saveResumeFile(file, fileContent);
	}
	
	public static void saveFile(File file, byte[] fileContent) throws IOException {
		if(file == null || file.isDirectory()) {
			return;
		}
		if(!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		try {
			fos.write(fileContent);
		} finally {
			if(fos != null)
				fos.close();
		}
		
	}
	
	/**
	 * 续传
	 * @param file
	 * @param fileContent
	 * @throws IOException
	 */
	public static void saveResumeFile(File file, byte[] fileContent) throws IOException {
		if(file == null || file.isDirectory()) {
			return;
		}
		if(!file.exists()) {
			file.createNewFile();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rws");
		try {
			raf.seek(file.length());
			raf.write(fileContent);
		} finally {
			if(raf != null)
				raf.close();
		}
		
	}
	
	public static String translateFileUrl(String filePath) {
		return "file://" + filePath;
	}
}
