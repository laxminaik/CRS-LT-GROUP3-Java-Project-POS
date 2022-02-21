/**
 * 
 */
package com.lt.service;

import java.sql.SQLException;
import java.util.List;

import com.lt.bean.*;
import com.lt.exception.GradeNotAllotedException;

public interface ProfessorInterface {

	public boolean addGrade(String studentID, String courseID, String grade)
			throws GradeNotAllotedException;

	public List<Course> viewCourses(String profID);

	String getProfessorById(String profId);

	public List<EnrolledStudent> viewEnrolledStudents(String profId)
			throws SQLException;

}
