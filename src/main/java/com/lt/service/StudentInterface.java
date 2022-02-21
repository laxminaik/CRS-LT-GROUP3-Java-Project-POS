package com.lt.service;

import com.lt.constant.GenderConstant;
import com.lt.exception.StudentNotRegisteredException;

public interface StudentInterface {

	public String register(String name, String userID, String password,
			GenderConstant gender, int batch, String branch, String address)
			throws StudentNotRegisteredException;

	public String getStudentId(String userId);

	public boolean isApproved(String studentId);
}