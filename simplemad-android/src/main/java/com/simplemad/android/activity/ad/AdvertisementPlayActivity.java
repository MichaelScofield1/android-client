package com.simplemad.android.activity.ad;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.activity.NoTitleActivity;
import com.simplemad.android.activity.RequestAndResultCode;
import com.simplemad.android.activity.share.sina.Authorizer;
import com.simplemad.android.client.AdEffectEntityUtil;
import com.simplemad.android.service.AdvertisementService;
import com.simplemad.android.service.AdvertisementServiceImpl;
import com.simplemad.android.service.FileService;
import com.simplemad.android.service.FileServiceImpl;
import com.simplemad.android.setting.AdvertisementPlaySetting;
import com.simplemad.android.setting.base.BaseAdvertisementPlaySetting;
import com.simplemad.android.util.AdUtil;
import com.simplemad.android.util.AppFileHelper;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.DialogUtil;
import com.simplemad.android.util.StringUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.util.VibratorUtil;
import com.simplemad.android.util.WebViewHelper;
import com.simplemad.android.view.BackgroundProgressDialog;
import com.simplemad.bean.AdEffectEntity;
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.AdvertisementType;
import com.simplemad.parameter.ClientParameter;

public class AdvertisementPlayActivity extends NoTitleActivity {
	
//	private static final int DISPLAY = 1;
	private static final int WEIBO_DIALOG_ID = 1;
	private static final long COUNT_DOWN_INTERVAL = 1 * 1000;
	
//	private Handler handler;
	private CountDownTimer timer;
	
	private Advertisement advertisement;
	
	private AdvertisementService adService;
	private FileService fileService;
	
	private View finishView;
	private TextView timerView;
	
	private AdvertisementPlaySetting setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("AdvertisementPlayActivity ...");
		ready();
		Serializable serializable = getIntent().getSerializableExtra(AppUtil.EXTRA_NAME_AD);
		if(serializable instanceof Advertisement) {
			advertisement = (Advertisement) serializable;
			adService.playOnce(advertisement.getId());
			AdEffectEntityUtil.send(advertisement, AdEffectEntity.KEY_AD_OPENED);
		} else {
			finish();
			return;
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setOrientation();
		setContentView(createAdvertisementView());
		hideFinishView();/*创建view后隐藏finishView,等事件触发其再次显示*/
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		CharSequence[] items = new CharSequence[]{"分享新浪微博", "分享腾讯微博"};
		AlertDialog.Builder builder = new AlertDialog.Builder(AdvertisementPlayActivity.this);
		builder.setTitle("分享到微博:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: {
					Authorizer authorizer = new Authorizer(AdvertisementPlayActivity.this);
					authorizer.authorize();
					break;
				}
				case 1: {
					Toast.makeText(AdvertisementPlayActivity.this, "暂未实现腾讯微博...", Toast.LENGTH_SHORT).show();
					break;
				}
				default:
					break;
				}
				dialog.dismiss();
			}
		});
		return builder.create();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RequestAndResultCode.RequestCode.REQUEST_SHARE) {
			if(resultCode == RESULT_OK) {
				if(AdUtil.canShareFee(advertisement)) {
					BackgroundProgressDialog dialog = new BackgroundProgressDialog(this, "正在获取分享金额，请稍侯...") {
						double money;
						
						@Override
						protected void work() throws Exception {
							try {
								money = adService.earnSharingMoney(advertisement.getId());
							} catch (IOException e) {
								e.printStackTrace();
								throw new Exception("获取分享金额失败,请稍候重试");
							}
						}

						@Override
						protected void complete() throws Exception {
							advertisement = adService.find(advertisement.getId());
							showFinishView();
							Toast.makeText(AdvertisementPlayActivity.this, "恭喜获得" + money + "分", Toast.LENGTH_LONG).show();
						}
						
					};
					dialog.show();
					
				} else {
					AdEffectEntityUtil.send(advertisement, AdEffectEntity.KEY_AD_SHARED);
				}
				if(AdUtil.canNotSubmit(advertisement)) {
					setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
					finish();
				}
			}
		}
	}
	
	private void ready() {
		setting = new BaseAdvertisementPlaySetting();
		adService = AdvertisementServiceImpl.instance();
		fileService = FileServiceImpl.instance();
	}
	
	private void setOrientation() {
		int requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
		if(advertisement.getAdType() == AdvertisementType.TEXT) {
			requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		} else if(advertisement.getAdType() == AdvertisementType.IMAGE) {
			BitmapDrawable bm = new BitmapDrawable(fileService.getFilePath(advertisement));
			if(bm.getBitmap().getWidth() > bm.getBitmap().getHeight()) {
				requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			} else {
				requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			}
			bm.getBitmap().recycle();
		} else if(advertisement.getAdType() == AdvertisementType.VIDEO) {
			requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		} else if(advertisement.getAdType() == AdvertisementType.HTML) {
			requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		} else if(advertisement.getAdType() == AdvertisementType.INTERACTION) {
			requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
		setRequestedOrientation(requestedOrientation);
	}
	
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(advertisement.getAdType() == AdvertisementType.TEXT 
				|| advertisement.getAdType() == AdvertisementType.IMAGE) {
			pause();
		}
	}
	
	@Override
	protected void onDestroy() {
//		if(handler != null) {
//			handler.removeMessages(DISPLAY);
//		}
		if(timer != null) {
			timer.cancel();
		}
		if(pw != null && pw.isShowing()) {
			pw.dismiss();
		}
		if(vv != null) {
//			vv.stopPlayback();
		}
		super.onDestroy();
	}
	
	private PopupWindow pw;
	
	protected void showFinishView() {
		Display display = getWindowManager().getDefaultDisplay();
		createTool();
		if(pw != null) {
			pw.dismiss();
		}
		pw = new PopupWindow(finishView, 400, 100);
		pw.showAsDropDown(getWindow().getDecorView(), -display.getWidth(), -100);
		
//		finishView.bringToFront();
//		setVisibleForFinishView(View.VISIBLE);
	}
	
	protected void hideFinishView() {
		if(pw != null) {
			pw.dismiss();
		}
//		setVisibleForFinishView(View.INVISIBLE);
	}
	
//	private void setVisibleForFinishView(int visibility) {
//		if(finishView == null)
//			return;
//		finishView.setVisibility(visibility);
//	}
	
	protected void pause() {
//		if(handler == null)
//			handler = new Handler() {
//				@Override
//				public void handleMessage(Message msg) {
//					if(msg.what == DISPLAY) {
//						
//						removeMessages(DISPLAY);
//					}
//				}
//			};
//		handler.sendEmptyMessageDelayed(DISPLAY, advertisement.getWaitingTime() * 1000);
		startCountDownTimer();
	}
	
	private void createTimerView() {
		timerView = new TextView(this);
		StyleSettingTool.setTextStyle(timerView, setting.timer());
		timerView.setText(advertisement.getWaitingTime() + "秒");
		timerView.setTag(advertisement.getWaitingTime());
		timerView.bringToFront();
	}
	
	private void minusTimerView() {
		if(timerView == null) {
			return;
		}
		int timeToLeft = ((Integer)timerView.getTag()) - 1;
		timerView.setText(timeToLeft + "秒");
		timerView.setTag(timeToLeft);
	}
	
	private void hideTimerView() {
		if(timerView == null) {
			return;
		}
		timerView.setVisibility(View.INVISIBLE);
	}
	
	private void startCountDownTimer() {
		long millisInFuture = advertisement.getWaitingTime() * 1000;
		timer = new CountDownTimer(millisInFuture, COUNT_DOWN_INTERVAL) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				minusTimerView();
			}
			
			@Override
			public void onFinish() {
				showFinishView();
				hideTimerView();
			}
		};
		timer.start();
	}
	
	private View createAdvertisementView() {
		if(advertisement.getAdType() == AdvertisementType.TEXT) {
			return createTextAdvertisement();
		} else if(advertisement.getAdType() == AdvertisementType.IMAGE) {
			return createImageAdvertisement();
		} else if(advertisement.getAdType() == AdvertisementType.VIDEO) {
			return createVideoAdvertisement();
		} else if(advertisement.getAdType() == AdvertisementType.HTML) {
			return createHtmlAdvertisement();
		} else if(advertisement.getAdType() == AdvertisementType.INTERACTION) {
			return createInteractionAdvertisement();
		} else {
			return createUnsupportedAdvertisement();
		}
	}
	
	private View createHtmlAdvertisement() {
		WebView wv = WebViewHelper.getSystemWebView(this);
		wv.loadUrl(advertisement.getUrl());
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if(SimpleMadApp.instance().isInNetwork()) {
					pause();
				} else {
					Toast.makeText(AdvertisementPlayActivity.this, "该广告需要连接网络,请打开网络连接!", Toast.LENGTH_LONG);
				}
			}
			
		});
		return createContent(wv);
	}
	
	private View createInteractionAdvertisement() {
		WebView wv = WebViewHelper.getSystemWebView(this);
		wv.loadUrl(advertisement.getUrl());
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if(!SimpleMadApp.instance().isInNetwork()) {
					Toast.makeText(AdvertisementPlayActivity.this, "该广告需要连接网络,请打开网络连接!", Toast.LENGTH_LONG);
					return;
				}
				if(url.startsWith(ClientParameter.COMPLETION)) {
					showFinishView();
				}
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}
		});
		return createContent(wv);
	}
	
	private View createTextAdvertisement() {
		if(StringUtil.isEmpty(advertisement.getFile())) {
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			StyleSettingTool.setBackground(layout, setting.text());
			createTextAdvertisementTitle(layout);
			
			return createContent(layout);
		} else if("txt".equals(advertisement.getFileExtendedName())) {
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			StyleSettingTool.setBackground(layout, setting.text());
			createTextAdvertisementTitle(layout);
			createTextAdvertisementContent(layout);
			return createContent(layout);
		} else {
			WebView wv = WebViewHelper.getSystemWebView(this);
			wv.loadUrl(AppFileHelper.translateFileUrl(fileService.getFilePath(advertisement)));
			return createContent(wv);
		}
	}

	private void createTextAdvertisementTitle(LinearLayout layout) {
		TextView title = new TextView(this);
		StyleSettingTool.setTextStyle(title, setting.text().title());
		title.setText(advertisement.getName());
		title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
		title.getPaint().setFakeBoldText(true);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.text().title().margin(), params);
		layout.addView(title, params);
	}
	
	private void createTextAdvertisementContent(LinearLayout layout) {
		TextView content = new TextView(this);
		StyleSettingTool.setTextStyle(content, setting.text().content());
		
		File file = fileService.getFile(advertisement);
		if(file != null && file.exists()) {
			try {
				byte[] dataArray = AppFileHelper.readFile(file);
				String contentStr = new String(dataArray, "UTF-8");
				content.setText("     " + contentStr.trim());
			} catch (IOException e) {
				DialogUtil.sayError(AdvertisementPlayActivity.this, "读取文字广告文件失败!");
				return;
			}
		} else {
			return;
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.text().content().margin(), params);
		layout.addView(content, params);
	}
	
	private View createImageAdvertisement() {
		ImageView iv = new ImageView(this);
		iv.setAdjustViewBounds(true);
		iv.setScaleType(ScaleType.FIT_CENTER);
		File imageFile = fileService.getFile(advertisement);
		if(imageFile == null || !imageFile.exists())
			return createContent(null);
		iv.setImageURI(Uri.fromFile(imageFile));
		return createContent(iv);
	}
	
	private VideoView vv;
	private View createVideoAdvertisement() {
		vv = new VideoView(this) {
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				 Display display = AdvertisementPlayActivity.this.getWindowManager().getDefaultDisplay();
				 int width = getDefaultSize(display.getWidth(), widthMeasureSpec);
				 int height = getDefaultSize(display.getHeight(), heightMeasureSpec);
				 setMeasuredDimension(width, height);
			}
		};
		File videoFile = fileService.getFile(advertisement);
		if(videoFile == null || !videoFile.exists())
			return createContent(null);
		vv.setVideoURI(Uri.fromFile(videoFile));
		
		vv.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				showFinishView();
			}
		});
		
		vv.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				DialogUtil.sayError(AdvertisementPlayActivity.this, "视频播放错误");
				VibratorUtil.vibrator(AdvertisementPlayActivity.this, 100);
				return true;
			}
		});
		
		Handler h = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 99) {
					vv.start();
					removeMessages(99);
				}
			};
		};
		h.sendEmptyMessageDelayed(99, 100);
		
		return createContent(vv);
	}
	
	private View createContent(View mainView) {
		if(advertisement.getAdType().equals(AdvertisementType.INTERACTION) || advertisement.getAdType().equals(AdvertisementType.VIDEO)) {
			StyleSettingTool.setBackground(mainView, setting);
			return mainView;
		} else {
			createTimerView();
			RelativeLayout layout = new RelativeLayout(this);
			StyleSettingTool.setBackground(layout, setting);
			if(mainView == null)
				return layout;
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			params.alignWithParent = true;
			if(advertisement.getAdType().equals(AdvertisementType.TEXT) && "txt".equals(advertisement.getFileExtendedName())) {
				StyleSettingTool.setMargin(setting.text().margin(), params);
			}
			layout.addView(mainView, params);
			
			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP, layout.getId());
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, layout.getId());
			StyleSettingTool.setMargin(setting.timer().margin(), params);
			layout.addView(timerView, 0, params);
			
			layout.bringChildToFront(timerView);
			
			return layout;
		}
	}
	
	private View createUnsupportedAdvertisement() {
		return null;
	}
	
	protected void createTool() {
		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.earnAdMoney().margin(), params);
		TextView earnAdMoneyView = null;
		if(AdUtil.canNotSubmit(advertisement)) {
			earnAdMoneyView = createThankyouView();
		} else {
			earnAdMoneyView = createSubmitView();
		}
		layout.addView(earnAdMoneyView, params);
		
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.earnSharingMoney().margin(), params);
		TextView earnSharingMoneyView = null;
		if(AdUtil.canShareFee(advertisement)) {
			earnSharingMoneyView = createShareViewFee();
		} else {
			earnSharingMoneyView = createShareView();
		}
		layout.addView(earnSharingMoneyView, params);
		
		finishView = layout;
	}
	
	protected TextView createShareView() {
		Button button = new Button(this);
		button.setText("分享微博");
		StyleSettingTool.setTextStyle(button, setting.earnSharingMoney());
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SimpleMadApp.DEBUG_MODE) {
					Toast.makeText(AdvertisementPlayActivity.this, "恭喜获得" + advertisement.getPrice() + "分", Toast.LENGTH_LONG).show();
//					setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
//					finish();
				} else if(!SimpleMadApp.instance().isInNetwork()) {
					Toast.makeText(AdvertisementPlayActivity.this, "网络连接失败,请在网络环境分享微博", Toast.LENGTH_LONG).show();
//					setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
//					finish();
				} else {
					showDialog(WEIBO_DIALOG_ID);
//					openContextMenu(v);
				}
			}
		});
		registerForContextMenu(button);
		return button;
	}
	
	protected TextView createShareViewFee() {
		Button button = new Button(this);
		button.setText("分享微博(" + advertisement.getSharingPrice() + ")");
		StyleSettingTool.setTextStyle(button, setting.earnSharingMoney());
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SimpleMadApp.DEBUG_MODE) {
					Toast.makeText(AdvertisementPlayActivity.this, "恭喜获得" + advertisement.getPrice() + "分", Toast.LENGTH_LONG).show();
//					setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
//					finish();
				} else if(!SimpleMadApp.instance().isInNetwork()) {
					Toast.makeText(AdvertisementPlayActivity.this, "网络连接失败,请在网络环境分享微博", Toast.LENGTH_LONG).show();
//					setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
//					finish();
				} else {
					showDialog(WEIBO_DIALOG_ID);
				}
			}
		});
		return button;
	}
	
	protected TextView createSubmitView() {
		Button button = new Button(this);
		button.setText("获取金额");
		StyleSettingTool.setTextStyle(button, setting.earnAdMoney());
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SimpleMadApp.DEBUG_MODE) {
					Toast.makeText(AdvertisementPlayActivity.this, "恭喜获得" + advertisement.getPrice() + "分", Toast.LENGTH_LONG).show();
					setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
					finish();
				} else if(!SimpleMadApp.instance().isInNetwork()) {
					Toast.makeText(AdvertisementPlayActivity.this, "网络连接失败,请在网络环境中获取广告金额", Toast.LENGTH_LONG).show();
					setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
					finish();
				} else {
					BackgroundProgressDialog dialog = new BackgroundProgressDialog(v.getContext(), "正在获取金额，请稍侯...") {
						double money;
						@Override
						protected void work() throws Exception {
							try {
								money = adService.earnAdMoney(advertisement.getId());
							} catch (IOException e) {
								e.printStackTrace();
								throw new Exception("获取广告金额失败,请稍候重试");
							}
						}
						
						@Override
						protected void complete() throws Exception {
							advertisement = adService.find(advertisement.getId());
							Toast.makeText(AdvertisementPlayActivity.this, "恭喜获得" + money + "分", Toast.LENGTH_LONG).show();
							showFinishView();
						}
					};
					dialog.show();
				}
				
			}
		});
		return button;
	}
	
	protected TextView createThankyouView() {
		Button button = new Button(this);
		button.setText("返    回");
		StyleSettingTool.setTextStyle(button, setting.earnAdMoney());
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RequestAndResultCode.ResultCode.RESULT_ADVERTISEMENT_PLAY_OK);
				finish();
			}
		});
		return button;
	}
}
