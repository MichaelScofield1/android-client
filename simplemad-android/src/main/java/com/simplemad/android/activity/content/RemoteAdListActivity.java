package com.simplemad.android.activity.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.activity.NoTitleActivity;
import com.simplemad.android.activity.RequestAndResultCode;
import com.simplemad.android.activity.ad.AdvertisementPlayActivity;
import com.simplemad.android.adapter.AdvertisementAdapter;
import com.simplemad.android.service.AdvertisementService;
import com.simplemad.android.service.AdvertisementServiceImpl;
import com.simplemad.android.service.DownloadService;
import com.simplemad.android.service.DownloadServiceImpl;
import com.simplemad.android.service.DownloadServiceManager;
import com.simplemad.android.setting.AdListSetting;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.DateUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.ConfirmDialog;
import com.simplemad.android.view.adList.AdListItemRelativeView;
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.AdvertisementType;

public class RemoteAdListActivity extends NoTitleActivity {

	private AdvertisementService adService;
	private List<Advertisement> data;
	private BaseAdapter listViewAdapter;
	private AdDownloadCompletedReceiverAtRemote remoteReceiver;
	private NewAdReceiverAtRemote newAdReceiver;
	protected ListView listView;
	private TextView title;
	/*for context menu*/
	protected Advertisement selectedAd;
	protected AdListItemRelativeView adView;
	
	private AdListSetting setting;
	
	private boolean isActive;
	
//	@Override
//	public void onBackPressed() {
//		System.out.println("RemoteAd bakc pressed...");
//		AppUtil.homePresse(this);
//		startService(new Intent(this, AppDisplayService.class));
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initUI();
		initEvent();
	}
	
	private void initUI() {
		ready();
		createUI();
		
	}
	
	private void createUI() {
		RelativeLayout layout = new RelativeLayout(this);
		StyleSettingTool.setBackground(layout, setting);
		createTitle(layout);
		createContent(layout);
		setContentView(layout);
	}
	
	private void createContent(RelativeLayout layout) {
		createListView();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.BELOW, title.getId());
		StyleSettingTool.setMargin(setting.listMargin(), params);
		layout.addView(listView, params);
	}
	
	private void createTitle(RelativeLayout layout) {
		title = new TextView(this);
		title.setId(id++);
		title.setText("新广告");
		StyleSettingTool.setTextStyle(title, setting.title());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.title().margin(), params);
		layout.addView(title, params);
	}
	
	private void createListView() {
		listView = new ListView(this);
		listView.setVerticalScrollBarEnabled(false);
		listView.setSmoothScrollbarEnabled(true);
		listViewAdapter = createAdapter();
		listView.setAdapter(listViewAdapter);
		StyleSettingTool.setListViewStyle(listView, setting.adListDivider());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AdvertisementAdapter adapter = (AdvertisementAdapter) parent.getAdapter();
				selectedAd = (Advertisement) adapter.getItem(position);
				adView = (AdListItemRelativeView) view;
				openContextMenu(listView);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AdvertisementAdapter adapter = (AdvertisementAdapter) parent.getAdapter();
				selectedAd = (Advertisement) adapter.getItem(position);
				adView = (AdListItemRelativeView) view;
				openContextMenu(listView);
				return true;
			}
		});
		listView.setOnCreateContextMenuListener(this);
	}
	
	private void initEvent() {
		registerNewAdReceiver();
		registerDownloadRecevier();
	}
	
	private void registerNewAdReceiver() {
		newAdReceiver = new NewAdReceiverAtRemote();
		IntentFilter filter = new IntentFilter(AppUtil.RECEIVER_NEW_ADVERTISEMENT_MSG);
		registerReceiver(newAdReceiver, filter);
	}
	
	private void registerDownloadRecevier() {
		remoteReceiver = new AdDownloadCompletedReceiverAtRemote();
		IntentFilter filter = new IntentFilter(AppUtil.RECEIVER_DOWNLOAD_COMPLETE);
		registerReceiver(remoteReceiver, filter);
	}
	
	private void unregisterRemoteReceiver() {
		if(newAdReceiver != null) {
			unregisterReceiver(newAdReceiver);
		}
		if(remoteReceiver != null) {
			unregisterReceiver(remoteReceiver);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("请选择操作:" + selectedAd.getName());
		if(DownloadServiceManager.instance().getService(selectedAd.getId()) == null) {
			if(selectedAd.isCancelDownload()) {
				menu.add(ContextMenu.NONE, ContextMenu.FIRST + 0, 0, "继续下载");
			} else {
				menu.add(ContextMenu.NONE, ContextMenu.FIRST + 0, 0, "下载");
			}
		} else {
			menu.add(ContextMenu.NONE, ContextMenu.FIRST + 1, 1, "取消下载");
		}
		menu.add(ContextMenu.NONE, ContextMenu.FIRST + 2, 2, "删除");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 0: {
			if(!SimpleMadApp.instance().isInNetwork()) {
				Toast.makeText(this, "请打开网络继续下载", Toast.LENGTH_SHORT).show();
				return true;
			}
			if(selectedAd.isCancelDownload()) {
				selectedAd.setCancelDownload(false);
				adService.update(selectedAd);
				Toast.makeText(RemoteAdListActivity.this, "继续下载:" + selectedAd.getName(), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(RemoteAdListActivity.this, "开始下载:" + selectedAd.getName(), Toast.LENGTH_SHORT).show();
			}
			selectedAd.setDownloading(true);
			DownloadService ds = new DownloadServiceImpl();
			ds.setAdListItemRelativeView(adView);
			ds.download(selectedAd);
			DownloadServiceManager.instance().addService(selectedAd.getId(), ds);
			break;
		}
		case Menu.FIRST + 1: {
			DownloadServiceManager.instance().cancelService(selectedAd.getId());
			Toast.makeText(RemoteAdListActivity.this, "已取消下载:" + selectedAd.getName(), Toast.LENGTH_SHORT).show();
			refresh();
			break;
		}
		case Menu.FIRST + 2: {
			DownloadServiceManager.instance().cancelService(selectedAd.getId());
			boolean isDeleted = adService.delete(selectedAd.getId());
			if(isDeleted) {
				Toast.makeText(RemoteAdListActivity.this, "删除成功:" + selectedAd.getName(), Toast.LENGTH_SHORT).show();
				refresh();
			} else {
				Toast.makeText(RemoteAdListActivity.this,  selectedAd.getName() + "不存在", Toast.LENGTH_SHORT).show();
				refresh();
			}
			break;
		}
		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		System.out.println("RemoteAd destroy...");
		super.onDestroy();
//		stopService(new Intent(this, ServerNotificationService.class));
//		stopService(new Intent(this, DownloadService.class));
//		stopService(new Intent(this, AppDisplayService.class));
		unregisterRemoteReceiver();
	}
	
	@Override
	protected void onResume() {
		refresh();
		isActive = true;
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isActive = false;
	}
	
	protected void refresh() {
		ready();
		listViewAdapter.notifyDataSetChanged();
	}
	
	private BaseAdapter createAdapter() {
		return new AdvertisementAdapter(this, data, setting.adListItemSetting());
	}
	
	private void ready() {
		System.out.println("RemoteAd ready...");
		setting = AppSetting.adListSetting();
		adService = AdvertisementServiceImpl.instance();
		if(!SimpleMadApp.DEBUG_MODE) {
			if(data == null) {
				data = new ArrayList<Advertisement>();
			}
			data.clear();
			data.addAll(adService.findRemoteAd());
			System.out.println("RemoteAD data size:" + data.size());
		} else {
			data = new ArrayList<Advertisement>();
			data.add(createTestData("Test111"));
			data.add(createTestData("Test222"));
			data.add(createTestData("Test333"));
			data.add(createTestData("Test444"));
			data.add(createTestData("Test555"));
			data.add(createTestData("Test666"));
			data.add(createTestData("Test777"));
			data.add(createTestData("Test888"));
			data.add(createTestData("Test999"));
			data.add(createTestData("Test1010"));
			data.add(createTestData("Test1111"));
		}
	}
	private int id = 1;
	private Advertisement createTestData(String desc) {
		Advertisement ad = new Advertisement();
		ad.setId(String.valueOf(id++));
		ad.setName(desc);
		ad.setAdType(AdvertisementType.IMAGE);
		ad.setStartDate(new Date());
		ad.setEndDate(DateUtil.addDay(ad.getStartDate(), 3));
		ad.setFile(null);
		ad.setPreviewFile(null);
		ad.setPrice(50);
		ad.setTimes(0);
		ad.setWaitingTime(5);
		ad.setSubmited(false);
		return ad;
	}
	
	protected void beginPlayingAd(Advertisement advertisement) {
		Intent playActivity = new Intent(this, AdvertisementPlayActivity.class);
		playActivity.putExtra(AppUtil.EXTRA_NAME_AD, advertisement);
		startActivityForResult(playActivity, RequestAndResultCode.RequestCode.REQUEST_ADVERTISEMENT_PLAY);
	}
	
	class AdDownloadCompletedReceiverAtRemote extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("ad download result received...:" + intent.getAction());
			if(AppUtil.RECEIVER_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
				if(intent.getBooleanExtra(AppUtil.EXTRA_NAME_DOWNLOAD_AD_RESULT, false)) {
					refresh();
					
					if(!isActive) {
						return;
					}
					final Advertisement ad = (Advertisement) intent.getSerializableExtra(AppUtil.EXTRA_NAME_AD);
					if(ad == null) {
						return;
					}
					ConfirmDialog cd = new ConfirmDialog(RemoteAdListActivity.this, "广告(" + ad.getName() + ")已下载完毕,是否播放?") {
						
						@Override
						public void ok() {
							beginPlayingAd(ad);
						}
					};
					cd.show();
				} else {
					Toast.makeText(RemoteAdListActivity.this, "下载广告(" + selectedAd.getName() + ")失败,请检查网络连接!", Toast.LENGTH_SHORT);
				}
			} 
		}
		
	}
	
	class NewAdReceiverAtRemote extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("receive new ad...:" + intent.getAction());
			if(AppUtil.RECEIVER_NEW_ADVERTISEMENT_MSG.equals(intent.getAction())) {
				refresh();
			}
		}
		
	}
}
