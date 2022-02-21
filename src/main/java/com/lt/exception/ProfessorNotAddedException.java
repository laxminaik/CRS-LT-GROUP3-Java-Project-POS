package com.lt.exception;

public class ProfessorNotAddedException extends Exception {
	private String professorId;

	public ProfessorNotAddedException(String id) {
		professorId = id;
	}

	public String getUserId() {
		return this.professorId;
	}

	@Override
	public String getMessage() {
		return "professorId: " + professorId + " not added!";
	}
}