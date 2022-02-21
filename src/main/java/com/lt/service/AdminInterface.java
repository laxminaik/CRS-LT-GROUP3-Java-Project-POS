/**
 * 
 */
package com.lt.service;

import java.util.List;

import com.lt.bean.*;
import com.lt.exception.*;


public interface AdminInterface 
{
	
	public void setGeneratedReportCardTrue(String Studentid);
	
	
	public List<Course> viewCourses();
	

	public List<Professor> viewProfessors();
	
	
	public List<Student> viewPendingAdmissions();
	
	
	public List<RegisteredCourse> generateGradeCard(String Studentid);
	
	
	
	public void approveStudent(String studentid, List<Student> studentlist) throws StudentNotFoundForApprovalException;
	
	
	public void addProfessor(Professor professor) throws ProfessorNotAddedException, UserIdAlreadyInUseException;
	
	/* throws CourseNotFoundException 
	 * throws CourseNotDeletedException 
	 */

	public void removeCourse(String coursecode, List<Course> courselist) throws CourseNotFoundException, CourseNotDeletedException;
	
	/*@throws CourseExistsAlreadyException;
	 */
	
	public void addCourse(Course course, List<Course> courselist) throws CourseExistsAlreadyException;
	
	/*throws CourseNotFoundException 
	 * throws UserNotFoundException 
	 */
	public void assignCourse(String courseCode, String professorId) throws CourseNotFoundException, UserNotFoundException;

	public void deleteCourse(String courseCode, List<Course> courseList) throws CourseNotFoundException;
	
}
