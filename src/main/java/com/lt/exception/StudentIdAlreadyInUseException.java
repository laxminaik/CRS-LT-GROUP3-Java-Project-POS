package com.lt.exception;

public class StudentIdAlreadyInUseException extends Exception {
	private String StudentId;

	public StudentIdAlreadyInUseException(String id) {
		StudentId = id;
	}

	public String getUserId() {
		return StudentId;
	}

	@Override
	public String getMessage() {
		return "userId: " + StudentId + " is already in use.";
	}

}