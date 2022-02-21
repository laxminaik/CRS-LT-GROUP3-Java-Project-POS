package com.lt.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.Log4JLogRecord;

import com.lt.bean.Course;
import com.lt.bean.Grade;
import com.lt.bean.Notification;
import com.lt.constant.PaymentModeConstant;
import com.lt.dao.RegistrationDaoInterface;
import com.lt.dao.RegistrationDaoOperation;
import com.lt.exception.CourseAlreadyRegisteredException;
import com.lt.exception.CourseLimitExceededException;
import com.lt.exception.CourseNotFoundException;
import com.lt.exception.SeatNotAvailableException;
import com.lt.validator.StudentValidator;

public class RegistrationOperation implements RegistrationInterface {

	private static volatile RegistrationOperation instance = null;

	private RegistrationOperation() {
	}

	public static RegistrationOperation getInstance() {
		if (instance == null) {
			synchronized (RegistrationOperation.class) {
				instance = new RegistrationOperation();
			}
		}
		return instance;
	}

	RegistrationDaoInterface registrationDaoInterface = RegistrationDaoOperation
			.getInstance();

	/*
	 * throws CourseNotFoundException
	 * 
	 * @throws SeatNotAvailableException
	 * 
	 * @throws CourseLimitExceedException
	 * 
	 * @throws SQLException
	 */
	@Override
	public boolean checkCourse(String courseCode, String studentId,
			List<Course> availableCourseList)
			throws CourseLimitExceededException,
			CourseAlreadyRegisteredException, SeatNotAvailableException,
			CourseNotFoundException {

		try {
			int response = registrationDaoInterface.checkCourseAvailability(
					studentId, courseCode);

			if (response == 0) {
				throw new CourseLimitExceededException(6);
			} else if (response == 1) {
				throw new CourseAlreadyRegisteredException(courseCode);
			} else if (!registrationDaoInterface.seatAvailable(courseCode)) {
				throw new SeatNotAvailableException(courseCode);
			} else if (!StudentValidator.isValidCourseCode(courseCode,
					availableCourseList)) {
				throw new CourseNotFoundException(courseCode);
			}

			return true;

		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

		return false;

	}

	public boolean addCourse(String courseCode, String studentId,
			List<Course> availableCourseList) throws CourseNotFoundException,
			CourseLimitExceededException, SeatNotAvailableException,
			SQLException {

		if (registrationDaoInterface.numOfRegisteredCourses(studentId) >= 6) {
			throw new CourseLimitExceededException(6);
		} else if (registrationDaoInterface.isRegistered(courseCode, studentId)) {
			return false;
		} else if (!registrationDaoInterface.seatAvailable(courseCode)) {
			throw new SeatNotAvailableException(courseCode);
		} else if (!StudentValidator.isValidCourseCode(courseCode,
				availableCourseList)) {
			throw new CourseNotFoundException(courseCode);
		}

		return registrationDaoInterface.addCourse(courseCode, studentId);

	}

	/*
	 * throws CourseNotFoundException
	 * 
	 * @throws SQLException
	 */
	@Override
	public boolean dropCourse(String courseCode, String studentId,
			List<Course> registeredCourseList) throws CourseNotFoundException,
			SQLException {
		if (!StudentValidator.isRegistered(courseCode, studentId,
				registeredCourseList)) {
			throw new CourseNotFoundException(courseCode);
		}

		return registrationDaoInterface.dropCourse(courseCode, studentId);

	}

	/*
	 * throws SQLException
	 */
	@Override
	public double calculateFee(String studentId) throws SQLException {
		return registrationDaoInterface.calculateFee(studentId);
	}

	/*
	 * throws SQLException
	 */
	@Override
	public List<Grade> viewGradeCard(String studentId) throws SQLException {
		return registrationDaoInterface.viewGradeCard(studentId);
	}

	/*
	 * return List of courses
	 * 
	 * @throws SQLException
	 */
	@Override
	public List<Course> viewCourses(String studentId) throws SQLException {
		return registrationDaoInterface.viewCourses(studentId);
	}

	/*
	 * throws SQLException
	 */
	@Override
	public List<Course> viewRegisteredCourses(String studentId)
			throws SQLException {
		return registrationDaoInterface.viewRegisteredCourses(studentId);
	}

	/*
	 * throws SQLException
	 */
	@Override
	public boolean getRegistrationStatus(String studentId) throws SQLException {
		return registrationDaoInterface.getRegistrationStatus(studentId);
	}

	@Override
	public void setRegistrationStatus(String studentId) throws SQLException {
		registrationDaoInterface.setRegistrationStatus(studentId);

	}

	@Override
	public boolean isReportGenerated(String studentId) throws SQLException {

		return registrationDaoInterface.isReportGenerated(studentId);
	}

	@Override
	public boolean getPaymentStatus(String studentId) throws SQLException {
		return registrationDaoInterface.getPaymentStatus(studentId);

	}

	@Override
	public void setPaymentStatus(String studentId) throws SQLException {
		registrationDaoInterface.setPaymentStatus(studentId);

	}

	@Override
	public boolean addCourse(String courseCode, String studentId) {

		try {
			registrationDaoInterface.addCourse(courseCode, studentId);
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

	@Override
	public Notification payFee(String studentId, PaymentModeConstant mode,
			double fee) throws SQLException {
		try {
			return registrationDaoInterface.payFee(studentId, mode, fee);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return null;

	}

}