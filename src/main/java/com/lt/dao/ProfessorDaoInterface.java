package com.lt.dao;

import java.util.*;

import com.lt.bean.Course;
import com.lt.bean.EnrolledStudent;
import com.lt.bean.Student;

public interface ProfessorDaoInterface {

	public List<Course> getCoursesByProfessor(String userId);

	public List<EnrolledStudent> getEnrolledStudents(String courseId);

	public Boolean addGrade(String studentId, String courseCode, String grade);

	public String getProfessorById(String profId);
}