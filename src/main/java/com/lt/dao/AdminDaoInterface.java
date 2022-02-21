/**
 * 
 */
package com.lt.dao;

import java.util.List;

import com.lt.bean.Course;
import com.lt.bean.Professor;
import com.lt.bean.RegisteredCourse;
import com.lt.bean.Student;
import com.lt.bean.User;
import com.lt.exception.CourseExistsAlreadyException;
import com.lt.exception.CourseNotDeletedException;
import com.lt.exception.CourseNotFoundException;
import com.lt.exception.ProfessorNotAddedException;
import com.lt.exception.StudentNotFoundForApprovalException;
import com.lt.exception.UserIdAlreadyInUseException;
import com.lt.exception.UserNotAddedException;
import com.lt.exception.UserNotFoundException;

public interface AdminDaoInterface {

	public List<Course> viewCourses();

	public List<Professor> viewProfessors();

	public void setGeneratedReportCardTrue(String Studentid);

	public List<RegisteredCourse> generateGradeCard(String Studentid);

	public List<Student> viewPendingAdmissions();

	public void approveStudent(String studentid)
			throws StudentNotFoundForApprovalException;

	public void addProfessor(Professor professor)
			throws ProfessorNotAddedException, UserIdAlreadyInUseException;

	public void removeCourse(String coursecode) throws CourseNotFoundException,
			CourseNotDeletedException;

	public void addCourse(Course course) throws CourseExistsAlreadyException;

	public void assignCourse(String courseCode, String professorId)
			throws CourseNotFoundException, UserNotFoundException;

	public void addUser(User user) throws UserNotAddedException,
			UserIdAlreadyInUseException;

	public void deleteCourse(String courseCode) throws CourseNotFoundException;
}
