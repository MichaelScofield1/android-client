package com.simplemad.android.activity.content;

import java.io.IOException;
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
import com.simplemad.android.activity.share.sina.Authorizer;
import com.simplemad.android.adapter.AdvertisementAdapter;
import com.simplemad.android.client.AdEffectEntityUtil;
import com.simplemad.android.service.AdvertisementService;
import com.simplemad.android.service.AdvertisementServiceImpl;
import com.simplemad.android.setting.AdListSetting;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.util.AdUtil;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.DateUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.ConfirmDialog;
import com.simplemad.bean.AdEffectEntity;
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.AdvertisementType;

public class LocalAdListActivity extends NoTitleActivity {

	private AdvertisementService adService;
	private List<Advertisement> data;
	private BaseAdapter listViewAdapter;
	private AdDownloadCompletedReceiverAtLocal localReceiver;
	
	protected Advertisement selectedAd;
	
	protected ListView listView;
	private TextView title;
	private int id = 1;
	
	private AdListSetting setting;
	
	private boolean isActive;
	
//	@Override
//	public void onBackPressed() {
//		AppUtil.homePresse(this);
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initUI();
		initEvent();
	}
	
	private void initEvent() {
		localReceiver = new AdDownloadCompletedReceiverAtLocal();
		IntentFilter filter = new IntentFilter(AppUtil.RECEIVER_DOWNLOAD_COMPLETE);
		registerReceiver(localReceiver, filter);
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
		title.setText("已下载广告");
		StyleSettingTool.setTextStyle(title, setting.title());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.title().margin(), params);
		layout.addView(title, params);
	}
	
	private void createListView() {
		listView = new ListView(this);
		listView.setId(id++);
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
				openContextMenu(listView);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AdvertisementAdapter adapter = (AdvertisementAdapter) parent.getAdapter();
				selectedAd = (Advertisement) adapter.getItem(position);
				openContextMenu(listView);
				return true;
			}
		});
		listView.setOnCreateContextMenuListener(this);
	}
	
	@Override
	protected void onResume() {
		refresh();
		isActive = true;
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isActive = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(localReceiver);
		super.onDestroy();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("请选择操作:" + selectedAd.getName());
		
		menu.add(ContextMenu.NONE, ContextMenu.FIRST + 1, 1, "播放");
		if(AdUtil.canShareFee(selectedAd)) {
			menu.add(ContextMenu.NONE, ContextMenu.FIRST + 2, 2, "分享新浪微博(" + selectedAd.getSharingPrice() + "分)");
			menu.add(ContextMenu.NONE, ContextMenu.FIRST + 3, 3, "分享腾讯微博(" + selectedAd.getSharingPrice() + "分)");
		} else {
			menu.add(ContextMenu.NONE, ContextMenu.FIRST + 2, 2, "分享新浪微博");
			menu.add(ContextMenu.NONE, ContextMenu.FIRST + 3, 3, "分享腾讯微博");
		}
		menu.add(ContextMenu.NONE, ContextMenu.FIRST + 4, 4, "删除");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1: {
			if(AdUtil.needNetWork(selectedAd) && !SimpleMadApp.instance().isInNetwork()) {
				Toast.makeText(this, "请打开网络观看广告", Toast.LENGTH_SHORT).show();
				return true;
			}
			beginPlayingAd(selectedAd);
			break;
		}
		case Menu.FIRST + 2: {
			Authorizer authorizer = new Authorizer(this);
			authorizer.authorize();
			break;
		}
		case Menu.FIRST + 3: {
			Toast.makeText(this, "暂未实现腾讯微博...", Toast.LENGTH_SHORT).show();
			break;
		}
		case Menu.FIRST + 4: {
			boolean isDeleted = adService.delete(selectedAd.getId());
			if(isDeleted) {
				Toast.makeText(LocalAdListActivity.this, "删除成功:" + selectedAd.getName(), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(LocalAdListActivity.this,  selectedAd.getName() + "不存在", Toast.LENGTH_SHORT).show();
				refresh();
			}
			break;
		}
		default:
			break;
		}
		return true;
	}
	
	protected void refresh() {
		ready();
		listViewAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("LocalAdListActivity result : " + resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == RequestAndResultCode.RequestCode.REQUEST_ADVERTISEMENT_PLAY) {
			if(resultCode == RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK) {
				refresh();
			}
		}
		if(requestCode == RequestAndResultCode.RequestCode.REQUEST_SHARE) {
			if(resultCode == RESULT_OK) {
				if(AdUtil.canShareFee(selectedAd)) {
					try {
						double money = adService.earnSharingMoney(selectedAd.getId());
						Toast.makeText(this, "恭喜获得" + money + "分", Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(this, "获取分享金额失败,请稍候重试", Toast.LENGTH_LONG).show();
						setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
						finish();
					}
				} else {
					AdEffectEntityUtil.send(selectedAd, AdEffectEntity.KEY_AD_SHARED);
				}
				refresh();
			}
		}
	}
	
	protected void beginPlayingAd(Advertisement advertisement) {
		Intent playActivity = new Intent(this, AdvertisementPlayActivity.class);
		playActivity.putExtra(AppUtil.EXTRA_NAME_AD, advertisement);
		startActivityForResult(playActivity, RequestAndResultCode.RequestCode.REQUEST_ADVERTISEMENT_PLAY);
	}
	
	private BaseAdapter createAdapter() {
		return new AdvertisementAdapter(this, data, setting.adListItemSetting());
	}
	
	private void ready() {
		System.out.println("LocalAd ready...");
		setting = AppSetting.adListSetting();
		adService = AdvertisementServiceImpl.instance();
		if(SimpleMadApp.DEBUG_MODE) {
			if(data == null) {
				data = new ArrayList<Advertisement>();
			}
			data.clear();
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
		} else {
			if(data == null) {
				data = new ArrayList<Advertisement>();
			}
			data.clear();
			data.addAll(adService.findLocalAd());
			System.out.println("LocalAD data size:" + data.size());
		}
	}
	
	private Advertisement createTestData(String desc) {
		Advertisement ad = new Advertisement();
		ad.setName(desc);
		ad.setAdType(AdvertisementType.TEXT);
		ad.setStartDate(new Date());
		ad.setEndDate(DateUtil.addMinute(ad.getStartDate(), 2));
		ad.setFile(null);
		ad.setPreviewFile(null);
		ad.setPrice(50);
		ad.setTimes(0);
		ad.setWaitingTime(5);
		ad.setSubmited(false);
		return ad;
	}
	
	class AdDownloadCompletedReceiverAtLocal extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("Local Receiver...:" + intent.getAction());
			if(AppUtil.RECEIVER_DOWNLOAD_COMPLETE.equals(intent.getAction()) && intent.getBooleanExtra(AppUtil.EXTRA_NAME_DOWNLOAD_AD_RESULT, false)) {
				refresh();
				
				if(!isActive) {
					return;
				}
				final Advertisement ad = (Advertisement) intent.getSerializableExtra(AppUtil.EXTRA_NAME_AD);
				if(ad == null) {
					return;
				}
				ConfirmDialog cd = new ConfirmDialog(LocalAdListActivity.this, "广告(" + ad.getName() + ")已下载完毕,是否播放?") {
					
					@Override
					public void ok() {
						beginPlayingAd(ad);
					}
				};
				cd.show();
			}
		}
		
	}
}
