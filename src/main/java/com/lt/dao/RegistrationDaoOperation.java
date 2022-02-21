package com.lt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.lt.bean.Course;
import com.lt.bean.Grade;
import com.lt.bean.Notification;
import com.lt.constant.NotificationTypeConstant;
import com.lt.constant.PaymentModeConstant;
import com.lt.constant.SQLQueriesConstant;
import com.lt.exception.CourseNotFoundException;
import com.lt.utils.DBUtils;

public class RegistrationDaoOperation implements RegistrationDaoInterface {

	private static volatile RegistrationDaoOperation instance = null;
	private static Logger logger = Logger
			.getLogger(RegistrationDaoOperation.class);
	private PreparedStatement stmt = null;

	private RegistrationDaoOperation() {
	}

	public static RegistrationDaoOperation getInstance() {
		if (instance == null) {
			synchronized (RegistrationDaoOperation.class) {
				instance = new RegistrationDaoOperation();
			}
		}
		return instance;
	}

	@Override
	public boolean addCourse(String courseCode, String studentId)
			throws SQLException {

		Connection conn = DBUtils.getConnection();

		try {
			stmt = conn.prepareStatement(SQLQueriesConstant.ADD_COURSE);
			stmt.setString(1, studentId);
			stmt.setString(2, courseCode);
			stmt.setString(3, "-");
			stmt.executeUpdate();

			stmt = conn
					.prepareStatement(SQLQueriesConstant.DECREMENT_COURSE_SEATS);
			stmt.setString(1, courseCode);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}
		return false;

	}

	@Override
	public int numOfRegisteredCourses(String studentId) throws SQLException {

		Connection conn = DBUtils.getConnection();

		int count = 0;
		try {

			stmt = conn
					.prepareStatement(SQLQueriesConstant.NUMBER_OF_REGISTERED_COURSES);
			stmt.setString(1, studentId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count++;
			}
			return count;

		} catch (SQLException se) {

			logger.error(se.getMessage());

		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}

		return count;
	}

	@Override
	public boolean seatAvailable(String courseCode) throws SQLException {

		Connection conn = DBUtils.getConnection();
		try {
			stmt = conn.prepareStatement(SQLQueriesConstant.GET_SEATS);
			stmt.setString(1, courseCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return (rs.getInt("seats") > 0);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}

		return true;

	}

	/*
	 * throws SQLException
	 */
	@Override
	public boolean isRegistered(String courseCode, String studentId)
			throws SQLException {

		Connection conn = DBUtils.getConnection();

		boolean check = false;
		try {
			stmt = conn.prepareStatement(SQLQueriesConstant.IS_REGISTERED);
			stmt.setString(1, courseCode);
			stmt.setString(2, studentId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				check = true;
			}
		} catch (Exception e) {
			logger.error(e.getClass());
			logger.error(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}

		return check;

	}

	/*
	 * throws CourseNotFoundException
	 */
	@Override
	public boolean dropCourse(String courseCode, String studentId)
			throws SQLException {

		Connection conn = DBUtils.getConnection();

		try {
			stmt = conn.prepareStatement(SQLQueriesConstant.DROP_COURSE_QUERY);
			stmt.setString(1, courseCode);
			stmt.setString(2, studentId);
			stmt.execute();

			stmt = conn
					.prepareStatement(SQLQueriesConstant.INCREMENT_SEAT_QUERY);
			stmt.setString(1, courseCode);
			stmt.execute();

			stmt.close();

			return true;
		} catch (Exception e) {
			logger.error("Exception found" + e.getMessage());
		} finally {

			stmt.close();
			conn.close();
		}

		return false;

	}

	/*
	 * throws SQLException
	 */

	@Override
	public double calculateFee(String studentId) throws SQLException {
		Connection conn = DBUtils.getConnection();
		double fee = 0;
		try {
			stmt = conn.prepareStatement(SQLQueriesConstant.CALCULATE_FEES);
			stmt.setString(1, studentId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			fee = rs.getDouble(1);
		} catch (SQLException e) {
			logger.error(e.getErrorCode());
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}

		return fee;
	}

	@Override
	public List<Grade> viewGradeCard(String studentId) throws SQLException {

		Connection conn = DBUtils.getConnection();
		List<Grade> grade_List = new ArrayList<Grade>();
		try {
			stmt = conn.prepareStatement(SQLQueriesConstant.VIEW_GRADE);
			stmt.setString(1, studentId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String courseCode = rs.getString("courseCode");
				String courseName = rs.getString("courseName");
				String grade = rs.getString("grade");
				Grade obj = new Grade(courseCode, courseName, grade);
				grade_List.add(obj);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			stmt.close();
			conn.close();

		}

		return grade_List;
	}

	/*
	 * throws SQLException
	 */
	@Override
	public List<Course> viewCourses(String studentId) throws SQLException {

		List<Course> availableCourseList = new ArrayList<>();
		Connection conn = DBUtils.getConnection();

		try {
			stmt = conn
					.prepareStatement(SQLQueriesConstant.VIEW_AVAILABLE_COURSES);
			stmt.setString(1, studentId);
			// stmt.setBoolean(2, true);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				availableCourseList.add(new Course(rs.getString("courseCode"),
						rs.getString("courseName"),
						rs.getString("professorId"), rs.getInt("seats")));

			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}

		return availableCourseList;

	}

	/*
	 * throws SQLException
	 */
	@Override
	public List<Course> viewRegisteredCourses(String studentId)
			throws SQLException {

		Connection conn = DBUtils.getConnection();
		List<Course> registeredCourseList = new ArrayList<>();
		try {
			stmt = conn
					.prepareStatement(SQLQueriesConstant.VIEW_REGISTERED_COURSES);
			stmt.setString(1, studentId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				registeredCourseList.add(new Course(rs.getString("courseCode"),
						rs.getString("courseName"),
						rs.getString("professorId"), rs.getInt("seats")));

			}
		} catch (SQLException e) {
			logger.error(e.getMessage());

		} finally {
			stmt.close();
			conn.close();
		}

		return registeredCourseList;
	}

	/* throws SQLException */
	@Override
	public boolean getRegistrationStatus(String studentId) throws SQLException {
		Connection conn = DBUtils.getConnection();
		boolean status = false;
		try {
			stmt = conn
					.prepareStatement(SQLQueriesConstant.GET_REGISTRATION_STATUS);
			stmt.setString(1, studentId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			status = rs.getBoolean(1);

		} catch (SQLException e) {
			logger.error(e.getMessage());

		} finally {
			stmt.close();
			conn.close();
		}

		return status;
	}

	/*
	 * throws SQLException
	 */
	@Override
	public void setRegistrationStatus(String studentId) throws SQLException {
		Connection conn = DBUtils.getConnection();
		try {
			stmt = conn
					.prepareStatement(SQLQueriesConstant.SET_REGISTRATION_STATUS);
			stmt.setString(1, studentId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.error(e.getMessage());

		} finally {
			stmt.close();
			conn.close();
		}

	}

	@Override
	public boolean isReportGenerated(String studentId) throws SQLException {
		Connection conn = DBUtils.getConnection();
		boolean status = false;
		try {
			stmt = conn
					.prepareStatement(SQLQueriesConstant.GET_GENERATED_REPORT_CARD_TRUE);
			stmt.setString(1, studentId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			status = rs.getBoolean(1);
			// System.out.println(status);
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		} finally {
			stmt.close();
			conn.close();
		}

		return status;
	}

	@Override
	public boolean getPaymentStatus(String studentId) throws SQLException {
		{
			Connection conn = DBUtils.getConnection();
			boolean status = false;
			try {
				stmt = conn
						.prepareStatement(SQLQueriesConstant.GET_PAYMENT_STATUS);
				stmt.setString(1, studentId);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				status = rs.getBoolean(1);
				// System.out.println(status);
			} catch (SQLException e) {
				logger.error(e.getMessage());

			} finally {
				stmt.close();
				conn.close();
			}

			return status;
		}

	}

	@Override
	public void setPaymentStatus(String studentId) throws SQLException {
		Connection conn = DBUtils.getConnection();
		try {
			stmt = conn.prepareStatement(SQLQueriesConstant.SET_PAYMENT_STATUS);
			stmt.setString(1, studentId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			logger.error(e.getMessage());

		} finally {
			stmt.close();
			conn.close();
		}

	}

	@Override
	public int checkCourseAvailability(int studentId, String courseCode)
			throws SQLException {

		Connection conn = DBUtils.getConnection();

		int count = 0;
		try {

			stmt = conn
					.prepareStatement(SQLQueriesConstant.CHECK_COURSE_AVAILABILITY);
			stmt.setInt(1, studentId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (courseCode.equals(rs.getString("courseCode")))
					return 1;
				count++;
			}

			if (count >= 6)
				return 0;

		} catch (SQLException se) {

			logger.error(se.getMessage());

		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}

		return 2;
	}

	@Override
	public int checkCourseAvailability(String studentId, String courseCode)
			throws SQLException {
		Connection conn = DBUtils.getConnection();

		int count = 0;
		try {

			stmt = conn
					.prepareStatement(SQLQueriesConstant.CHECK_COURSE_AVAILABILITY);
			stmt.setString(1, studentId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (courseCode.equals(rs.getString("courseCode")))
					return 1;
				count++;
			}

			if (count >= 6)
				return 0;

		} catch (SQLException se) {

			logger.error(se.getMessage());

		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}

		return 2;

	}

	@Override
	public Notification payFee(String studentId, PaymentModeConstant mode,
			double fee) throws SQLException {

		Connection conn = null;
		UUID uuid = UUID.randomUUID();
		Notification notify = null;

		logger.info(studentId + mode.toString() + uuid.toString());

		try {

			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement(SQLQueriesConstant.INSERT_PAYMENT);
			stmt.setString(1, studentId);
			stmt.setString(2, mode.toString());
			stmt.setString(3, uuid.toString());
			stmt.setDouble(4, fee);
			stmt.executeUpdate();

			stmt = conn
					.prepareStatement(SQLQueriesConstant.INSERT_NOTIFICATION);
			stmt.setString(1, studentId);
			stmt.setString(2, NotificationTypeConstant.PAYED.toString());
			stmt.setString(3, uuid.toString());
			stmt.executeUpdate();

			stmt = conn.prepareStatement(SQLQueriesConstant.GET_NOTIFICATION);
			stmt.setString(1, uuid.toString());

			ResultSet rs = stmt.executeQuery();
			rs.next();
			notify = new Notification();
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}

		return notify;

	}
}