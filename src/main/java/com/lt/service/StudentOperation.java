package com.lt.service;

//import com.flipkart.client.CRSApplication;
import com.lt.bean.SemRegistration;
import com.lt.bean.Student;
import com.lt.constant.GenderConstant;
import com.lt.constant.RoleConstant;
import com.lt.dao.StudentDaoInterface;
import com.lt.dao.StudentDaoOperation;
import com.lt.exception.StudentNotRegisteredException;

public class StudentOperation implements StudentInterface {

	private static volatile StudentOperation instance = null;

	StudentDaoInterface studentDaoInterface = StudentDaoOperation.getInstance();

	private StudentOperation() {

	}

	public static StudentOperation getInstance() {
		if (instance == null) {
			// This is a synchronized block, when multiple threads will access
			// this instance
			synchronized (StudentOperation.class) {
				instance = new StudentOperation();
			}
		}
		return instance;
	}

	@Override
	public String register(String name, String userId, String password,
			GenderConstant gender, int batch, String branch, String address)
			throws StudentNotRegisteredException {
		String studentId;
		try {
			// call the DAO class, and add the student record to the DB
			System.out.println("1 reg");
			Student newStudent = new Student(userId, name,
					RoleConstant.STUDENT, password, gender, address, branch,
					userId, batch, false);
			System.out
					.println("\nYour account has been created and pending for Approval by Admin.\n");
			studentId = studentDaoInterface.addStudent(newStudent);

		} catch (StudentNotRegisteredException ex) {
			throw ex;
		}
		return studentId;
	}

	@Override
	public String getStudentId(String userId) {

		return studentDaoInterface.getStudentId(userId);
	}

	@Override
	public boolean isApproved(String studentId) {
		return studentDaoInterface.isApproved(studentId);
	}

}