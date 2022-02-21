package com.lt.dao;

import java.sql.SQLException;
import java.util.List;

import com.lt.bean.Course;
import com.lt.bean.Grade;
import com.lt.bean.Notification;
import com.lt.constant.PaymentModeConstant;

public interface RegistrationDaoInterface {

	public boolean addCourse(String courseCode, String studentId)
			throws SQLException;

	public boolean dropCourse(String courseCode, String studentId)
			throws SQLException;

	public List<Course> viewCourses(String studentId) throws SQLException;

	public List<Course> viewRegisteredCourses(String studentId)
			throws SQLException;

	public double calculateFee(String studentId) throws SQLException;

	public boolean seatAvailable(String courseCode) throws SQLException;

	public int numOfRegisteredCourses(String studentId) throws SQLException;

	public boolean isRegistered(String courseCode, String studentId)
			throws SQLException;

	public boolean getRegistrationStatus(String studentId) throws SQLException;

	public boolean getPaymentStatus(String studentId) throws SQLException;

	public void setRegistrationStatus(String studentId) throws SQLException;

	public List<Grade> viewGradeCard(String studentId) throws SQLException;

	public boolean isReportGenerated(String studentId) throws SQLException;

	public void setPaymentStatus(String studentId) throws SQLException;

	public int checkCourseAvailability(String studentId, String courseCode)
			throws SQLException;

	public Notification payFee(String studentId, PaymentModeConstant mode,
			double fee) throws SQLException;

	int checkCourseAvailability(int studentId, String courseCode)
			throws SQLException;

}
