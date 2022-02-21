/**
 * 
 */
package com.lt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.lt.bean.Student;
import com.lt.constant.SQLQueriesConstant;
import com.lt.exception.StudentNotRegisteredException;
import com.lt.utils.DBUtils;

public class StudentDaoOperation implements StudentDaoInterface {

	private static volatile StudentDaoOperation instance = null;
	private static Logger logger = Logger.getLogger(StudentDaoOperation.class);

	private StudentDaoOperation() {

	}

	public static StudentDaoOperation getInstance() {
		if (instance == null) {
			synchronized (StudentDaoOperation.class) {
				instance = new StudentDaoOperation();
			}
		}
		return instance;
	}

	/*
	 * throws StudentNotRegisteredException
	 */
	@Override
	public String addStudent(Student student)
			throws StudentNotRegisteredException {
		Connection connection = DBUtils.getConnection();

		String studentId = null;
		try {

			PreparedStatement preparedStatement = connection
					.prepareStatement(SQLQueriesConstant.ADD_USER_QUERY);
			preparedStatement.setString(1, student.getUserId());
			preparedStatement.setString(2, student.getName());
			preparedStatement.setString(3, student.getPassword());
			preparedStatement.setString(4, student.getRole().toString());
			preparedStatement.setString(5, student.getGender().toString());
			preparedStatement.setString(6, student.getAddress());

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 1) {

				PreparedStatement preparedStatementStudent;
				preparedStatementStudent = connection.prepareStatement(
						SQLQueriesConstant.ADD_STUDENT,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatementStudent.setString(1, student.getUserId());
				preparedStatementStudent.setString(2, student.getDepartment());
				preparedStatementStudent.setInt(3, student.getGradYear());
				// preparedStatementStudent.setBoolean(4, true);
				preparedStatementStudent.executeUpdate();
				ResultSet results = preparedStatementStudent.getGeneratedKeys();
				if (results.next())
					studentId = results.getString(1);

			}

		} catch (Exception ex) {
			throw new StudentNotRegisteredException(ex.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.info(e.getMessage() + "SQL error");
				e.printStackTrace();
			}
		}
		return studentId;
	}

	@Override
	public String getStudentId(String userId) {
		Connection connection = DBUtils.getConnection();
		try {
			PreparedStatement statement = connection
					.prepareStatement(SQLQueriesConstant.GET_STUDENT_ID);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getString("studentId");
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public boolean isApproved(String studentId) {
		Connection connection = DBUtils.getConnection();
		try {
			PreparedStatement statement = connection
					.prepareStatement(SQLQueriesConstant.IS_APPROVED);
			statement.setString(1, studentId);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getBoolean("isApproved");
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

		return false;
	}

}