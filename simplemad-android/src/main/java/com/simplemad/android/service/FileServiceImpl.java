package com.simplemad.android.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.util.AppFileHelper;
import com.simplemad.android.util.AppFileNameGenerator;
import com.simplemad.android.util.StringUtil;
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.AdvertisementType;

public class FileServiceImpl implements FileService {

	private static final String PREVIEW_FOLDER_NAME = "preview";
	
	private static Context context;
	
	private static FileService fileService;
	
	private FileServiceImpl() {
		context = SimpleMadApp.instance().getBaseContext();
	}
	
	public static synchronized FileService instance() {
		if(fileService == null)
			fileService = new FileServiceImpl();
		return fileService;
	}
	@Override
	public File getFile(Advertisement advertisement) {
		return getFile(getFilePath(advertisement));
	}
	
	@Override
	public File getPreviewFile(Advertisement advertisement) {
		return getFile(getPreviewFilePath(advertisement));
	}

	private File getFile(String filePath) {
		if(StringUtil.isEmpty(filePath))
			return null;
		File file = new File(filePath);
		if(file.exists())
			return file;
		else
			return null;
	}

	@Override
	public InputStream getFileIS(Advertisement advertisement) {
		return getFileIS(getFile(advertisement));
	}

	@Override
	public InputStream getPreviewFileIS(Advertisement advertisement) {
		return getFileIS(getPreviewFile(advertisement));
	}
	
	private InputStream getFileIS(File file) {
		try {
			if(file == null) {
				return null;
			} else {
				return new FileInputStream(file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getFilePath(Advertisement advertisement) {
		return getFilePath(advertisement, false);
	}

	@Override
	public String getPreviewFilePath(Advertisement advertisement) {
		return getFilePath(advertisement, true);
	}
	
	private String getFilePath(Advertisement advertisement, boolean isPreviewFile) {
		if(advertisement == null) {
			return null;
		} else if(isPreviewFile) {
			return getFilePath(String.valueOf(advertisement.getMobile()), PREVIEW_FOLDER_NAME, advertisement.getPreviewFile(), false);
		} else {
			return getFilePath(String.valueOf(advertisement.getMobile()), advertisement.getAdType().getEnglishName(), advertisement.getFile(), AdvertisementType.VIDEO.equals(advertisement.getAdType()));
		}
	}
	
	private String getFilePath(String userName, String folderName, String fileName, boolean isVideo) {
		if(StringUtil.isEmpty(fileName)) {
			return null;
		}
		String sourceFolderDir = null;
		if(isVideo) {
			sourceFolderDir = AppFileHelper.getExternalFilesDir(context, userName, folderName);
		} else {
			sourceFolderDir = AppFileHelper.getFilesDir(context, userName, folderName);
		}
		return AppFileHelper.appendFile(sourceFolderDir, fileName);
	}

	@Override
	public void saveFile(Advertisement advertisement, byte[] fileByteArray) {
		if(advertisement == null || fileByteArray == null || fileByteArray.length == 0) {
			return;
		}
		if(StringUtil.isEmpty(advertisement.getFile())) {
			String fileName = AppFileNameGenerator.generateFileName(advertisement.getAdType(), advertisement.getFileExtendedName());
			advertisement.setFile(fileName);
		}
		String filePath = getFilePath(advertisement);
		try {
			AppFileHelper.saveResumeFile(filePath, fileByteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void savePreviewFile(Advertisement advertisement, byte[] fileByteArray) {
		if(advertisement == null || fileByteArray == null || fileByteArray.length == 0) {
			return;
		}
		if(StringUtil.isEmpty(advertisement.getPreviewFile())) {
			String fileName = AppFileNameGenerator.generateFileName(advertisement.getAdType(), advertisement.getPreviewFileExtendedName());
			advertisement.setPreviewFile(fileName);
		}
		String filePath = getPreviewFilePath(advertisement);
		try {
			AppFileHelper.saveFile(filePath, fileByteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteFileReference(Advertisement advertisement) {
		File file = getFile(advertisement);
		if(file != null) {
			file.deleteOnExit();
		}
		File previewFile = getPreviewFile(advertisement);
		if(previewFile != null) {
			previewFile.deleteOnExit();
		}
		return true;
	}

	@Override
	public boolean deleteFile(Advertisement advertisement) {
		File file = getFile(advertisement);
		if(file != null) {
			file.deleteOnExit();
		}
		return true;
	}

}
