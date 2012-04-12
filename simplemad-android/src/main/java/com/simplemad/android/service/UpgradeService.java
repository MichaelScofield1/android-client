package com.simplemad.android.service;


public interface UpgradeService {
	
	void upgrade(String versionUrl);
	
	void destroy();
}
