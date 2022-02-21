package com.lt.constant;


public enum RoleConstant {
	ADMIN,PROFESSOR,STUDENT;
	

	@Override
	public String toString()
	{
		final String name=name();
		return name; 
	}
	
	
	public static RoleConstant stringToName(String role)
	{
		RoleConstant userRole=null;

		if(role.equalsIgnoreCase("ADMIN"))
			userRole=RoleConstant.ADMIN;
		else if(role.equalsIgnoreCase("PROFESSOR"))
			userRole=RoleConstant.PROFESSOR;
		else if(role.equalsIgnoreCase("STUDENT"))
			userRole=RoleConstant.STUDENT;
		return userRole;
	}
}