package com.simplemad.android.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.ClientUtil;
import com.simplemad.android.util.AppFileHelper;
import com.simplemad.android.view.ConfirmDialog;
import com.simplemad.android.view.ProgressView;
import com.simplemad.bean.PhoneSystemType;
import com.simplemad.bean.UpgradeInfo;

public class UpgradeServiceImpl implements UpgradeService {

	protected Context context;
	protected ProgressView progress;
	protected AlertDialog progressDialog;
	protected UpgradeInfo info;
	protected URLConnection conn;
	protected Handler handler;
	private Thread thread;
	protected InputStream is;
	protected FileOutputStream fos;
	protected File apkFile;
	
	protected int currentSize;
	protected int totalSize;
	
	private static final int DOWNLOAD_ERROR = -1;
	private static final int DOWNLOADING = 1;
	private static final int DOWNLOADED = 2;
	
	public UpgradeServiceImpl(Context context) {
		this.context = context;
	}

	@Override
	public void upgrade(String versionUrl) {
		if(SimpleMadApp.DEBUG_MODE) {
			return;
		}
		if(!SimpleMadApp.instance().isInNetwork()) {
			return;
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", PhoneSystemType.ANDROID);
			info = ClientUtil.doPost(versionUrl, params, UpgradeInfo.class);
			if(info == null) {
				return;
			}
			if(SimpleMadApp.instance().getVersion() >= info.getVersionCode()) {
				return;
			}
			ConfirmDialog cd = new ConfirmDialog(context, "检测到软件版本更新,是否更新软件?") {
				
				@Override
				public void ok() {
					startUpgrade();
				}
			};
			cd.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Handler createHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == DOWNLOAD_ERROR) {
					destroy();
				} else if(msg.what == DOWNLOADING) {
					progress.setProgress(currentSize, totalSize);
				} else if(msg.what == DOWNLOADED) {
					install();
					destroy();
				}
			}
		};
	}
	
	protected void install() {
		Intent install = new Intent(Intent.ACTION_VIEW);
		install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(install);
	}
	
	private void initUpgrade() {
		progress = new ProgressView(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("正在下载,请稍候");
		builder.setView(progress);
		builder.setCancelable(false);
		builder.setNegativeButton("取消下载", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				destroy();
			}
		});
		progressDialog = builder.create();
		progressDialog.show();
		handler = createHandler();
	}
	
	protected void startUpgrade() {
		initUpgrade();
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				download();
			}
		});
		thread.start();
	}
	
	protected void download() {
		try {
			URL url = new URL(info.getUpgradeUrl());
			URLConnection conn = url.openConnection();
			totalSize = conn.getContentLength();
			if(totalSize == 0) {
				destroy();
				return;
			}
			is = conn.getInputStream();
			String appRootFolderDir = AppFileHelper.getExternalFilesDir(context);
			apkFile = new File(AppFileHelper.appendFile(appRootFolderDir, "simplemad.apk"));
			fos = new FileOutputStream(apkFile);
			int len = 0;
			int oldFactor = 0;
			int newFactor = 0;
			byte[] data = new byte[1024];
			while((len = is.read(data)) != -1) {
				fos.write(data, 0, len);
				currentSize += len;
				newFactor = (currentSize * 100) / totalSize;
				if(newFactor != oldFactor) {
					handler.sendEmptyMessage(DOWNLOADING);
					oldFactor = newFactor;
				}
			}
			handler.sendEmptyMessage(DOWNLOADED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			destroy();
		} catch (IOException e) {
			e.printStackTrace();
			destroy();
		} finally {
			closeStream();
		}
	}
	
	protected void closeStream() {
		if(is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		closeStream();
		if(thread != null && !thread.isInterrupted()) {
			thread.interrupt();
		}
		if(progressDialog != null) {
			progressDialog.dismiss();
		}
	}

}
