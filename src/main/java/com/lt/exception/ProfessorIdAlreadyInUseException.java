package com.lt.exception;

public class ProfessorIdAlreadyInUseException extends Exception {
	private String ProfessorId;

	public ProfessorIdAlreadyInUseException(String id) {
		ProfessorId = id;
	}

	public String getUserId() {
		return ProfessorId;
	}

	@Override
	public String getMessage() {
		return "userId: " + ProfessorId + " is already in use.";
	}

}