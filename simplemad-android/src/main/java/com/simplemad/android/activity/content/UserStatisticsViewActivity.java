package com.simplemad.android.activity.content;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.activity.NoTitleActivity;
import com.simplemad.android.activity.RequestAndResultCode;
import com.simplemad.android.service.UserService;
import com.simplemad.android.service.UserServiceImpl;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.UserStatisticsSetting;
import com.simplemad.android.util.DateUtil;
import com.simplemad.android.util.StringUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.BackgroundProgressDialog;
import com.simplemad.android.view.LabelField;
import com.simplemad.bean.User;
import com.simplemad.bean.UserAttribute;

public class UserStatisticsViewActivity extends NoTitleActivity {

	private UserService userService;
	private User user;
	
	private UserStatisticsSetting setting;
	
	private int id = 1;
	
	private TextView title;
	private LinearLayout totalMoney;
	private LinearLayout detail;
	
	private boolean isCreated;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	};
	
	protected void refresh() {
		BackgroundProgressDialog dialog = new BackgroundProgressDialog(this, "初始化数据,请稍候...") {
			
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
	
	private void createUI() {
		RelativeLayout layout = new RelativeLayout(this);
		StyleSettingTool.setBackground(layout, setting);
		createTitle(layout);
		createTotalMoney(layout);
		createDetail(layout);
		setContentView(layout);
	}
	
	private void createTitle(RelativeLayout parent) {
		title = createTitle();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.title().margin(), params);
		parent.addView(title, params);
	}
	
	private TextView createTitle() {
		TextView title = new TextView(this);
		title.setId(id++);
		title.setText("账户管理");
		StyleSettingTool.setTextStyle(title, setting.title());
		return title;
	}
	
	private void createTotalMoney(RelativeLayout parent) {
		totalMoney = createTotalMoney();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, title.getId());
		StyleSettingTool.setMargin(setting.totalMoney().contentMargin(), params);
		parent.addView(totalMoney, params);
	}
	
	private void createDetail(RelativeLayout parent) {
		detail = createDetail();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, totalMoney.getId());
		StyleSettingTool.setMargin(setting.detail().contentMargin(), params);
		parent.addView(detail, params);
	}
	
	private LinearLayout createDetail() {
		LinearLayout layout = new LinearLayout(this);
		layout.setId(id++);
		layout.setOrientation(LinearLayout.VERTICAL);
		StyleSettingTool.setBackground(layout, setting.detail());
		createUserInfo(layout);
		createModifyButton(layout);
		createDetailInfo(layout);
		return layout;
	}
	
	private void createModifyButton(LinearLayout parent) {
		View btn = createModifyButton();
		parent.addView(btn, modifyButtonParams());
	}
	
	private void createDetailInfo(LinearLayout parent) {
		ScrollView sv = new ScrollView(this);
		sv.addView(createDetailInfo());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.detail().detailInfo().contentMargin(), params);
		parent.addView(sv, params);
	}
	
	private LinearLayout createDetailInfo() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		for(UserAttribute attr : user.getAttrs()) {
			LabelField item = new LabelField(this, setting.detail().detailInfo().labelField());
			item.setLabel(attr.getKey());
			item.setValue(StringUtil.isEmpty(attr.getValue()) ? "" : attr.getValue());
			layout.addView(item, detailItemParams());
		}
		return layout;
	}
	
	private void createUserInfo(LinearLayout parent) {
		RelativeLayout userInfo = createUserInfo();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.detail().userInfo().contentMargin(), params);
		parent.addView(userInfo, params);
	}
	
	private RelativeLayout createUserInfo() {
		RelativeLayout layout = new RelativeLayout(this);
		layout.setId(id++);
		StyleSettingTool.setBackground(layout, setting.detail().userInfo());
		
		ImageView image = new ImageView(this);
		image.setId(id++);
		image.setImageDrawable(setting.detail().userInfo().header().image());
		StyleSettingTool.setPadding(image, setting.detail().userInfo().header().padding());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		params.addRule(RelativeLayout.CENTER_VERTICAL, layout.getId());
		layout.addView(image, params);
		
		TextView name = new TextView(this);
		name.setId(id++);
		name.setText(StringUtil.isEmpty(user.getUserName()) ? String.valueOf(user.getMobile()) : user.getUserName());
		StyleSettingTool.setTextStyle(name, setting.detail().userInfo().name());
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, layout.getId());
		params.addRule(RelativeLayout.RIGHT_OF, image.getId());
		StyleSettingTool.setMargin(setting.detail().userInfo().name().margin(), params);
		layout.addView(name, params);
		
		TextView registerDate = new TextView(this);
		registerDate.setId(id++);
		registerDate.setText("注册时间: " + DateUtil.date2String(user.getRegisterDate(), DateUtil.SIMPLE_PATTERN));
		registerDate.setEnabled(false);
		StyleSettingTool.setTextStyle(registerDate, setting.detail().userInfo().registerDate());
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, name.getId());
		params.addRule(RelativeLayout.ALIGN_LEFT, name.getId());
		StyleSettingTool.setMargin(setting.detail().userInfo().registerDate().margin(), params);
		layout.addView(registerDate, params);
		
		return layout;
	}
	
	private LinearLayout createTotalMoney() {
		LinearLayout layout = new LinearLayout(this);
		layout.setId(id++);
		StyleSettingTool.setBackground(layout, setting.totalMoney());
		createTotalMoneyLabel(layout);
		createTotalMoneyText(layout);
		createTotalMoneyExchange(layout);
		return layout;
	}
	
	private void createTotalMoneyExchange(LinearLayout parent) {
		TextView exchange = createTotalMoneyExchange();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		StyleSettingTool.setMargin(setting.totalMoney().exchange().margin(), params);
		parent.addView(exchange, params);
	}
	
	private TextView createTotalMoneyExchange() {
		TextView text = new TextView(this);
		text.setText("申请兑换 > ");
		StyleSettingTool.setTextStyle(text, setting.totalMoney().exchange());
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SimpleMadApp.instance().isInNetwork()) {
					Intent exchangeIntent = new Intent(v.getContext(), ExchangeActivity.class);
					startActivityForResult(exchangeIntent, RequestAndResultCode.RequestCode.REQUEST_USER_EXCHANGE_MONEY);
				} else {
					Toast.makeText(v.getContext(), "请打开网络申请积分兑换", Toast.LENGTH_LONG);
				}
			}
		});
		return text;
	}
	
	private void createTotalMoneyText(LinearLayout parent) {
		TextView text = createTotalMoneyText();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		StyleSettingTool.setMargin(setting.totalMoney().money().margin(), params);
		parent.addView(text, params);
	}
	
	private TextView createTotalMoneyText() {
		TextView text = new TextView(this);
		text.setText(user.getMoney() + "分");
		StyleSettingTool.setTextStyle(text, setting.totalMoney().money());
		return text;
	}
	
	private void createTotalMoneyLabel(LinearLayout parent) {
		TextView label = createTotalMoneyLabel();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		StyleSettingTool.setMargin(setting.totalMoney().label().margin(), params);
		parent.addView(label, params);
	}
	
	private TextView createTotalMoneyLabel() {
		TextView label = new TextView(this);
		label.setText("你的账户余额为");
		StyleSettingTool.setTextStyle(label, setting.totalMoney().label());
		return label;
	}
	
	private View createModifyButton() {
		Button btn = new Button(this);
		btn.setText("修改资料");
		StyleSettingTool.setTextStyle(btn, setting.detail().detailInfo().modifyBtn());
		if(SimpleMadApp.instance().isInNetwork()) {
			btn.setEnabled(true);
		} else {
			btn.setEnabled(false);
		}
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SimpleMadApp.instance().isInNetwork()) {
					startActivityForResult(new Intent(UserStatisticsViewActivity.this, UserStatisticsModifyActivity.class), 
							RequestAndResultCode.RequestCode.REQUEST_USER_STATISTICS_MODIFY);
				} else {
					Toast.makeText(UserStatisticsViewActivity.this, "请打开网络连接进行修改", Toast.LENGTH_LONG);
				}
			}
		});
		return btn;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RequestAndResultCode.RequestCode.REQUEST_USER_STATISTICS_MODIFY) {
			refresh();
		}
		if(requestCode == RequestAndResultCode.RequestCode.REQUEST_USER_EXCHANGE_MONEY) {
			if(resultCode == RequestAndResultCode.ResultCode.RESULT_USER_EXCHANGE_MONEY_OK) {
				refresh();
			}
		}
	}
	
	private LinearLayout.LayoutParams modifyButtonParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.detail().detailInfo().modifyBtn().margin(), params);
		return params;
	}
	
	private LinearLayout.LayoutParams detailItemParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.detail().detailInfo().labelField().contentMargin(), params);
		return params;
	}
	
	private void ready() {
		setting = AppSetting.userStatisticsSetting();
		userService = UserServiceImpl.instance();
		if(isCreated) {
			user = userService.find();
		} else {
			isCreated = true;
			user = userService.synchronizedUser();
		}
		if(SimpleMadApp.DEBUG_MODE) {
			user.setAttrs(createTestAttrs());
		}
	}
	
	private List<UserAttribute> createTestAttrs() {
		int order = 1;
		List<UserAttribute> attrs = new ArrayList<UserAttribute>();
		for(int index = 0; index < 20; index++) {
			UserAttribute attr = new UserAttribute();
			attr.setMobile(user.getMobile());
			attr.setOrder(order++);
			attr.setKey("姓名");
			attr.setValue("赖细明");
			attrs.add(attr);
		}
		
		return attrs;
	}
	
	@Override
	protected void onResume() {
		refresh();
		super.onResume();
	}
//	@Override
//	public void onBackPressed() {
//		AppUtil.homePresse(this);
//	}
}
