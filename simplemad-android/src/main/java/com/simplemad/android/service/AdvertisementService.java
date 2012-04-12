package com.simplemad.android.service;

import java.io.IOException;
import java.util.List;

import com.simplemad.bean.Advertisement;


public interface AdvertisementService {

	boolean delete(String id);
	
	boolean saveAdvertisementFile(String id, byte[] dataArray, boolean isCompleted);
	
	boolean saveNewAdvertisement(Advertisement advertisement, byte[] dataArray);
	
	/**
	 * earn money from the server side and update the advertisement for times and isSubmitted <br/>
	 * and update the local user account
	 * @param id
	 * @return
	 */
	long earnAdMoney(String id) throws IOException;
	
	long earnSharingMoney(String id) throws IOException;
	
	boolean deleteAllSeenAd();
	
	List<Advertisement> findLocalAd();
	
	List<Advertisement> findRemoteAd();
	
	public void playOnce(String id);
	
	public void update(Advertisement advertisement);
	
	public boolean isExist(String id);
	
	public Advertisement find(String id);
}
