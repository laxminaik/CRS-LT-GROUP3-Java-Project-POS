package com.lt.constant;


public enum PaymentModeConstant {
	
	CREDIT_CARD,NET_BANKING,DEBIT_CARD;
	
	
	public static PaymentModeConstant getPaymentMode(int value)
	{
		switch(value)
		{
			case 1:
				return PaymentModeConstant.CREDIT_CARD;
			case 2:
				return PaymentModeConstant.NET_BANKING;
			case 3:
				return PaymentModeConstant.DEBIT_CARD;
			default:
				return null;
				
		}
			
	}
	
}