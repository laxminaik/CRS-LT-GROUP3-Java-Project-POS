/**
 * 
 */
package com.lt.service;

import com.lt.exception.UserNotFoundException;

public interface UserInterface {

	String getRole(String userId);

	boolean verifyCredentials(String userID, String password)
			throws UserNotFoundException;

	boolean updatePassword(String userID, String newPassword);

}
