/**
 *
 */
package com.lt.service;

import java.util.UUID;

import com.lt.constant.NotificationTypeConstant;
import com.lt.constant.PaymentModeConstant;

public interface NotificationInterface {

	public int sendNotification(NotificationTypeConstant type,
			String studentId, PaymentModeConstant modeOfPayment, double amount);

	UUID getReferenceId(int notificationId);

	int sendNotification(NotificationTypeConstant type, int studentId,
			PaymentModeConstant modeOfPayment, double amount);

}