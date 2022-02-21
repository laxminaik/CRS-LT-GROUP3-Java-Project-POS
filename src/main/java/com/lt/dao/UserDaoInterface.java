package com.lt.dao;

import com.lt.exception.UserNotFoundException;

public interface UserDaoInterface {

	public boolean verifyCredentials(String userId, String password)
			throws UserNotFoundException;

	public boolean updatePassword(String userID);

	public String getRole(String userId);

	public boolean updatePassword(String userID, String newPassword);
}