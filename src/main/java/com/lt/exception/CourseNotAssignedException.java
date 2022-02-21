/**
 * 
 */
package com.lt.exception;

public class CourseNotAssignedException extends Exception {

	private String courseCode;
	private String userId;

	public CourseNotAssignedException(String courseCode, String userId) {
		this.courseCode = courseCode;
		this.userId = userId;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	@Override
	public String getMessage() {
		return "courseCode: " + courseCode
				+ " couldn't be assigned to UserId: " + userId;
	}
}
