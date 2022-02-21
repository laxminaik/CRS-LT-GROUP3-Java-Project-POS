/**
 * 
 */
package com.lt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.lt.constant.NotificationTypeConstant;
import com.lt.constant.PaymentModeConstant;
import com.lt.constant.SQLQueriesConstant;
import com.lt.utils.DBUtils;


public class NotificationDaoOperation implements NotificationDaoInterface{

	
	private static volatile NotificationDaoOperation instance=null;
	private static Logger logger = Logger.getLogger(NotificationDaoOperation.class);

	
	private NotificationDaoOperation()
	{

	}
	

	public static NotificationDaoOperation getInstance()
	{
		if(instance==null)
		{
			// This is a synchronized block, when multiple threads will access this instance
			synchronized(NotificationDaoOperation.class){
				instance=new NotificationDaoOperation();
			}
		}
		return instance;
	}
	
	
	@Override
	public int sendNotification(NotificationTypeConstant type, int studentId,PaymentModeConstant modeOfPayment,double amount) throws SQLException{
		int notificationId=0;
		Connection connection=DBUtils.getConnection();
		try
		{
			//INSERT_NOTIFICATION = "insert into notification(studentId,type,referenceId) values(?,?,?);";
			PreparedStatement ps = connection.prepareStatement(SQLQueriesConstant.INSERT_NOTIFICATION,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, studentId);
			ps.setString(2,type.toString());
			if(type==NotificationTypeConstant.PAYED)
			{
				//insert into payment, get reference id and add here
				UUID referenceId=addPayment(studentId, modeOfPayment,amount);
				ps.setString(3, referenceId.toString());	
			}
			else
				ps.setString(3,"");
				
			ps.executeUpdate();
			ResultSet results=ps.getGeneratedKeys();
			if(results.next())
				notificationId=results.getInt(1);
			
			switch(type)
			{
			case REGISTRATION:
				System.out.println("Registration successfull. Administration will verify the details and approve it!");
				break;
			case APPROVED:
				System.out.println("Student with id "+studentId+" has been approved!");
				break;
			case PAYED:
				System.out.println("Student with id "+studentId+" fee has been paid");
			}
			
		}
		catch(SQLException ex)
		{
			throw ex;
		}
		return notificationId;
	}

	
	public UUID addPayment(int studentId, PaymentModeConstant modeOfPayment,double amount) throws SQLException
	{
		UUID referenceId;
		Connection connection=DBUtils.getConnection();
		try
		{
			referenceId=UUID.randomUUID();
			//INSERT_NOTIFICATION = "insert into notification(studentId,type,referenceId) values(?,?,?);";
			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstant.INSERT_PAYMENT);
			statement.setInt(1, studentId);
			statement.setString(2, modeOfPayment.toString());
			statement.setString(3,referenceId.toString());
			statement.setDouble(4, amount);
			statement.executeUpdate();
			//check if record is added
		}
		catch(SQLException ex)
		{
			throw ex;
		}
		return referenceId;
	}
	
}
