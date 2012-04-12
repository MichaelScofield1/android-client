package com.simplemad.android.activity;

import java.util.List;

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
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplemad.android.adapter.UserAccountAdapter;
import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.LoginSetting;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.CollectionUtil;
import com.simplemad.android.util.DialogUtil;
import com.simplemad.android.util.StringUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.bean.UserAccount;

public class LoginActivity extends NoTitleActivity {

	private AutoCompleteTextView mobile;
	private EditText password;
	private CheckBox remPassword;
	private CheckBox remLoginStatus;
	
	private UserAccount userAccount;
	private UserAccountService userAccountService;
	private List<UserAccount> userAccountList;
	
	private BroadcastReceiver loginResultReceiver;
	
	private static final int Login_Waiting_Id = 999;
	
	private Handler timeoutHandler;
	private static final int TIME_OUT_WHAT = 1;
	
	private LoginSetting setting;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initUI();
		registerLoginReceiver();
	}
	
	private void registerLoginReceiver() {
		loginResultReceiver = new LoginResultReceiver();
		IntentFilter filter = new IntentFilter(AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG);
		registerReceiver(loginResultReceiver, filter);
	}

	private void initUI() {
		ready();
		createUI();
		initUIData();
	}

	private void createUI() {
		RelativeLayout layout = new RelativeLayout(this);
		layout.setFocusableInTouchMode(true);
		StyleSettingTool.setBackground(layout, setting);
		createTitle(layout);
		createContent(layout);
		setContentView(layout);
	}

	private void createContent(RelativeLayout layout) {
		LinearLayout content = new LinearLayout(this);
		content.setGravity(Gravity.CENTER);
		content.addView(createContent(), contentParams());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.alignWithParent = true;
		layout.addView(content, params);
	}

	private void createTitle(RelativeLayout layout) {
		TextView title = new TextView(this);
		title.setText("登 录");
		StyleSettingTool.setTextStyle(title, setting.title());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.title().margin(), params);
		layout.addView(title, params);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(loginResultReceiver);
	}
	private void ready() {
		setting = AppSetting.loginSetting();
		userAccountService = UserAccountServiceImpl.instance();
		userAccountList = userAccountService.loadAvaiableUserAccounts();
		if(!CollectionUtil.isEmpty(userAccountList))
			userAccount = userAccountList.get(0);
	}
	
	private void initUIData() {
		if(userAccount == null) {
			remPassword.setChecked(true);
			return;
		}
		if(!mobile.getText().toString().equals(userAccount.getMobile())) {
			mobile.setText(String.valueOf(userAccount.getMobile()));
			if(!StringUtil.isEmpty(mobile.getEditableText().toString()))
				Selection.setSelection(mobile.getEditableText(), mobile.getEditableText().toString().length());
		}
		remLoginStatus.setChecked(userAccount.isRemLoginStatus());
		password.setText(userAccount.isRemPassword() ? userAccount.getPassword() : "");
		remPassword.setChecked(userAccount.isRemPassword());
	}
	
	private void generateData() {
		if(userAccount == null)
			userAccount = new UserAccount();
		userAccount.setMobile(Long.valueOf(mobile.getText().toString()));
		userAccount.setPassword(password.getText().toString());
		userAccount.setRemPassword(remPassword.isChecked());
		userAccount.setRemLoginStatus(remLoginStatus.isChecked());
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
		layout.addView(createRemember(), contentItemParams());
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
		mobile = new AutoCompleteTextView(this);
		StyleSettingTool.setBackgroundEmpty(mobile);
		StyleSettingTool.setTextStyle(mobile, setting.user().edit());
		mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
		UserAccountAdapter adapter = new UserAccountAdapter(this, userAccountList);
		mobile.setAdapter(adapter);
		mobile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AutoCompleteTextView completeView = (AutoCompleteTextView) v;
				UserAccountAdapter uaAdapter = (UserAccountAdapter) completeView.getAdapter();
				uaAdapter.getFilter().filter(completeView.getText().toString());
				completeView.showDropDown();
			}
		});
		mobile.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserAccountAdapter uaAdapter = (UserAccountAdapter) parent.getAdapter();
				userAccount = (UserAccount) uaAdapter.getItem(position);
				initUIData();
			}
		});
		mobile.setThreshold(0);
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
		password.setText("");
		password.setImeActionLabel("完成", EditorInfo.IME_ACTION_DONE);
		layout.addView(password, itemTextParams());
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
	
	private View createRemember() {
		LinearLayout layout = new LinearLayout(this);
		remPassword = new CheckBox(this);
		remPassword.setText("记住密码");
		layout.addView(remPassword, itemOtherLeftParams());
		
		remLoginStatus = new CheckBox(this);
		remLoginStatus.setText("自动登录");
		remLoginStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					if(!remPassword.isChecked())
						remPassword.setChecked(true);
				}
			}
		});
		remLoginStatus.setVisibility(View.INVISIBLE);
		layout.addView(remLoginStatus, itemOtherRightParams());
		return layout;
	}
	
	private View createButton() {
		LinearLayout layout = new LinearLayout(this);
		Button btn = new Button(this);
		StyleSettingTool.setTextStyle(btn, setting.login());
		btn.setText("登 录");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StringUtil.isEmpty(mobile.getText().toString())) {
					DialogUtil.sayError(LoginActivity.this, "用户名不能为空");
					mobile.requestFocus();
					return;
				}
				if(StringUtil.isEmpty(password.getText().toString())) {
					DialogUtil.sayError(LoginActivity.this, "密码不能为空");
					password.requestFocus();
					return;
				}
				generateData();
				userAccount.setLogined(false);
				boolean msgNotSend = userAccountService.loginRemote(userAccount);
				if(msgNotSend) {
					boolean isLoginLocal = userAccountService.loginLocal(userAccount, false);
					if(isLoginLocal) {
						startMainActivity();
					} else {
						DialogUtil.sayError(LoginActivity.this, "登录失败");
						mobile.requestFocus();
					}
				} else {
					//wait for the receiver invoke
					showDialog(Login_Waiting_Id);
					timeoutHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							if(msg.what == TIME_OUT_WHAT && !isFinishing()) {
								dismissDialog(Login_Waiting_Id);
								DialogUtil.sayError(LoginActivity.this, "与服务器连接超时, 登录失败");
							}
						};
					};
					timeoutHandler.sendEmptyMessageDelayed(TIME_OUT_WHAT, AppUtil.TIME_OUT);
				}
			}
		});
		layout.addView(btn, itemOtherLeftParams());
		
		Button registerBtn = new Button(this);
		StyleSettingTool.setTextStyle(registerBtn, setting.register());
		registerBtn.setText("注 册");
		registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startRegisterActivity();
			}
		});
		layout.addView(registerBtn, itemOtherRightParams());
		return layout;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在登录,请稍侯...");
		return dialog;
	}
	
	protected void startMainActivity() {
		Intent mainActivityIntent = new Intent(this, MainActivity.class);
		startActivity(mainActivityIntent);
		finish();
	}
	
	protected void startRegisterActivity() {
		Intent mainActivityIntent = new Intent(this, RegisterActivity.class);
		startActivity(mainActivityIntent);
		finish();
	}
	
	private LinearLayout.LayoutParams itemOtherLeftParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		StyleSettingTool.setMargin(setting.login().margin(), params);
		return params;
	}
	
	private LinearLayout.LayoutParams itemOtherRightParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.register().margin(), params);
		params.weight = 1;
		return params;
	}
	
	private class LoginResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(userAccount != null && AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG.equals(intent.getAction())) {
				boolean result = intent.getBooleanExtra(AppUtil.EXTRA_NAME_LOGIN_RESULT, false);
				dismissDialog(Login_Waiting_Id);
				if(!result) {
					DialogUtil.sayError(LoginActivity.this, "登录失败");
				} else {
					boolean isLoginLocal = userAccountService.loginLocal(userAccount, result);
					if(isLoginLocal) {
						startMainActivity();
					} else {
						DialogUtil.sayError(LoginActivity.this, "登录失败");
						mobile.requestFocus();
					}
				}
			}
		}
		
	}
}
