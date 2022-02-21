/**
 * 
 */
package com.lt.exception;

public class StudentNotFoundForApprovalException extends Exception {

	private String StudentId;

	public StudentNotFoundForApprovalException(String id) {
		StudentId = id;
	}

	public String getUserId() {
		return StudentId;
	}

	@Override
	public String getMessage() {
		return "StudentId: " + StudentId + " not registered!";
	}
}
