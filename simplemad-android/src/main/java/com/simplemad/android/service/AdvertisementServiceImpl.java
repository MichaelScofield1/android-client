package com.simplemad.android.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.AdEffectEntityUtil;
import com.simplemad.android.client.ClientUtil;
import com.simplemad.android.dao.AdvertisementDAO;
import com.simplemad.android.dao.AdvertisementDAOImpl;
import com.simplemad.android.util.AppFileNameGenerator;
import com.simplemad.bean.AdEffectEntity;
import com.simplemad.bean.Advertisement;
import com.simplemad.parameter.ClientParameter;

public class AdvertisementServiceImpl implements AdvertisementService {
	
	private static AdvertisementService adService;
	private static AdvertisementDAO adDao;
	private static FileService fileService;
	private static UserService userService;
	
	private static long mobile;
	
	private AdvertisementServiceImpl() {
		adDao = new AdvertisementDAOImpl();
		fileService = FileServiceImpl.instance();
		if(UserAccountServiceImpl.instance().getCurrentUserAccount() == null) {
			UserAccountServiceImpl.instance().loginrecently();
		}
		mobile = UserAccountServiceImpl.instance().getCurrentUserAccount().getMobile();
		userService = UserServiceImpl.instance();
		userService.setMobile(mobile);
	}
	
	public static synchronized AdvertisementService instance() {
		if(adService == null || mobile != UserAccountServiceImpl.instance().getCurrentUserAccount().getMobile()) {
			adService = new AdvertisementServiceImpl();
		}
		return adService;
	}
	@Override
	public boolean delete(String id) {
		Advertisement advertisement = adDao.find(id, mobile);
		if(advertisement == null)
			return false;
		fileService.deleteFileReference(advertisement);
		adDao.delete(advertisement);
		return true;
	}

	@Override
	public boolean saveAdvertisementFile(String id, byte[] dataArray, boolean isCompleted) {
		AdvertisementDAO adDao = new AdvertisementDAOImpl();
		Advertisement advertisement = adDao.find(id, mobile);
		fileService.saveFile(advertisement, dataArray);
		if(!advertisement.isDownloading() && !advertisement.isDownloaded()) {
			advertisement.setDownloading(true);
			adDao.update(advertisement);
		}
		if(isCompleted) {
			advertisement.setMessage(false);
			advertisement.setDownloading(false);
			advertisement.setDownloaded(true);
			return adDao.update(advertisement);
		} else {
			return true;
		}
	}
	
	@Override
	public boolean saveNewAdvertisement(Advertisement advertisement, byte[] dataArray) {
		advertisement.setMessage(true);
		advertisement.setSubmited(false);
		advertisement.setTimes(0);
		advertisement.setMobile(mobile);
		advertisement.setPreviewFile(AppFileNameGenerator.generateFileName(advertisement.getAdType(), advertisement.getPreviewFileExtendedName()));
		advertisement.setFile(AppFileNameGenerator.generateFileName(advertisement.getAdType(), advertisement.getFileExtendedName()));
		fileService.savePreviewFile(advertisement, dataArray);
		return adDao.add(advertisement);
	}

	@Override
	public boolean deleteAllSeenAd() {
		List<Advertisement> allAdList = adDao.findByMobile(mobile);
		List<Advertisement> localSeenAdList = new ArrayList<Advertisement>();
		for(Advertisement advertisement : allAdList) {
			if(!advertisement.isMessage() && advertisement.isSubmited())
				localSeenAdList.add(advertisement);
		}
		return adDao.delete(localSeenAdList);
	}

	@Override
	public List<Advertisement> findLocalAd() {
		List<Advertisement> allAdList = adDao.findByMobile(mobile);
		List<Advertisement> localAdList = new ArrayList<Advertisement>();
		for(Advertisement advertisement : allAdList) {
			if(!advertisement.isMessage())
				localAdList.add(advertisement);
		}
		return localAdList;
	}

	@Override
	public List<Advertisement> findRemoteAd() {
		List<Advertisement> allAdList = adDao.findByMobile(mobile);
		List<Advertisement> localAdList = new ArrayList<Advertisement>();
		for(Advertisement advertisement : allAdList) {
			if(advertisement.isMessage())
				localAdList.add(advertisement);
		}
		return localAdList;
	}

	@Override
	public void playOnce(String id) {
		Advertisement advertisement = adDao.find(id, mobile);
		if(advertisement == null) {
			return;
		}
		if(!advertisement.isOpened()) {
			advertisement.setOpened(true);
		}
		advertisement.setTimes(advertisement.getTimes() + 1);
		adDao.update(advertisement);
		if(!SimpleMadApp.DEBUG_MODE && SimpleMadApp.instance().isInNetwork())
			AdEffectEntityUtil.send(advertisement, AdEffectEntity.KEY_AD_TIMES, advertisement.getTimes());
	}

	@Override
	public void update(Advertisement advertisement) {
		adDao.update(advertisement);
	}
	
	@Override
	public long earnAdMoney(String id) throws IOException {
		long money = 0;
		Advertisement advertisement = adDao.find(id, mobile);
		if(advertisement == null || advertisement.isSubmited()) {
			return money;
		}
		//submit to the server side
		money = earnMoneyServer(advertisement);
		advertisement.setSubmited(true);
		adDao.update(advertisement);
		userService.addMoney(money);
		return money;
	}
	
	private long earnMoneyServer(Advertisement advertisement) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("adId", advertisement.getId());
		params.put("mobile", advertisement.getMobile());
		
		return ClientUtil.doPost(ClientParameter.USER_EARN_AD_MONEY, params, Long.class);
	}

	@Override
	public long earnSharingMoney(String id) throws IOException {
		long money = 0;
		Advertisement advertisement = adDao.find(id, mobile);
		if(advertisement == null || !advertisement.isSharable() || advertisement.isShared()) {
			return money;
		}
		//submit to the server side
		money = earnSharingMoneyServer(advertisement);
		advertisement.setShared(true);
		adDao.update(advertisement);
		userService.addMoney(money);
		return money;
	}
	
	private long earnSharingMoneyServer(Advertisement advertisement) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("adId", advertisement.getId());
		params.put("mobile", advertisement.getMobile());
		
		return ClientUtil.doPost(ClientParameter.USER_EARN_SHARING_MONEY, params, Long.class);
	}

	@Override
	public boolean isExist(String id) {
		if(adDao.find(id, mobile) == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Advertisement find(String id) {
		return adDao.find(id, mobile);
	}

}
