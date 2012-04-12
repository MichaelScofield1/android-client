package com.simplemad.android.service;

import java.util.List;

import com.simplemad.bean.MoneyExchangeRecord;
import com.simplemad.bean.User;

public interface UserService {

	static final long EXCHANGABLE_MONEY_NUM = 50;
	static final long EXCHANGABLE_MONEY = EXCHANGABLE_MONEY_NUM * 100;
	/**
	 * 用户在登录成功时需要调用此方法设置UserService的userName
	 * @param userName
	 */
	void setMobile(long mobile);
	
	/**
	 * 用户第一次在本手机上登录成功时使用
	 * @return
	 */
	boolean create();
	
	boolean update(long money);
	
	long addMoney(long money);
	
	User find();
	
	User synchronizedUser();
	
	boolean exchange();
	
	List<MoneyExchangeRecord> findRecords();
}
