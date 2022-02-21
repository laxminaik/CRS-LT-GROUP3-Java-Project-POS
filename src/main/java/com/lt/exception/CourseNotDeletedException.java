/**
 * 
 */
package com.lt.exception;

public class CourseNotDeletedException extends Exception {

	private String courseCode;

	public CourseNotDeletedException(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseCode() {
		return courseCode;
	}

	@Override
	public String getMessage() {
		return "Course with courseCode: " + courseCode + " can't be deleted.";
	}
}
