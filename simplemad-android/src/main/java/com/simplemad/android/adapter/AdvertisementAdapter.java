package com.simplemad.android.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.simplemad.android.service.DownloadService;
import com.simplemad.android.service.DownloadServiceImpl;
import com.simplemad.android.service.DownloadServiceManager;
import com.simplemad.android.service.FileService;
import com.simplemad.android.service.FileServiceImpl;
import com.simplemad.android.util.CollectionUtil;
import com.simplemad.android.view.adList.AdListItemRelativeView;
import com.simplemad.android.view.adList.AdListItemRelativeViewSetting;
import com.simplemad.bean.Advertisement;

public class AdvertisementAdapter extends BaseAdapter {

	private List<Advertisement> adList;
	private Context context;
	private AdListItemRelativeViewSetting setting;
	private FileService fs;
	
	public AdvertisementAdapter(Context context, List<Advertisement> adList, AdListItemRelativeViewSetting setting) {
		this.context = context;
		this.adList = adList;
		this.setting = setting;
		fs = FileServiceImpl.instance();
	}
	
	@Override
	public void notifyDataSetChanged() {
		System.out.println("adList size : " + adList.size());
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(CollectionUtil.isEmpty(adList))
			return 0;
		return adList.size();
	}

	@Override
	public Object getItem(int position) {
		return adList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Advertisement advertisement = adList.get(position);
		AdListItemRelativeView view = new AdListItemRelativeView(context);
		view.setAdvertisement(advertisement, setting);
		
		File file = fs.getFile(advertisement);
		if(file != null && advertisement.isMessage()) {//下载一部分,续传
			if(advertisement.isCancelDownload()) {//如果已取消下载的,不自动续传,需要用户手动点下载再续传
				return view;
			} else {
				DownloadService ds = DownloadServiceManager.instance().getService(advertisement.getId());
				if(ds == null ) {
					ds = new DownloadServiceImpl();
					ds.setAdListItemRelativeView(view);
					ds.download(advertisement);
					DownloadServiceManager.instance().addService(advertisement.getId(), ds);
				}
			}
		}
		DownloadService ds = DownloadServiceManager.instance().getService(advertisement.getId());
		if(ds != null)
			ds.setAdListItemRelativeView(view);
		return view;
	}

}
