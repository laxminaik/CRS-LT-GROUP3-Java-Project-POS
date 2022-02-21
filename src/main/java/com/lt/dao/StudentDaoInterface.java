/**
 * 
 */
package com.lt.dao;

import java.sql.SQLException;

import com.lt.bean.Student;
import com.lt.exception.StudentNotRegisteredException;

public interface StudentDaoInterface {

	public String addStudent(Student student)
			throws StudentNotRegisteredException;

	public String getStudentId(String userId);

	public boolean isApproved(String studentId);
}