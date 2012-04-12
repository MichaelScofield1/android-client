package com.simplemad.android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.RegisterSetting;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.DialogUtil;
import com.simplemad.android.util.StringUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.BackgroundProgressDialog;
import com.simplemad.android.view.setting.TextViewSetting;
import com.simplemad.bean.UserAccount;

public class RegisterActivity extends NoTitleActivity {

	private static final int Login_Waiting_Id = 999;
	
	private Handler timeoutHandler;
	private static final int TIME_OUT_WHAT = 1;
	
	private EditText mobile;
	private EditText password;
	private EditText confirm_password;
	
	private UserAccountService userAccountService;
	private BroadcastReceiver loginReceiver;
	
	private RegisterSetting setting;
	
	private int id = 1;
	private TextView title;
	private LinearLayout content;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initUI();
	}
	
	private void initUI() {
		ready();
		
		RelativeLayout layout = new RelativeLayout(this);
		layout.setFocusableInTouchMode(true);
		StyleSettingTool.setBackground(layout, setting);
		createTitle(layout, setting.title());
		createContentLayout(layout);
		createTipsLayout(layout);
		setContentView(layout);
	}
	
	private void createTipsLayout(RelativeLayout parent) {
		LinearLayout layout = createTips();
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, content.getId());
		StyleSettingTool.setMargin(setting.contentMargin(), params);
		parent.addView(layout, params);
	}
	
	private LinearLayout createTips() {
		LinearLayout layout = new LinearLayout(RegisterActivity.this);
		layout.setId(id++);
		layout.setOrientation(LinearLayout.VERTICAL);
		StyleSettingTool.setBackground(layout, setting.tips());
		
		TextView tv = new TextView(RegisterActivity.this);
		tv.setText("注 意 : ");
		StyleSettingTool.setTextStyle(tv, setting.tips().title());
		layout.addView(tv, tipsItemParams(setting.tips().title()));
		
		tv = new TextView(RegisterActivity.this);
		tv.setText("1.请正确填写11位手机号码,否则无否获得充值服务");
		StyleSettingTool.setTextStyle(tv, setting.tips().first());
		layout.addView(tv, tipsItemParams(setting.tips().first()));
		
		tv = new TextView(RegisterActivity.this);
		tv.setText("2.请使用你的手机号码注册,否则无否获得充值服务");
		StyleSettingTool.setTextStyle(tv, setting.tips().second());
		layout.addView(tv, tipsItemParams(setting.tips().second()));
		
		return layout;
	}
	
	private LinearLayout.LayoutParams tipsItemParams(TextViewSetting tvSetting) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(tvSetting.margin(), params);
		return params;
	}

	private void createTitle(RelativeLayout parent, TextViewSetting titleSetting) {
		title = new TextView(this);
		title.setId(id++);
		title.setText("注 册");
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(titleSetting.margin(), params);
		StyleSettingTool.setTextStyle(title, titleSetting);
		parent.addView(title, params);
	}
	
	private void createContentLayout(RelativeLayout parent) {
		content = new LinearLayout(this);
		content.setId(id++);
//		layout.setGravity(Gravity.CENTER);
		content.addView(createContent(), contentParams());
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, title.getId());
		parent.addView(content, params);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在登录,请稍侯...");
		return dialog;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindLoginReceiver();
	}
	
	private void ready() {
		userAccountService = UserAccountServiceImpl.instance();
		setting = AppSetting.registerSetting();
	}
	
	private LinearLayout.LayoutParams contentParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		return params;
	}
	
	private View createContent() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(createUserName(), contentItemParams());
		layout.addView(createPassword(), contentItemParams());
		layout.addView(createConfirmPassword(), contentItemParams());
		layout.addView(createButton(), contentItemParams());
		return layout;
	}
	
	private LinearLayout.LayoutParams contentItemParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.contentMargin(), params);
		return params;
	}
	
	private View createUserName() {
		LinearLayout layout = new LinearLayout(this);
		StyleSettingTool.setBackground(layout, setting.user());
		TextView label = new TextView(this);
		label.setText("手机号码:");
		StyleSettingTool.setTextStyle(label, setting.user().label());
		layout.addView(label, itemLabelParams());
		mobile = new EditText(this);
		StyleSettingTool.setBackgroundEmpty(mobile);
		StyleSettingTool.setTextStyle(mobile, setting.user().edit());
		mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
		mobile.setHint("请输入你的手机号");
		mobile.setImeActionLabel("下一个", EditorInfo.IME_ACTION_NEXT);
		layout.addView(mobile, itemTextParams());
		return layout;
	}
	
	private View createPassword() {
		LinearLayout layout = new LinearLayout(this);
		StyleSettingTool.setBackground(layout, setting.password());
		TextView label = new TextView(this);
		label.setText("密    码:");
		StyleSettingTool.setTextStyle(label, setting.password().label());
		layout.addView(label, itemLabelParams());
		password = new EditText(this);
		StyleSettingTool.setBackgroundEmpty(password);
		StyleSettingTool.setTextStyle(password, setting.password().edit());
		password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		password.setTransformationMethod(new PasswordTransformationMethod());
		password.setImeActionLabel("下一个", EditorInfo.IME_ACTION_NEXT);
		layout.addView(password, itemTextParams());
		return layout;
	}
	
	private View createConfirmPassword() {
		LinearLayout layout = new LinearLayout(this);
		StyleSettingTool.setBackground(layout, setting.confirmPassword());
		TextView label = new TextView(this);
		label.setText("确认密码:");
		StyleSettingTool.setTextStyle(label, setting.confirmPassword().label());
		layout.addView(label, itemLabelParams());
		confirm_password = new EditText(this);
		StyleSettingTool.setBackgroundEmpty(confirm_password);
		StyleSettingTool.setTextStyle(confirm_password, setting.confirmPassword().edit());
		confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		confirm_password.setTransformationMethod(new PasswordTransformationMethod());
		confirm_password.setImeActionLabel("完成", EditorInfo.IME_ACTION_DONE);
		layout.addView(confirm_password, itemTextParams());
		return layout;
	}
	
	private LinearLayout.LayoutParams itemLabelParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = 130;
		return params;
	}
	
	private LinearLayout.LayoutParams itemTextParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		return params;
	}
	
	private View createButton() {
		LinearLayout layout = new LinearLayout(this);
		Button btn = new Button(this);
		btn.setText("注 册");
		StyleSettingTool.setTextStyle(btn, setting.register());
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StringUtil.isEmpty(mobile.getText().toString())) {
					DialogUtil.sayError(RegisterActivity.this, "用户名不能为空");
					mobile.requestFocus();
					return;
				}
				if(StringUtil.isEmpty(password.getText().toString())) {
					DialogUtil.sayError(RegisterActivity.this, "密码不能为空");
					password.requestFocus();
					return;
				}
				if(StringUtil.isEmpty(confirm_password.getText().toString())) {
					DialogUtil.sayError(RegisterActivity.this, "确认密码不能为空");
					confirm_password.requestFocus();
					return;
				}
				if(!password.getText().toString().equals(confirm_password.getText().toString())) {
					DialogUtil.sayError(RegisterActivity.this, "密码不一致");
					confirm_password.requestFocus();
					return;
				}
				Intent protocolIntent = new Intent(v.getContext(), UserProtocolActivity.class);
				startActivityForResult(protocolIntent, RequestAndResultCode.RequestCode.REQUEST_USER_PROTOCOL);
			}
		});
		layout.addView(btn, itemOtherLeftParams());
		btn = new Button(this);
		btn.setText("取 消");
		StyleSettingTool.setTextStyle(btn, setting.cancel());
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startLoginActivity();			
			}
		});
		layout.addView(btn, itemOtherRightParams());
		return layout;
	}
	
	protected void bindLoginReceiver() {
		if(loginReceiver == null) {
			loginReceiver = new LoginReceiver();
			IntentFilter filter = new IntentFilter(AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG);
			registerReceiver(loginReceiver, filter);
		}
	}
	
	protected void unbindLoginReceiver() {
		if(loginReceiver != null) {
			unregisterReceiver(loginReceiver);
		}
	}
	
	protected void startMainActivity() {
		Intent mainActivityIntent = new Intent(this, MainActivity.class);
		startActivity(mainActivityIntent);
		finish();
	}
	
	protected void startLoginActivity() {
		Intent loginActivityIntent = new Intent(this, LoginActivity.class);
		startActivity(loginActivityIntent);
		finish();
	}
	
	private LinearLayout.LayoutParams itemOtherLeftParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		StyleSettingTool.setMargin(setting.register().margin(), params);
		return params;
	}
	
	private LinearLayout.LayoutParams itemOtherRightParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		StyleSettingTool.setMargin(setting.cancel().margin(), params);
		return params;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RequestAndResultCode.RequestCode.REQUEST_USER_PROTOCOL && resultCode == RequestAndResultCode.ResultCode.RESULT_USER_PROTOCOL_OK) {
			bindLoginReceiver();
			BackgroundProgressDialog dialog = new BackgroundProgressDialog(RegisterActivity.this, "正在注册,请稍候...") {
				
				private boolean isRegisterSuccess;
				
				@Override
				protected void work() throws Exception {
					isRegisterSuccess = userAccountService.registerRemote(Long.valueOf(mobile.getText().toString()), password.getText().toString());
				}
				
				@Override
				protected void complete() throws Exception {
					if(isRegisterSuccess || SimpleMadApp.DEBUG_MODE) {
						boolean isRegisterLocal = userAccountService.registerLocal(Long.valueOf(mobile.getText().toString()), password.getText().toString());
						if(isRegisterLocal) {
							UserAccount userAccount = new UserAccount();
							userAccount.setMobile(Long.valueOf(mobile.getText().toString()));
							userAccount.setPassword(password.getText().toString());
							boolean msgNotSend = userAccountService.loginRemote(userAccount);
							if(msgNotSend) {
								boolean isLoginLocal =  userAccountService.loginLocal(userAccount, false);
								if(isLoginLocal) {
									startMainActivity();
								} else {
									DialogUtil.sayError(RegisterActivity.this, "登录失败");
								}
							} else {
								showDialog(Login_Waiting_Id);
								timeoutHandler = new Handler() {
									public void handleMessage(android.os.Message msg) {
										if(msg.what == TIME_OUT_WHAT && !isFinishing()) {
											dismissDialog(Login_Waiting_Id);
											DialogUtil.sayError(RegisterActivity.this, "与服务器连接超时, 登录失败");
										}
									};
								};
								timeoutHandler.sendEmptyMessageDelayed(TIME_OUT_WHAT, AppUtil.TIME_OUT);
							}
						} else {
							DialogUtil.sayError(RegisterActivity.this, "应用程序出错,请重新安装");
						}
					} else {
						if(!SimpleMadApp.instance().isInNetwork()) {
							DialogUtil.sayError(RegisterActivity.this, "注册失败,请打开网络连接!");
						} else {
							DialogUtil.sayError(RegisterActivity.this, "注册失败!");
						}
						mobile.requestFocus();
					}
				}
			}; 
			dialog.show();
		}
	}
	
	private class LoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG.equals(intent.getAction())) {
				boolean result = intent.getBooleanExtra(AppUtil.EXTRA_NAME_LOGIN_RESULT, false);
				dismissDialog(Login_Waiting_Id);
				if(result) {
					UserAccount userAccount = new UserAccount();
					userAccount.setMobile(Long.valueOf(mobile.getText().toString()));
					userAccount.setPassword(password.getText().toString());
					boolean isLoginLocal = userAccountService.loginLocal(userAccount, true);
					if(isLoginLocal) {
						startMainActivity();
					} else {
						DialogUtil.sayError(RegisterActivity.this, "应用程序出错,请重新登录");
					}
				} else {
					startLoginActivity();
				}
			}
		}
		
	}
}
