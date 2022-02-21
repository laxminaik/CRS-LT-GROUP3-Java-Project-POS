/**
 * 
 */
package com.lt.Application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.lt.constant.GenderConstant;
import com.lt.constant.NotificationTypeConstant;
import com.lt.exception.StudentNotRegisteredException;
import com.lt.exception.UserNotFoundException;
import com.lt.service.NotificationInterface;
import com.lt.service.NotificationOperation;
import com.lt.service.StudentInterface;
import com.lt.service.StudentOperation;
import com.lt.service.UserInterface;
import com.lt.service.UserOperation;

public class CRSApplication {
	public static final String ANSI_RESET ="\u001B[0m";
	public static final String ANSI_GREEN ="\u001B[32m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_CYAN = "\u001B[46m";
	public static final String ANSI_BLUE = "\u001B[34m";
	
	static boolean loggedin = false;
	StudentInterface studentInterface = StudentOperation.getInstance();
	UserInterface userInterface = UserOperation.getInstance();
	NotificationInterface notificationInterface = NotificationOperation
			.getInstance();

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		CRSApplication crsApplication = new CRSApplication();
		int userInput;
		/* main menu */
		createMainMenu();
		userInput = sc.nextInt();

		try {

			while (userInput != 4) {
				switch (userInput) {
				case 1:

					crsApplication.loginUser();
					break;
				case 2:

					crsApplication.registerStudent();
					break;
				case 3:
					crsApplication.updatePassword();
					break;
				default:
					System.out.println("Invalid user Input");
				}
				createMainMenu();
				userInput = sc.nextInt();
			}
		} catch (Exception e) {
			System.out.println("Error occured " + e);
		} finally {
			sc.close();
		}
	}

	public static void createMainMenu() {
		System.out.println(ANSI_CYAN+"                Welcome to CRS APPLICATION              "+ANSI_RESET);
		System.out.println("*********************************************************");
		System.out.println(ANSI_GREEN+"1. Login"+ANSI_RESET);
		System.out.println(ANSI_GREEN+"2. Student Registration"+ANSI_RESET);
		System.out.println(ANSI_GREEN+"3. Update password"+ANSI_RESET);
		System.out.println(ANSI_GREEN+"4. Exit"+ANSI_RESET);
		System.out.println("*********************************************************");
		System.out.println(ANSI_BLUE+"Enter user input"+ANSI_RESET);
	}

	public void loginUser() {

		// invalid credential exception
		// user not found exception
		// user not approved exception
		Scanner in = new Scanner(System.in);

		String userId = "abc", password = "root";
		try {
			System.out.println("-----Login-----");
			System.out.println("UserId:");
			userId = in.nextLine();
			System.out.println("Password:");
			password = in.nextLine();
			loggedin = userInterface.verifyCredentials(userId, password);

			System.out.println("");
			if (loggedin) {

				DateTimeFormatter myFormatObj = DateTimeFormatter
						.ofPattern("dd-MM-yyyy HH:mm:ss");

				LocalDateTime myDateObj = LocalDateTime.now();

				String formattedDate = myDateObj.format(myFormatObj);

				String role = userInterface.getRole(userId);

				switch (role) {
				case "ADMIN":
					System.out.println("Login Successful at "+formattedDate+"\n");
					AdminCRSMenu adminMenu = new AdminCRSMenu();
					adminMenu.createMenu();
					break;
				case "PROFESSOR":
					System.out.println(formattedDate + " Login Successful");
					ProfessorCRSMenu professorMenu = new ProfessorCRSMenu();
					professorMenu.createMenu(userId);

					break;
				case "STUDENT":

					String studentId = userId;
					boolean isApproved = studentInterface.isApproved(studentId);
					if (isApproved) {
						System.out.println(formattedDate + " Login Successful");
						StudentCRSMenu studentMenu = new StudentCRSMenu();
						studentMenu.create_menu(studentId);

					} else {
						System.out
								.println("Failed to login, you have not been approved by the admin!");
						loggedin = false;
					}
					break;
				}

			} else {
				System.out.println("Invalid Credentials!");
			}

		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
		}

	}

	public void registerStudent() {
		Scanner sc = new Scanner(System.in);

		String userId, name, password, address, branchName;
		GenderConstant gender;
		int genderV, batch;
		try {
			// input the student details
			System.out.println("Student Registration");
			System.out.println("Name:");
			name = sc.nextLine();
			System.out.println("Email:");
			userId = sc.nextLine();
			System.out.println("Password:");
			password = sc.nextLine();
			System.out
					.println("Gender : \t 1: Male \t 2.Female\t 3.Other");
			genderV = sc.nextInt();
			sc.nextLine();

			switch (genderV) {
			case 1:
				gender = GenderConstant.MALE;
				break;
			case 2:
				gender = GenderConstant.FEMALE;
				break;

			case 3:
				gender = GenderConstant.OTHER;
				break;
			default:
				gender = GenderConstant.OTHER;
			}

			System.out.println("Branch:");
			branchName = sc.nextLine();
			System.out.println("Batch:");
			batch = sc.nextInt();
			sc.nextLine();
			System.out.println("Address:");
			address = sc.nextLine();

			String newStudentId = studentInterface.register(name, userId,
					password, gender, batch, branchName, address);

		} catch (StudentNotRegisteredException ex) {
			System.out.println("Something is wrong! " + ex.getStudentName()
					+ " not registered. Please try again later");
		}
		// sc.close();
	}

	public void updatePassword() {
		Scanner in = new Scanner(System.in);
		String userId, newPassword;
		try {
			System.out.println("Update Password");
			System.out.println("Email");
			userId = in.nextLine();
			System.out.println("New Password:");
			newPassword = in.nextLine();
			boolean isUpdated = userInterface.updatePassword(userId,
					newPassword);
			if (isUpdated)
				System.out.println("Password updated successfully!");

			else
				System.out.println("Something went wrong, please try again!");
		} catch (Exception ex) {
			System.out.println("Error Occured " + ex.getMessage());
		}

	}

}
