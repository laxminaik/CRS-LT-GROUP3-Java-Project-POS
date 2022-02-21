package com.lt.exception;

public class StudentNotRegisteredException extends Exception {
	private String studentName;

	public StudentNotRegisteredException(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentName() {
		return studentName;
	}

}