package com.simplemad.android.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.ClientUtil;
import com.simplemad.android.dao.UserAccountDAO;
import com.simplemad.android.dao.UserAccountDAOImpl;
import com.simplemad.android.dao.UserAttributeDAO;
import com.simplemad.android.dao.UserAttributeDAOImpl;
import com.simplemad.android.dao.UserDAO;
import com.simplemad.android.dao.UserDAOImpl;
import com.simplemad.bean.MoneyExchangeRecord;
import com.simplemad.bean.User;
import com.simplemad.bean.UserAccount;
import com.simplemad.bean.UserAttribute;
import com.simplemad.parameter.ClientParameter;

public class UserServiceImpl implements UserService {

	private static UserService userService;
	
	private UserDAO userDao;
	private UserAttributeDAO attrDao;
	
	private long mobile;
	
	private UserServiceImpl() {
		userDao = new UserDAOImpl();
		attrDao = new UserAttributeDAOImpl();
	}
	
	@Override
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	
	public static synchronized UserService instance() {
		if(userService == null) {
			userService = new UserServiceImpl();
		}
		return userService;
	}
	
	@Override
	public boolean create() {
		User user = new User();
		user.setMobile(mobile);
		user.setMoney(0);
		return userDao.add(user);
	}

	@Override
	public boolean update(long money) {
		User user = new User();
		user.setMobile(mobile);
		user.setMoney(money);
		return userDao.update(user);
	}

	@Override
	public long addMoney(long money) {
		User user = userDao.find(mobile);
		user.setMoney(user.getMoney() + money);
		userDao.update(user);
		return user.getMoney();
	}

	@Override
	public User find() {
		User user = userDao.find(mobile);
		if(user == null) {
			return null;
		}
		List<UserAttribute> attrs = attrDao.find(mobile);
		user.setAttrs(attrs);
		return user;
	}
	
	@Override
	public User synchronizedUser() {
		User user = loadFromServer();
		if(user == null) {
			return find();
		} else {
			attrDao.delete(user.getMobile());
			for(UserAttribute attr : user.getAttrs()) {
				attrDao.add(attr);
			}
			update(user.getMoney());
			return user;
		}
	}
	
	private User loadFromServer() {
		if(SimpleMadApp.DEBUG_MODE) {
			return null;
		}
		if(!SimpleMadApp.instance().isInNetwork()) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);
		try {
			return ClientUtil.doPost(ClientParameter.USER_ATTRIBUTE_FIND, params, User.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean exchange() {
		UserAccountDAO userAccountDao = new UserAccountDAOImpl();
		UserAccount account = userAccountDao.find(mobile);
		if(account == null) {
			return false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", account.getMobile());
		params.put("password", account.getPassword());
		params.put("exchangeMoney", EXCHANGABLE_MONEY);
		try {
			boolean result =  ClientUtil.doPost(ClientParameter.USER_EXCHANGE_MONEY, params, boolean.class);
			if(result) {
				addMoney(-EXCHANGABLE_MONEY);
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MoneyExchangeRecord> findRecords() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);
		try {
			return ClientUtil.doPost(ClientParameter.USER_EXCHANGE_MONEY_HISTORY, params, List.class);
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
