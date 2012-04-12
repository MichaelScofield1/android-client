package com.simplemad.android.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DownloadServiceManager {

	private static DownloadServiceManager manager;
	
	private Map<String, DownloadService> serviceMap = new HashMap<String, DownloadService>();
	
	private DownloadServiceManager() {
	}
	
	public static synchronized DownloadServiceManager instance() {
		if(manager == null) {
			manager = new DownloadServiceManager();
		}
		return manager;
	}
	
	public void addService(String key, DownloadService ds) {
		serviceMap.put(key, ds);
	}
	
	public void removeService(String key) {
		serviceMap.remove(key);
	}
	
	public DownloadService getService(String key) {
		return serviceMap.get(key);
	}
	
	public void stopService(String key) {
		DownloadService ds = serviceMap.remove(key);
		if(ds != null) {
			ds.stopService();
		}
	}
	
	public void stopAllServices() {
		Set<String> dsSet = serviceMap.keySet();
		Iterator<String> it = dsSet.iterator();
		while(it.hasNext()) {
			String key = it.next();
			stopService(key);
		}
	}
	
	public void cancelService(String key) {
		DownloadService ds = serviceMap.remove(key);
		if(ds != null) {
			ds.cancelDownload();
		}
	}
}
