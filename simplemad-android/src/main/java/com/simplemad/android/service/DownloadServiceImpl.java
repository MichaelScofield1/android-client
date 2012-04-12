package com.simplemad.android.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.AdEffectEntityUtil;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.view.adList.AdListItemRelativeView;
import com.simplemad.bean.AdEffectEntity;
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.AdvertisementType;
import com.simplemad.parameter.ClientParameter;

public class DownloadServiceImpl extends Thread implements DownloadService {

	private static final int DOWNLOADING = 0;
	private static final int DOWNLOAD_COMPLETE = 1;
	private static final int DOWNLOAD_ERROR = 1;
	
	private Handler handler;
	protected Advertisement advertisement;
	protected AdListItemRelativeView adView;
	private int currentSize;
	private int totalSize;
	private InputStream is;
	private FileService fileService = FileServiceImpl.instance();
	private AdvertisementService adService = AdvertisementServiceImpl.instance();
	
	@Override
	public void setAdListItemRelativeView(AdListItemRelativeView adView) {
		this.adView = adView;
		if(totalSize > 0)
			this.adView.setProcess(totalSize, currentSize);
	}
	
	@Override
	public void download(Advertisement advertisement) {
		this.advertisement = advertisement;
		createHandler();
		start();
	}
	
	private void createHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == DOWNLOADING) {
					adView.setProcess(totalSize, currentSize);
				} else if(msg.what == DOWNLOAD_COMPLETE) {
					sendIntentMessage(true);
					DownloadServiceManager.instance().removeService(advertisement.getId());
				}  else if(msg.what == DOWNLOAD_ERROR) {
					sendIntentMessage(false);
					DownloadServiceManager.instance().removeService(advertisement.getId());
				}
				removeMessages(msg.what);
			}
		};
	}
	
	protected void sendIntentMessage(boolean result) {
		Intent intent = new Intent(AppUtil.RECEIVER_DOWNLOAD_COMPLETE);
		intent.putExtra(AppUtil.EXTRA_NAME_DOWNLOAD_AD_RESULT, result);
		intent.putExtra(AppUtil.EXTRA_NAME_AD, advertisement);
		adView.getContext().sendBroadcast(intent);
	}
	
	protected boolean canDownloadContent(Advertisement advertisement) {
		if(advertisement == null || advertisement.isDownloaded()) {
			return false;
		}
		if(AdvertisementType.HTML.equals(advertisement.getAdType()) || AdvertisementType.INTERACTION.equals(advertisement.getAdType())) {
			return false;
		}
		return true;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Download Thread start...");
			if(!canDownloadContent(advertisement)) {
				if(!SimpleMadApp.DEBUG_MODE) {
					adService.saveAdvertisementFile(advertisement.getId(), null, true);
				}
				handler.sendEmptyMessage(DOWNLOAD_COMPLETE);
				return;
			}
			URL url = null;
			if(!SimpleMadApp.instance().isInNetwork()) {
				handler.sendEmptyMessage(DOWNLOAD_ERROR);
				return;
			} else if(SimpleMadApp.DEBUG_MODE) {
				url = new URL("http://labs.renren.com/apache-mirror/httpd/httpd-2.2.21.tar.gz");
			} else {
				url = new URL(ClientParameter.AD_DOWNLOAD_CONTENT + "?adId=" + advertisement.getId());
			}
			AdEffectEntityUtil.send(advertisement, AdEffectEntity.KEY_AD_DOWNLOADING);
			URLConnection urlConn = url.openConnection();
			currentSize = 0;
			if(advertisement != null) {
				File file = fileService.getFile(advertisement);
				if(file != null && file.exists())
					currentSize = (int) file.length();
			}
			urlConn.setRequestProperty("User-Agent","NetFox");
			urlConn.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
			totalSize = urlConn.getContentLength();
			if(currentSize == totalSize) {
				handler.sendEmptyMessage(DOWNLOAD_COMPLETE);
				return;
			}
			stopService();
			is = urlConn.getInputStream();
			System.out.println("TotalSize:" + totalSize);
			handler.sendEmptyMessage(DOWNLOADING);
			int block = 1024;
			byte[] dataArray = new byte[block];
			int len = 0;
			int factor = 0;
			int newFactor = 0;
			while((len = is.read(dataArray)) != -1) {
				currentSize += len;
				if(!SimpleMadApp.DEBUG_MODE) {
					adService.saveAdvertisementFile(advertisement.getId(), dataArray, currentSize == totalSize);
				}
				newFactor = currentSize * 100 /totalSize;
				if(factor != newFactor) {//仅更新进度条100次
					handler.sendEmptyMessage(DOWNLOADING);
					factor = newFactor;
				}
			}
			System.out.println("Download complete...");
			handler.sendEmptyMessage(DOWNLOAD_COMPLETE);
			AdEffectEntityUtil.send(advertisement, AdEffectEntity.KEY_AD_DOWNLOADED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(DOWNLOAD_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(DOWNLOAD_ERROR);
		} finally {
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
	}
	
	@Override
	public void stopService() {
		if(is != null) {
			try {
				interrupt();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void cancelDownload() {
		stopService();
		if(advertisement != null) {
			advertisement.setCancelDownload(true);
			AdvertisementServiceImpl.instance().update(advertisement);
		}
	}
	
	protected boolean canDownloadIcon(Advertisement advertisement) {
		if(advertisement == null) {
			return false;
		}
		Advertisement localAd = adService.find(advertisement.getId());
		if(localAd == null) {
			return true;
		}
		if(localAd.getMobile() == advertisement.getMobile()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean downloadPreviewFile(Advertisement advertisement) {
		boolean isDownloaded = false;
		if(!canDownloadIcon(advertisement)) {
			return false;
		}
		InputStream is = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			System.out.println("Download PreviewFile start...");
			URL url = null;
			if(SimpleMadApp.DEBUG_MODE) {
				url = new URL("http://labs.renren.com/apache-mirror//httpd/httpd-2.2.21.tar.gz");
			} else {
				url = new URL(ClientParameter.AD_DOWNLOAD_ICON + "?adId=" + advertisement.getId());
			}
			URLConnection urlConn = url.openConnection();
			urlConn.setRequestProperty("User-Agent","NetFox");
			int length = urlConn.getContentLength();
			is = urlConn.getInputStream();
			System.out.println("TotalSize:" + length);
			byte[] dataArray = new byte[1024];
			int lengthRead = 0;
			while((lengthRead = is.read(dataArray)) != -1) {
				bos.write(dataArray, 0, lengthRead);
			}
			if(SimpleMadApp.DEBUG_MODE) {
				isDownloaded = true;
			} else {
				isDownloaded = adService.saveNewAdvertisement(advertisement, bos.toByteArray());
				bos.close();
			}
			System.out.println("Download complete...");
			return isDownloaded;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			return isDownloaded;
	}

}
