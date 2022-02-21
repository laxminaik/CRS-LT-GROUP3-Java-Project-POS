/**
 * 
 */
package com.lt.exception;

public class CourseLimitExceededException extends Exception {

	private int num;

	public CourseLimitExceededException(int num) {
		this.num = num;
	}

	@Override
	public String getMessage() {
		return "You have already registered for " + num + " courses";
	}
}
