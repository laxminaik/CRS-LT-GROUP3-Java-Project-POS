package com.lt.exception;

public class UserIdAlreadyInUseException extends Exception {
	private String userId;

	public UserIdAlreadyInUseException(String id) {
		userId = id;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public String getMessage() {
		return "userId: " + userId + " is already in use.";
	}

}