package edu.polyu.comp.service;

import edu.polyu.comp.dao.UserDao;
import edu.polyu.comp.domain.User;
import edu.polyu.comp.util.StringUtil;

public class UserService {
	
	private UserDao userDao;
	public UserDao getUserDao() {
		if (userDao == null) {
			userDao = new UserDao();
		}
		return userDao;
	}
	

	public boolean createUser(User user) {
		return getUserDao().createUser(user);
	}
	
	public String findUserNameByPhoneNumber(String phoneNumber) {
		String userName = "";
		if (!StringUtil.isEmpty(phoneNumber)) {
			User user = getUserDao().findUserByPhoneNumber(phoneNumber);
			if(user!=null && !StringUtil.isEmpty(user.getName())){
				userName = user.getName();
			}
		}
		return userName;
	}
	
	public String findDeviceTokenByPhoneNumber(String phoneNumber) {
		String deviceToken = "";
		if (!StringUtil.isEmpty(phoneNumber)) {
			User user = getUserDao().findUserByPhoneNumber(phoneNumber);
			if(user!=null && !StringUtil.isEmpty(user.getDeviceToken())){
				deviceToken = user.getDeviceToken();
			}
		}
		return deviceToken;
	}

}
