package com.simplemad.android.setting;

import com.simplemad.android.setting.base.BaseAdListSetting;
import com.simplemad.android.setting.base.BaseAppBackground;
import com.simplemad.android.setting.base.BaseExchangeSetting;
import com.simplemad.android.setting.base.BaseLoginSetting;
import com.simplemad.android.setting.base.BaseRegisterSetting;
import com.simplemad.android.setting.base.BaseTabsSetting;
import com.simplemad.android.setting.base.BaseUserManualSetting;
import com.simplemad.android.setting.base.BaseUserPasswordSetting;
import com.simplemad.android.setting.base.BaseUserProtocolSetting;
import com.simplemad.android.setting.base.BaseUserStatisticsSetting;

public class AppSetting {

	public static BaseAppBackground getAppBackground() {
		return new BaseAppBackground();
	}
	
	public static LoginSetting loginSetting() {
		return new BaseLoginSetting();
	}
	
	public static RegisterSetting registerSetting() {
		return new BaseRegisterSetting();
	}
	
	public static TabsSetting tabsSetting() {
		return new BaseTabsSetting();
	}
	
	public static AdListSetting adListSetting() {
		return new BaseAdListSetting();
	}
	
	public static UserPasswordSetting userPasswordSetting() {
		return new BaseUserPasswordSetting();
	}
	
	public static UserStatisticsSetting userStatisticsSetting() {
		return new BaseUserStatisticsSetting();
	}

	public static UserManualSetting userManualSetting() {
		return new BaseUserManualSetting();
	}
	
	public static ExchangeSetting exchangeSetting() {
		return new BaseExchangeSetting();
	}
	
	public static UserProtocolSetting protocolSetting() {
		return new BaseUserProtocolSetting();
	}
}
