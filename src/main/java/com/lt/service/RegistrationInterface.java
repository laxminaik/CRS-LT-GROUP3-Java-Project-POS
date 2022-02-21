package com.lt.service;

import java.sql.SQLException;
import java.util.List;

import com.lt.bean.Course;
import com.lt.bean.Grade;
import com.lt.bean.Notification;
import com.lt.constant.PaymentModeConstant;
import com.lt.exception.CourseAlreadyRegisteredException;
import com.lt.exception.CourseLimitExceededException;
import com.lt.exception.CourseNotFoundException;
import com.lt.exception.SeatNotAvailableException;

public interface RegistrationInterface {

	public boolean addCourse(String courseCode, String studentId,
			List<Course> availableCourseList) throws CourseNotFoundException,
			CourseLimitExceededException, SeatNotAvailableException,
			SQLException;

	/*
	 * throws SQLException
	 */
	void setRegistrationStatus(String studentId) throws SQLException;

	/*
	 * throws SQLException
	 */
	boolean getRegistrationStatus(String studentId) throws SQLException;

	boolean getPaymentStatus(String studentId) throws SQLException;

	/*
	 * throws SQLException
	 */
	List<Course> viewRegisteredCourses(String studentId) throws SQLException;

	/*
	 * throws SQLException
	 */
	List<Course> viewCourses(String studentId) throws SQLException;

	/*
	 * throws SQLException
	 */
	List<Grade> viewGradeCard(String studentId) throws SQLException;

	/*
	 * throws SQLException
	 */
	double calculateFee(String studentId) throws SQLException;

	/*
	 * throws CourseNotFoundException
	 * 
	 * @throws SQLException
	 */
	boolean dropCourse(String courseCode, String studentId,
			List<Course> registeredCourseList) throws CourseNotFoundException,
			SQLException;

	public boolean isReportGenerated(String studentId) throws SQLException;

	public void setPaymentStatus(String studentId) throws SQLException;

	/*
	 * throws CourseNotFoundException
	 * 
	 * @throws SeatNotAvailableException
	 * 
	 * @throws CourseLimitExceedException
	 * 
	 * @throws SQLException
	 */
	boolean checkCourse(String courseCode, String studentId,
			List<Course> availableCourseList)
			throws CourseLimitExceededException,
			CourseAlreadyRegisteredException, SeatNotAvailableException,
			CourseNotFoundException;

	public boolean addCourse(String courseCode, String studentId);

	public Notification payFee(String studentId, PaymentModeConstant mode,
			double fee) throws SQLException;

	// public boolean checkCourse(String courseCode, String studentId,
	// List<Course> availableCourseList) throws CourseLimitExceededException,
	// CourseAlreadyRegisteredException, SeatNotAvailableException,
	// CourseNotFoundException;

}