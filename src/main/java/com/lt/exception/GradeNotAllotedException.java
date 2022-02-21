package com.lt.exception;

public class GradeNotAllotedException extends Exception {

	private String studentId;

	public GradeNotAllotedException(String studentId2) {
		this.studentId = studentId2;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getMessage() {
		return "Student with id: " + studentId
				+ "hasn't been alloted a grade yet";
	}
}
