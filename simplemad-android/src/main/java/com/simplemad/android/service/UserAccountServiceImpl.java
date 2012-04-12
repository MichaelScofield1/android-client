package com.simplemad.android.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.ClientUtil;
import com.simplemad.android.client.TimerManager;
import com.simplemad.android.dao.UserAccountDAO;
import com.simplemad.android.dao.UserAccountDAOImpl;
import com.simplemad.bean.MessageType;
import com.simplemad.bean.Timer;
import com.simplemad.bean.UserAccount;
import com.simplemad.parameter.ClientParameter;


public class UserAccountServiceImpl implements UserAccountService {

	private static final String PASSWORD = "password";
	private static final String MOBILE = "mobile";
	
	private UserAccountDAO userAccountDao;
	private static UserAccountService instance;
	private UserAccount userAccount;
	private UserService userService;
	
	private UserAccountServiceImpl() {
		userAccountDao = new UserAccountDAOImpl();
		userService = UserServiceImpl.instance();
	}
	
	public static synchronized UserAccountService instance() {
		if(instance == null)
			instance = new UserAccountServiceImpl();
		return instance;
	}
	@Override
	public UserAccount loadFirstUserAccount() {
		return userAccountDao.first();
	}
	
	@Override
	public boolean loginLocal(UserAccount userAccount, boolean isLoginToServer) {
		boolean isLogined = false;
		UserAccount loadedLocal = userAccountDao.find(userAccount.getMobile());
		if(isLoginToServer) {
			if(loadedLocal == null) {
				/*if login to the server, do the following*/
				loadedLocal = userAccount;
				loadedLocal.setLogined(true);
				loadedLocal.setLoginTime(new Date());
				isLogined = userAccountDao.add(loadedLocal);
				userService.setMobile(loadedLocal.getMobile());
				userService.create();
			} else {
				loadedLocal.setLogined(true);
				loadedLocal.setLoginTime(new Date());
				isLogined = userAccountDao.update(loadedLocal);
			}
		} else {
			if(loadedLocal == null) {
				isLogined = false;
			} else {
				if(loadedLocal.getPassword().equals(userAccount.getPassword())) {
					loadedLocal.setLogined(true);
					loadedLocal.setLoginTime(new Date());
					isLogined = userAccountDao.update(loadedLocal);
				} else {
					isLogined = false;
				}
			}
		}
		
		if(isLogined) {
			userService.setMobile(loadedLocal.getMobile());
//			if(isLoginToServer) {
//				userService.synchronizedUser();
//			}
			this.userAccount = loadedLocal;
		}
		return isLogined;
	}
	
	@Override
	public boolean loginRemote(UserAccount userAccount) {
		if(this.userAccount == null) {
			userAccount.setLogined(false);
		} else if(this.userAccount.getMobile() != userAccount.getMobile()) {
			userAccount.setLogined(false);
		}
		if(userAccount.isLogined()) {
			userService.setMobile(userAccount.getMobile());
			this.userAccount = userAccount;
			return true;
		}
		try {
			TimerManager.setTimer(ClientUtil.doGet(ClientParameter.TIMER, Timer.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loginToServer(userAccount);
	}

	/**
	@Override
	public boolean login(UserAccount userAccount) {
		boolean isLogined = false;
		System.out.println("UserAccountService userAccount.isLogined():" + userAccount.isLogined());
		if(this.userAccount == null) {
			userAccount.setLogined(false);
		} else if(this.userAccount.getMobile() != userAccount.getMobile()) {
			userAccount.setLogined(false);
		}
		if(userAccount.isLogined()) {
			userService.setMobile(userAccount.getMobile());
			this.userAccount = userAccount;
			return true;
		}
		boolean isLogin = loginToServer(userAccount);
		userAccount.setLogined(true);
		System.out.println("UserAccountService loginToServer:" + isLogin);
		UserAccount loadedLocal = userAccountDao.find(userAccount.getMobile());
		if(!isLogin) {
			isLogined = false;
		} else {
			if(loadedLocal == null) {
				//f login to the server, do the following
				userAccount.setLoginTime(new Date());
				isLogined = userAccountDao.add(userAccount);
				userService.setMobile(userAccount.getMobile());
				userService.create();
			} else {
				userAccount.setLoginTime(new Date());
				isLogined = userAccountDao.update(userAccount);
			}
		}
		if(isLogined) {
			logout();
			userService.setMobile(userAccount.getMobile());
			userService.synchronizedUser();
			this.userAccount = userAccount;
		}
		return isLogined;
	}
	*/
	
	private boolean loginAfterRegister(UserAccount userAccount) {
		UserAccount loadedLocal = userAccountDao.find(userAccount.getMobile());
		if(loadedLocal == null) {
			return false;
		}
		userService.setMobile(loadedLocal.getMobile());
		userService.create();
//		userService.synchronizedUser();
		this.userAccount = loadedLocal;
		return true;
	}

	private boolean notNeedLoginToServer(UserAccount userAccount) {
		if(SimpleMadApp.DEBUG_MODE) {
			return true;
		}
		if(!SimpleMadApp.instance().isInNetwork()) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean loginToServer(UserAccount userAccount) {
		if(notNeedLoginToServer(userAccount)) {
			return true;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MOBILE, userAccount.getMobile());
		params.put(PASSWORD, userAccount.getPassword());
		/**
		try {
			ClientUtil.doPost(ClientParameter.USER_LOGIN, params, Boolean.class);
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		*/
		ClientUtil.doSend(MessageType.LOGON, userAccount);
		return false;
	}
	
	private void logoutToServer(UserAccount userAccount) {
		if(SimpleMadApp.DEBUG_MODE) {
			return;
		}
		if(!SimpleMadApp.instance().isInNetwork()) {
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MOBILE, userAccount.getMobile());
		params.put(PASSWORD, userAccount.getPassword());
		/**
		try {
			ClientUtil.doPost(ClientParameter.USER_LOGOUT, params, Boolean.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		ClientUtil.doSend(MessageType.LOGOFF, userAccount);
	}

	@Override
	public boolean logout() {
		if(userAccount != null) {
			userAccount.setLogined(false);
			userAccountDao.update(userAccount);
			logoutToServer(userAccount);
		}
		userAccount = null;
		return true;
	}

	@Override
	public List<UserAccount> loadAvaiableUserAccounts() {
		return userAccountDao.getAllUserAccountByLoginTime();
	}

	@Override
	public boolean registerRemote(long mobile, String password) {
		UserAccount userAccount = new UserAccount();
		userAccount.setLoginTime(new Date());
		userAccount.setPassword(password);
		userAccount.setMobile(mobile);
		return registerToServer(userAccount);
	}
	
	@Override
	public boolean registerLocal(long mobile, String password) {
		UserAccount userAccount = new UserAccount();
		userAccount.setLoginTime(new Date());
		userAccount.setPassword(password);
		userAccount.setMobile(mobile);
		userAccount.setLogined(true);
		userAccountDao.add(userAccount);
		return loginAfterRegister(userAccount);
	}
	
	private boolean registerToServer(UserAccount userAccount) {
		if(SimpleMadApp.DEBUG_MODE) {
			return false;
		}
		if(!SimpleMadApp.instance().isInNetwork()) {
			return false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MOBILE, userAccount.getMobile());
		params.put(PASSWORD, userAccount.getPassword());
		
		try {
			return ClientUtil.doPost(ClientParameter.USER_REGISTER, params, Boolean.class);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean clean(long mobile) {
		UserAccount userAccount = new UserAccount();
		userAccount.setMobile(mobile);
		return userAccountDao.delete(userAccount);
	}

	@Override
	public UserAccount getCurrentUserAccount() {
		return userAccount;
	}

	@Override
	public boolean modifyPassword(String newPassword, String oldPassword) {
		boolean isModified = modifyPasswordInServer(newPassword, oldPassword);
		if(isModified) {
			userAccount.setPassword(newPassword);
			userAccountDao.update(userAccount);
		}
		return isModified;
	}

	private boolean modifyPasswordInServer(String newPassword,
			String oldPassword) {
		if(SimpleMadApp.DEBUG_MODE) {
			return true;
		}
		if(!SimpleMadApp.instance().isInNetwork()) {
			return false;
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mobile", userAccount.getMobile());
			params.put("oldPassword", oldPassword);
			params.put("newPassword", newPassword);
			return ClientUtil.doPost(ClientParameter.USER_PASSWORD_MODIFY, params, Boolean.class);
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean loginrecently() {
		userAccount = userAccountDao.first();
		userService.setMobile(userAccount.getMobile());
		return true;
	}

	@Override
	public boolean reloginRemote() {
		if(getCurrentUserAccount() != null) {
			UserAccount userAccount = new UserAccount();
			userAccount.setLogined(false);
			userAccount.setMobile(getCurrentUserAccount().getMobile());
			userAccount.setPassword(getCurrentUserAccount().getPassword());
			userAccount.setRemLoginStatus(getCurrentUserAccount().isRemLoginStatus());
			userAccount.setRemPassword(getCurrentUserAccount().isRemPassword());
			return !loginRemote(userAccount);
		}
		return false;
	}

}
