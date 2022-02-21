package com.lt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.lt.constant.SQLQueriesConstant;
import com.lt.exception.UserNotFoundException;
import com.lt.utils.DBUtils;

public class UserDaoOperation implements UserDaoInterface {
	private static volatile UserDaoOperation instance = null;
	private static Logger logger = Logger.getLogger(UserDaoOperation.class);

	private UserDaoOperation() {

	}

	public static UserDaoOperation getInstance() {
		if (instance == null) {
			synchronized (UserDaoOperation.class) {
				instance = new UserDaoOperation();
			}
		}
		return instance;
	}

	@Override
	public boolean updatePassword(String userId, String newPassword) {
		Connection connection = DBUtils.getConnection();
		try {
			System.out.println("2 updated");
			PreparedStatement statement = connection
					.prepareStatement(SQLQueriesConstant.UPDATE_PASSWORD);

			statement.setString(1, newPassword);
			statement.setString(2, userId);

			int row = statement.executeUpdate();

			if (row == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/*
	 * throws UserNotFoundException
	 */
	@Override
	public boolean verifyCredentials(String userId, String password)
			throws UserNotFoundException {
		Connection connection = DBUtils.getConnection();

		//String userId1="abc";
		try {

			PreparedStatement preparedStatement = connection
					.prepareStatement(SQLQueriesConstant.VERIFY_CREDENTIALS);
			preparedStatement.setString(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new UserNotFoundException(userId);

			else if (password.equals(resultSet.getString(1))) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException ex) {
			logger.error("Something went wrong, please try again! "
					+ ex.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.debug("inside finally catch");
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean updatePassword(String userID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getRole(String userId) {
		
		Connection connection = DBUtils.getConnection();
		

		try {
			logger.info(userId);
			// connection=DBUtils.getConnection();
			
			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstant.GET_ROLE);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();

			logger.info("query executed");

			if (rs.next()) {
				logger.info(rs.getString("role"));
				return rs.getString("role");
			}

		} catch (SQLException e) {
			
			logger.error("sql exception "+e);
		}
		catch (Exception e) {
			logger.error(e.getMessage());

		}

		finally {
			try {
				connection.close();
			} catch (SQLException e) {

			}
		}
		return null;
	}

}