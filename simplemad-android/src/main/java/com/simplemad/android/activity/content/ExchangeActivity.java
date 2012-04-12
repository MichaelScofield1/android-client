package com.simplemad.android.activity.content;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simplemad.android.activity.NoTitleActivity;
import com.simplemad.android.activity.RequestAndResultCode;
import com.simplemad.android.adapter.AverageTextViewAdapter;
import com.simplemad.android.service.UserService;
import com.simplemad.android.service.UserServiceImpl;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.AverageTextViewListSetting;
import com.simplemad.android.setting.ExchangeSetting;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.BackgroundProgressDialog;
import com.simplemad.android.view.ConfirmDialog;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;
import com.simplemad.bean.MoneyExchangeRecord;
import com.simplemad.bean.User;

public class ExchangeActivity extends NoTitleActivity {

	private UserService userService;
	private User user;
	private List<List<String>> data;
	private ExchangeSetting setting;
	
	private int id = 1;
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initUI();
	};
	
	private void initUI() {
		BackgroundProgressDialog dialog = new BackgroundProgressDialog(this, "正在查询,请稍候...") {
			
			@Override
			protected void work() throws Exception {
				ready();
			}
			
			@Override
			protected void complete() throws Exception {
				createUI();
			}
		};
		dialog.show();
	}
	
	private void ready() {
		userService = UserServiceImpl.instance();
		user = userService.find();
		setting = AppSetting.exchangeSetting();
		data = createData();
	}
	
	private List<List<String>> createData() {
		List<List<String>> data = new ArrayList<List<String>>();
		
		List<String> dataList = new ArrayList<String>();
		dataList.add("申请时间");
		dataList.add("状态");
		dataList.add("项目");
		data.add(dataList);
		
		for(MoneyExchangeRecord record : userService.findRecords()) {
			dataList = new ArrayList<String>();
			dataList.add(record.getAppliedDate());
			dataList.add(record.getStatus());
			dataList.add(record.getItem());
			data.add(dataList);
		}
		return data;
	}
	
	private void createUI() {
		RelativeLayout layout = new RelativeLayout(this);
		StyleSettingTool.setBackground(layout, setting);
		createTitle(layout);
		createContent(layout);
		setContentView(layout);
	}
	
	private void createContent(RelativeLayout layout) {
		LinearLayout content = new LinearLayout(this);
		content.setOrientation(LinearLayout.VERTICAL);
		createContent(content);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.BELOW, title.getId());
		StyleSettingTool.setMargin(setting.margin(), params);
		layout.addView(content, params);
	}
	
	private void createContent(LinearLayout layout) {
		createExchangeLabel(layout);
		createExchange(layout);
		createRecordLabel(layout);
		createRecordList(layout);
		createPhone(layout);
	}
	
	private void createExchangeLabel(LinearLayout layout) {
		TextView label = new TextView(this);
		label.setText("你当前积分是" + user.getMoney() + "分,你可以:");
		TextViewSetting labelSetting = setting.exchangeDesc();
		StyleSettingTool.setTextStyle(label, labelSetting);
		layout.addView(label, itemParams(labelSetting.margin(), false));
	}
	
	private void createExchange(LinearLayout layout) {
		TextViewSetting btnSetting = setting.exchange();
		layout.addView(createExchangeButton(btnSetting), itemParams(btnSetting.margin(), false));
	}
	
	private void createRecordLabel(LinearLayout layout) {
		TextView label = new TextView(this);
		label.setText("兑换记录");
		TextViewSetting labelSetting = setting.recordLable();
		StyleSettingTool.setTextStyle(label, labelSetting);
		layout.addView(label, itemParams(labelSetting.margin(), false));
	}
	
	private void createRecordList(LinearLayout layout) {
		AverageTextViewListSetting listSetting = setting.list();
		layout.addView(createRecordList(listSetting), itemParams(null, true));
	}
	
	private View createRecordList(AverageTextViewListSetting listSetting) {
		ListView lv = new ListView(this);
		StyleSettingTool.setBackground(lv, listSetting);
		if(listSetting.divider() != null) {
			lv.setDividerHeight(listSetting.divider().getHeight());
		}
		lv.setAdapter(new AverageTextViewAdapter(this, listSetting.text(), data));
		return lv;
	}
	
	
	private void createPhone(LinearLayout layout) {
		TextView label = new TextView(this);
		label.setText("客服热线: 13560134790");
		TextViewSetting labelSetting = setting.phone();
		StyleSettingTool.setTextStyle(label, labelSetting);
		layout.addView(label, itemParams(labelSetting.margin(), false));
	}
	
	private LinearLayout.LayoutParams itemParams(Margin margin, boolean isFull) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(margin, params);
		if(isFull) {
			params.weight = 1;
		}
		return params;
	}
	
	private void createTitle(RelativeLayout layout) {
		title = new TextView(this);
		title.setId(id++);
		title.setText("积分兑换");
		StyleSettingTool.setTextStyle(title, setting.title());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.title().margin(), params);
		layout.addView(title, params);
	}
	
	private View createExchangeButton(TextViewSetting btnSetting) {
		Button btn = new Button(this);
		StyleSettingTool.setTextStyle(btn, btnSetting);
		btn.setText("立即兑换50元话费");
		if(user.getMobile() < UserService.EXCHANGABLE_MONEY) {
			btn.setEnabled(false);
		} else {
			btn.setEnabled(true);
		}
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				exchangeWithConfirm();
			}
		});
		return btn;
	}
	
	protected void exchangeWithConfirm() {
		ConfirmDialog cd = new ConfirmDialog(this, "请问你是否用" + UserService.EXCHANGABLE_MONEY + "积分兑换" + UserService.EXCHANGABLE_MONEY_NUM + "元话费?") {
			
			@Override
			public void ok() {
				exchange();
			}
		};
		cd.show();
	}
	
	protected void exchange() {
		BackgroundProgressDialog dialog = new BackgroundProgressDialog(this, "正在申请积分兑换,请稍候...") {
			private boolean isSuccess;
			
			@Override
			protected void work() throws Exception {
				isSuccess = userService.exchange();
			}
			
			@Override
			protected void complete() throws Exception {
				if(isSuccess) {
					Toast.makeText(getContext(), "积分兑换申请成功,系统将在7天内向你的手机卡中充值,请注意查收", Toast.LENGTH_LONG);
					setResult(RequestAndResultCode.ResultCode.RESULT_USER_EXCHANGE_MONEY_OK);
					finish();
				} else {
					Toast.makeText(getContext(), "积分兑换申请失败,请稍候重试!", Toast.LENGTH_LONG);
				}
			}
		};
		dialog.show();
	}
}
