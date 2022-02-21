/**
 * 
 */
package com.lt.exception;

public class CourseExistsAlreadyException extends Exception {

	private String courseCode;

	public CourseExistsAlreadyException(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseCode() {
		return courseCode;
	}

	@Override
	public String getMessage() {
		return "Course: " + courseCode + " already exists in catalog.";
	}
}
