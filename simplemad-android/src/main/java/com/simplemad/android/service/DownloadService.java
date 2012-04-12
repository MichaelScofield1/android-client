package com.simplemad.android.service;

import com.simplemad.android.view.adList.AdListItemRelativeView;
import com.simplemad.bean.Advertisement;

public interface DownloadService {

	public void setAdListItemRelativeView(AdListItemRelativeView adView);
	
	public void download(Advertisement advertisement);
	
	public boolean downloadPreviewFile(Advertisement advertisement);
	
	public void cancelDownload();
	
	public void stopService();
}
