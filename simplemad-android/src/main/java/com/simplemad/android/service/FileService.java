package com.simplemad.android.service;

import java.io.File;
import java.io.InputStream;

import com.simplemad.bean.Advertisement;

/**
 * 负责储存\读取\删除文件的操作
 * @author dynamic
 *
 */
public interface FileService {
	
	public File getFile(Advertisement advertisement);
	
	public File getPreviewFile(Advertisement advertisement);
	
	public InputStream getFileIS(Advertisement advertisement);
	
	public InputStream getPreviewFileIS(Advertisement advertisement);
	
	public String getFilePath(Advertisement advertisement);
	
	public String getPreviewFilePath(Advertisement advertisement);
	
	/**
	 * 保存advertisement对应的广告文件,并设置advertisement的file属性,当file已存在时,则把fileByteArray 保存至file的尾部
	 * @param advertisement
	 * @param fileByteArray
	 */
	public void saveFile(Advertisement advertisement, byte[] fileByteArray);
	
	/**
	 * 保存advertisement对应的预览图片,并设置advertisement的previewFile属性,,当previewFile已存在时,则把fileByteArray 保存至file的尾部
	 * @param advertisement
	 * @param fileByteArray
	 */
	public void savePreviewFile(Advertisement advertisement, byte[] fileByteArray);
	
	public boolean deleteFileReference(Advertisement advertisement);
	
	public boolean deleteFile(Advertisement advertisement);
	
}
