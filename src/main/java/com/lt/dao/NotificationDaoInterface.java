/**
 * 
 */
package com.lt.dao;

import java.sql.SQLException;

import com.lt.constant.NotificationTypeConstant;
import com.lt.constant.PaymentModeConstant;


public interface NotificationDaoInterface {


	public int sendNotification(NotificationTypeConstant type,int studentId,PaymentModeConstant modeOfPayment,double amount) throws SQLException;
	
}
