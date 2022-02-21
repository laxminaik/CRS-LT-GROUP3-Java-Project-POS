package com.lt.exception;

public class CourseNotAvailableException extends Exception {

	private String courseCode;

	public CourseNotAvailableException(String courseCode) {
		this.courseCode = courseCode;
	}

	@Override
	public String getMessage() {
		return "Seats are not available in : " + courseCode;
	}

}