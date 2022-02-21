package com.lt.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lt.bean.Course;
import com.lt.bean.EnrolledStudent;
import com.lt.dao.ProfessorDaoInterface;
import com.lt.dao.ProfessorDaoOperation;
import com.lt.exception.GradeNotAllotedException;

public class ProfessorOperation implements ProfessorInterface {

	private static volatile ProfessorOperation instance = null;
	ProfessorDaoInterface professorDAOInterface = ProfessorDaoOperation
			.getInstance();

	private ProfessorOperation() {

	}

	public static ProfessorOperation getInstance() {
		if (instance == null) {
			synchronized (ProfessorOperation.class) {
				instance = new ProfessorOperation();
			}
		}
		return instance;
	}

	/*
	 * throws GradeNotAddedException
	 */
	@Override
	public boolean addGrade(String studentId, String courseCode, String grade)
			throws GradeNotAllotedException {
		try {
			professorDAOInterface.addGrade(studentId, courseCode, grade);
		} catch (Exception ex) {
			throw new GradeNotAllotedException(studentId);
		}
		return true;
	}

	@Override
	public List<EnrolledStudent> viewEnrolledStudents(String profId)
			throws SQLException {
		List<EnrolledStudent> enrolledStudents = new ArrayList<EnrolledStudent>();
		try {
			enrolledStudents = professorDAOInterface
					.getEnrolledStudents(profId);
		} catch (Exception ex) {
			throw ex;
		}
		return enrolledStudents;
	}

	@Override
	public List<Course> viewCourses(String profId) {
		// call the DAO class
		// get the courses for the professor
		List<Course> coursesOffered = new ArrayList<Course>();
		try {
			coursesOffered = professorDAOInterface
					.getCoursesByProfessor(profId);
		} catch (Exception ex) {
			throw ex;
		}
		return coursesOffered;
	}

	@Override
	public String getProfessorById(String profId) {
		return professorDAOInterface.getProfessorById(profId);
	}

}