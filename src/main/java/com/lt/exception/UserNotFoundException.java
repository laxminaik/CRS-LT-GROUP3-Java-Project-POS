package com.lt.exception;

public class UserNotFoundException extends Exception {

	private String userId;

	public UserNotFoundException(String id) {
		userId = id;
	}

	public String getMessage() {
		return "User with userId: " + userId + " not found.";
	}

}