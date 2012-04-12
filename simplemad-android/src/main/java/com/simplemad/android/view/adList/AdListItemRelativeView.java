package com.simplemad.android.view.adList;

import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplemad.android.R;
import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.service.FileService;
import com.simplemad.android.service.FileServiceImpl;
import com.simplemad.android.util.BitmapUtil;
import com.simplemad.android.util.DateUtil;
import com.simplemad.android.util.StringUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.bean.Advertisement;

public class AdListItemRelativeView extends RelativeLayout {

	private static final long COUNT_DOWN_INTERVAL = 60 * 1000;
	
	private CountDownTimer timer;
	
	private Advertisement advertisement;
	
	private int startId = 1;
	
	private TextView descView;
	private TextView adTypeView;
	private TextView leftTimeView;
	
	private ImageView imageView;
	private TextView moneyView;
	private TextView statusView;
	private ProgressBar progressBar;
	private TextView progressData;
	
	private boolean progressing;
	
	private FileService fs;
	
	private AdListItemRelativeViewSetting setting;
	
	public AdListItemRelativeView(Context context) {
		super(context);
		fs = FileServiceImpl.instance();
	}
	
	public AdListItemRelativeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AdListItemRelativeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setAdvertisement(Advertisement advertisement, AdListItemRelativeViewSetting setting) {
		this.advertisement = advertisement;
		this.setting = setting;
		if(this.advertisement == null) {
			return;
		} else {
			drawUI();
			startCountDownTimer();
		}
	}
	
	public boolean isProgressing() {
		return progressing;
	}
	
	public void setProcess(int max, int current) {
		if(progressBar.getVisibility() != VISIBLE) {
			progressBar.setVisibility(VISIBLE);
		}
		if(progressData.getVisibility() != VISIBLE) {
			progressData.setVisibility(VISIBLE);
		}
		progressBar.setMax(max);
		progressBar.setProgress(current);
		progressData.setText(getProgressData(max, current));
		if(current == max) {
			progressing = false;
			progressBar.setVisibility(GONE);
			progressData.setVisibility(GONE);
		} else {
			progressing = true;
		}
		updateStatusView();
		updateMoneyView();
	}
	
	private void startCountDownTimer() {
		if(DateUtil.isOutOfDate(advertisement.getEndDate())) {
			return;
		}
		if(timer != null) {
			return;
		}
		long millisInFuture = DateUtil.timeMinus(new Date(), advertisement.getEndDate());
		timer = new CountDownTimer(millisInFuture, COUNT_DOWN_INTERVAL) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				updateLeftTime(millisUntilFinished);
			}
			
			@Override
			public void onFinish() {
				updateLeftTime(0);
				updateStatusView();
				updateMoneyView();
			}
		};
		timer.start();
	}
	
	private String getProgressData(int max, int current) {
		return (current * 100 / max) + "%";
	}
	
	private void drawUI() {
		setViewStyle();
		createPreviewBitMap();
		createToolButtons();
		createDescription();
		createProgress();
	}
	
	private void setViewStyle() {
		StyleSettingTool.setBackground(this, setting);
	}
	
	private void createToolButtons() {
		createMoneyView();
		createStatusView();
	}
	
	private void createStatusView() {
		statusView = new TextView(getContext());
		if(setting != null) {
			StyleSettingTool.setTextStyle(statusView, setting.status());
		}
		statusView.setId(startId++);
		updateStatusView();
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, moneyView.getId());
		params.addRule(ALIGN_LEFT, moneyView.getId());
		params.addRule(ALIGN_RIGHT, moneyView.getId());
		addView(statusView, params);
	}
	
	protected void updateStatusView() {
		if(advertisement.isMessage()) {
			if(advertisement.isCancelDownload()) {
				statusView.setText("继续下载");
			} else if(advertisement.isDownloading()) {
				statusView.setText("正在下载");
			} else if(advertisement.isDownloaded()) {
				statusView.setText("下载完成");
			} else {
				statusView.setText("请下载");
			}
		} else if(DateUtil.isOutOfDate(advertisement.getEndDate())) {
			statusView.setText("已过期");
		} else if(advertisement.isSubmited()) {
			statusView.setText("已领取");
		} else {
			statusView.setText("未领取");
		}
	}
	
	private void updateMoneyView() {
		if(advertisement.isMessage()) {
			moneyView.setPressed(true);
		} else if(DateUtil.isOutOfDate(advertisement.getEndDate())) {
			moneyView.setEnabled(false);
		} else if(advertisement.isSubmited()) {
			moneyView.setEnabled(false);
		} else {
			moneyView.setEnabled(true);
		}
	}

	private void createMoneyView() {
		moneyView = new TextView(getContext());
		StyleSettingTool.setTextStyle(moneyView, setting.money());
		
		moneyView.setId(startId++);
		moneyView.setText(String.valueOf(advertisement.getPrice()) + "分");
		updateMoneyView();		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(ALIGN_PARENT_TOP, getId());
		params.addRule(ALIGN_PARENT_RIGHT, getId());
//		params.addRule(CENTER_VERTICAL, getId());
		StyleSettingTool.setMargin(setting.money().margin(), params);
		addView(moneyView, params);
	}

	private void createDescription() {
		LayoutParams params = textParams();
		/*ad description*/
		descView = new TextView(getContext());
		descView.setId(startId++);
		StyleSettingTool.setTextStyle(descView, setting.name());
		descView.setText(advertisement.getName());
		params.addRule(CENTER_HORIZONTAL);
		params.addRule(ALIGN_PARENT_TOP, getId());
		params.addRule(RIGHT_OF, imageView.getId());
		StyleSettingTool.setMargin(setting.name().margin(), params);
		addView(descView, params);
		
		params = textParams();
		adTypeView = new TextView(getContext());
		StyleSettingTool.setTextStyle(adTypeView, setting.adType());
		adTypeView.setId(startId++);
		adTypeView.setText("类型:" + advertisement.getAdType().getChineseName());
		params.addRule(BELOW, descView.getId());
		params.addRule(ALIGN_LEFT, descView.getId());
		params.addRule(ALIGN_PARENT_RIGHT, descView.getId());
		StyleSettingTool.setMargin(setting.adType().margin(), params);
		addView(adTypeView, params);
		
		params = textParams();
		leftTimeView = new TextView(getContext());
		leftTimeView.setId(startId++);
		StyleSettingTool.setTextStyle(leftTimeView, setting.leftTime());
		updateLeftTime(DateUtil.timeMinus(new Date(), advertisement.getEndDate()));
		params.addRule(BELOW, adTypeView.getId());
		params.addRule(ALIGN_LEFT, adTypeView.getId());
		params.addRule(ALIGN_PARENT_RIGHT, adTypeView.getId());
		StyleSettingTool.setMargin(setting.leftTime().margin(), params);
		addView(leftTimeView, params);
		
		if(setting != null) {
			StyleSettingTool.setTextStyle(descView, setting.name());
			StyleSettingTool.setTextStyle(adTypeView, setting.adType());
			StyleSettingTool.setTextStyle(leftTimeView, setting.adType());
		}
	}
	
	protected void updateLeftTime(long leftTime) {
		if(leftTime < 0) {
			leftTimeView.setText("剩余时间: " + DateUtil.calculateLeftTimeString(0));
		} else {
			leftTimeView.setText("剩余时间: " + DateUtil.calculateLeftTimeString(leftTime));
		}
	}
	
	private void createProgress() {
		RelativeLayout layout = new RelativeLayout(getContext());
		
		progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setId(startId++);
		progressBar.setVisibility(GONE);
		layout.addView(progressBar, progressBarParams());
		
		progressData = new TextView(getContext());
		progressData.setId(startId++);
		progressData.setVisibility(GONE);
		layout.addView(progressData, progressDataParams());
		
		LayoutParams params =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RIGHT_OF, imageView.getId());
		params.addRule(BELOW, leftTimeView.getId());
		params.addRule(LEFT_OF, moneyView.getId());
		addView(layout, params);
	}
	
	private LayoutParams progressBarParams() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.processBarMargin(), params);
		return params;
	}
	
	private LayoutParams progressDataParams() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(CENTER_IN_PARENT, -1);
		return params;
	}
	
	private LayoutParams textParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	private void createPreviewBitMap() {
		Bitmap bitMap = null;
		imageView = new ImageView(getContext());
		imageView.setId(startId++);
		imageView.setAdjustViewBounds(true);
		imageView.setScaleType(ScaleType.FIT_XY);
//		imageView.setScaleType(ScaleType.CENTER_CROP);
		StyleSettingTool.setImageViewStyle(setting.image(), imageView);
		if(!StringUtil.isEmpty(advertisement.getPreviewFile()))
				System.out.println("Advertisement preview fiel path:" + fs.getPreviewFilePath(advertisement));
				bitMap = BitmapUtil.createPromptBitmap(fs.getPreviewFilePath(advertisement), 768, 100*100);
//				imageView.setImageURI(new Uri.Builder().path(fs.getPreviewFilePath(advertisement)).build());
		if(bitMap == null) {
			BitmapDrawable drawable = (BitmapDrawable) SimpleMadApp.instance().getResources().getDrawable(R.drawable.ic_launcher);
			bitMap = drawable.getBitmap();
		}
		
		imageView.setImageBitmap(bitMap);
		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.alignWithParent = true;
		params.addRule(CENTER_VERTICAL, getId());
		
		addView(imageView, params);
		
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if(timer != null) {
			timer.cancel();
		}
		super.onDetachedFromWindow();
	}

}
