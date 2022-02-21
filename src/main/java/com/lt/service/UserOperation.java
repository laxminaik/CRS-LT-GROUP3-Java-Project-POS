package com.lt.service;

import com.lt.dao.UserDaoInterface;
import com.lt.dao.UserDaoOperation;
import com.lt.exception.UserNotFoundException;

public class UserOperation implements UserInterface {

	private static volatile UserOperation instance = null;
	UserDaoInterface userDaoInterface = UserDaoOperation.getInstance();

	private UserOperation() {

	}

	public static UserOperation getInstance() {
		if (instance == null) {
			// This is a synchronized block, when multiple threads will access
			// this instance
			synchronized (UserOperation.class) {
				instance = new UserOperation();
			}
		}
		return instance;
	}

	@Override
	public boolean updatePassword(String userID, String newPassword) {
		System.out.println("1 upd");
		return userDaoInterface.updatePassword(userID, newPassword);
	}

	@Override
	public boolean verifyCredentials(String userID, String password)
			throws UserNotFoundException {
		// DAO class
		try {
			return userDaoInterface.verifyCredentials(userID, password);
		} finally {

		}
	}

	@Override
	public String getRole(String userId) {
		return userDaoInterface.getRole(userId);
	}

}