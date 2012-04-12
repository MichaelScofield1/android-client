package com.simplemad.android.service;

import java.util.List;

import com.simplemad.bean.UserAccount;


public interface UserAccountService {

	UserAccount loadFirstUserAccount();
	
	boolean loginLocal(UserAccount userAccount, boolean isLoginToServer);
	
	/**
	 * @param userAccount
	 * @return true if login server message not send, false if message send
	 */
	boolean loginRemote(UserAccount userAccount);
	
	/**
	 * @param userAccount
	 * @return false if login server message not send, true if message send
	 */
	boolean reloginRemote();
	
	boolean loginrecently();
	
	boolean logout();
	/**
	boolean changeNetwork(boolean hasNetwork);
	*/
	
	List<UserAccount> loadAvaiableUserAccounts();
	
	/**
	 * @param mobile
	 * @param password
	 * @return true if register success, false if failure
	 */
	boolean registerRemote(long mobile, String password);
	
	boolean registerLocal(long mobile, String password);
	
	boolean clean(long mobile);
	
	UserAccount getCurrentUserAccount();
	
	boolean modifyPassword(String newPassword, String oldPassword);
}
