/**
 * 
 */
package com.lt.service;

import java.util.List;

import com.lt.bean.*;
import com.lt.dao.AdminDaoInterface;
import com.lt.dao.AdminDaoOperation;
import com.lt.exception.*;
import com.lt.validator.AdminValidator;

public class AdminOperation implements AdminInterface {

	private static volatile AdminOperation instance = null;

	private AdminOperation() {

	}

	public static AdminOperation getInstance() {
		if (instance == null) {
			synchronized (AdminOperation.class) {
				instance = new AdminOperation();
			}
		}
		return instance;
	}

	AdminDaoInterface adminDaoOperation = AdminDaoOperation.getInstance();

	public List<Course> viewCourses() {
		return adminDaoOperation.viewCourses();
	}

	public List<Professor> viewProfessors() {
		return adminDaoOperation.viewProfessors();
	}

	@Override
	public List<Student> viewPendingAdmissions() {
		return adminDaoOperation.viewPendingAdmissions();
	}

	public List<RegisteredCourse> generateGradeCard(String Studentid) {
		return adminDaoOperation.generateGradeCard(Studentid);
	}

	@Override
	public void removeCourse(String dropCourseCode, List<Course> courseList)
			throws CourseNotFoundException, CourseNotDeletedException {
		if (!AdminValidator.isValidDropCourse(dropCourseCode, courseList)) {
			System.out.println("courseCode: " + dropCourseCode
					+ " not present in catalog!");
			throw new CourseNotFoundException(dropCourseCode);
		}

		try {
			adminDaoOperation.removeCourse(dropCourseCode);
		} catch (CourseNotFoundException | CourseNotDeletedException e) {
			throw e;
		}
	}

	@Override
	public void addCourse(Course newCourse, List<Course> courseList)
			throws CourseExistsAlreadyException {

		try {
			if (!AdminValidator.isValidNewCourse(newCourse, courseList)) {
				System.out.println("courseCode: " + newCourse.getCourseCode()
						+ " already present in catalog!");
				throw new CourseExistsAlreadyException(
						newCourse.getCourseCode());
			}
			adminDaoOperation.addCourse(newCourse);
		} catch (CourseExistsAlreadyException e) {
			throw e;
		}
	}

	/*
	 * @throws StudentNotFoundException
	 */
	@Override
	public void approveStudent(String studentId, List<Student> studentList)
			throws StudentNotFoundForApprovalException {

		try {

			if (AdminValidator.isValidUnapprovedStudent(studentId, studentList)) {

				throw new StudentNotFoundForApprovalException(studentId);
			}
			adminDaoOperation.approveStudent(studentId);
		} catch (StudentNotFoundForApprovalException e) {

			throw e;
		}
	}

	/*
	 * throws ProfessorNotAddedException
	 */
	@Override
	public void addProfessor(Professor professor)
			throws ProfessorNotAddedException, UserIdAlreadyInUseException {
		try {
			adminDaoOperation.addProfessor(professor);
		} catch (ProfessorNotAddedException | UserIdAlreadyInUseException e) {
			throw e;
		}

	}

	/*
	 * throws CourseNotFoundException
	 * 
	 * @throws UserNotFoundException
	 */
	public void assignCourse(String courseCode, String professorId)
			throws CourseNotFoundException, UserNotFoundException {
		try {
			adminDaoOperation.assignCourse(courseCode, professorId);
		} catch (CourseNotFoundException | UserNotFoundException e) {
			throw e;
		}
	}

	@Override
	public void setGeneratedReportCardTrue(String Studentid) {
		adminDaoOperation.setGeneratedReportCardTrue(Studentid);

	}

	@Override
	public void deleteCourse(String courseCode, List<Course> courseList)
			throws CourseNotFoundException {
		if (!AdminValidator.isValidDropCourse(courseCode, courseList)) {
			System.out.println("courseCode: " + courseCode
					+ " not present in catalog!");
			throw new CourseNotFoundException(courseCode);
		}

		adminDaoOperation.deleteCourse(courseCode);

	}

}
