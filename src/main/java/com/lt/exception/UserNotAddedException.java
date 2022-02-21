package com.lt.exception;

public class UserNotAddedException extends Exception {
	private String userId;

	public UserNotAddedException(String id) {
		userId = id;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public String getMessage() {
		return "UserId: " + userId + " not added!";
	}
}