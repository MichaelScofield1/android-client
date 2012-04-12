package com.simplemad.android.activity.content;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.activity.NoTitleActivity;
import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.InputSetting;
import com.simplemad.android.setting.UserPasswordSetting;
import com.simplemad.android.util.DialogUtil;
import com.simplemad.android.util.StringUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.BackgroundProgressDialog;
import com.simplemad.android.view.setting.TextViewSetting;

public class UserPasswordManagerActivity extends NoTitleActivity {

	protected EditText oldPassword;
	protected EditText newPassword;
	protected EditText newConfirmPassword;
	
	private UserPasswordSetting setting;
	
	protected UserAccountService userAccountService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initUI();
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
	
	private void createTitle(RelativeLayout layout) {
		TextView title = new TextView(this);
		title.setText("修改密码");
		StyleSettingTool.setTextStyle(title, setting.title());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.title().margin(), params);
		layout.addView(title, params);
	}
	
	private void createContent(RelativeLayout layout) {
		View content = createContent();		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.alignWithParent = true;
		layout.addView(content, params);
	}
	
	private View createContent() {
		LinearLayout content = new LinearLayout(this);
		content.setOrientation(LinearLayout.VERTICAL);
		content.setGravity(Gravity.CENTER);
		
		content.addView(createOldPassword(), contentItemParams());
		content.addView(createNewPassword(), contentItemParams());
		content.addView(createConfirmPassword(), contentItemParams());
		content.addView(createButton(), contentItemParams());

		return content;
	}
	
	private void ready() {
		setting = AppSetting.userPasswordSetting();
		userAccountService = UserAccountServiceImpl.instance();
	}
	
	private LinearLayout.LayoutParams contentItemParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.contentMargin(), params);
		return params;
	}
	
	private LinearLayout createpassword(String labelText, InputSetting inputSetting) {
		LinearLayout layout = new LinearLayout(this);
		StyleSettingTool.setBackground(layout, inputSetting);
		
		TextView label = new TextView(this);
		label.setText(labelText + ":");
		StyleSettingTool.setTextStyle(label, inputSetting.label());
		layout.addView(label, itemLabelParams());
		
		EditText password = new EditText(this);
		password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		password.setTransformationMethod(new PasswordTransformationMethod());
		StyleSettingTool.setBackgroundEmpty(password);
		StyleSettingTool.setTextStyle(password, inputSetting.edit());
		layout.addView(password, itemTextParams(inputSetting.edit()));
		
		return layout;
	}
	
	private View createOldPassword() {
		LinearLayout layout = createpassword("原密码", setting.originalPsw());
		oldPassword = (EditText) layout.getChildAt(1);
		return layout;
	}
	
	private View createNewPassword() {
		LinearLayout layout = createpassword("密  码", setting.newPsw());
		newPassword = (EditText) layout.getChildAt(1);
		return layout;
	}
	
	private View createConfirmPassword() {
		LinearLayout layout = createpassword("确认密码", setting.confirmPsw());
		newConfirmPassword = (EditText) layout.getChildAt(1);
		return layout;
	}
	
	private LinearLayout.LayoutParams itemLabelParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = 130;
		return params;
	}
	
	private LinearLayout.LayoutParams itemTextParams(TextViewSetting tvs) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(tvs.margin(), params);
		params.weight = 1;
		return params;
	}
	
	private View createButton() {
		LinearLayout layout = new LinearLayout(this);
		Button btn = new Button(this);
		btn.setText("修 改");
		StyleSettingTool.setTextStyle(btn, setting.modifyBtn());
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StringUtil.isEmpty(oldPassword.getText().toString())) {
					DialogUtil.sayError(UserPasswordManagerActivity.this, "原密码不能为空");
					oldPassword.requestFocus();
					return;
				}
				if(StringUtil.isEmpty(newPassword.getText().toString())) {
					DialogUtil.sayError(UserPasswordManagerActivity.this, "新密码不能为空");
					newPassword.requestFocus();
					return;
				}
				if(!newPassword.getText().toString().equals(newConfirmPassword.getText().toString())) {
					DialogUtil.sayError(UserPasswordManagerActivity.this, "密码不一致");
					newConfirmPassword.requestFocus();
					return;
				}
				BackgroundProgressDialog dialog = new BackgroundProgressDialog(UserPasswordManagerActivity.this, "正在修改密码,请稍候...") {
					
					private boolean isSuccess;
					
					@Override
					protected void work() throws Exception {
						isSuccess = userAccountService.modifyPassword(newPassword.getEditableText().toString(), oldPassword.getEditableText().toString());
					}
					
					@Override
					protected void complete() throws Exception {
						if(isSuccess) {
							Toast.makeText(UserPasswordManagerActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
							finish();
						} else if(!SimpleMadApp.instance().isInNetwork()) {
							DialogUtil.sayError(UserPasswordManagerActivity.this, "网络连接不存在,密码修改失败");
							oldPassword.requestFocus();
						} else {
							DialogUtil.sayError(UserPasswordManagerActivity.this, "密码修改失败");
							oldPassword.requestFocus();
						}
					}
				};
				dialog.show();
			}
		});
		layout.addView(btn, itemOtherLeftParams());
		btn = new Button(this);
		btn.setText("取 消");
		StyleSettingTool.setTextStyle(btn, setting.cancelBtn());
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();		
			}
		});
		layout.addView(btn, itemOtherRightParams());
		return layout;
	}
	
	private LinearLayout.LayoutParams itemOtherLeftParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		return params;
	}
	
	private LinearLayout.LayoutParams itemOtherRightParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		return params;
	}
}
