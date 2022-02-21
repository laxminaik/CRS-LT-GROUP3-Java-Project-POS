/**
 * 
 */
package com.lt.Application;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.lt.bean.Course;
import com.lt.bean.Grade;
import com.lt.bean.Professor;
import com.lt.bean.RegisteredCourse;
import com.lt.bean.Student;
import com.lt.constant.GenderConstant;
import com.lt.constant.NotificationTypeConstant;
import com.lt.constant.RoleConstant;
import com.lt.exception.CourseExistsAlreadyException;
import com.lt.exception.CourseNotDeletedException;
import com.lt.exception.CourseNotFoundException;
import com.lt.exception.ProfessorNotAddedException;
import com.lt.exception.StudentNotFoundForApprovalException;
import com.lt.exception.UserIdAlreadyInUseException;
import com.lt.exception.UserNotFoundException;
import com.lt.service.AdminInterface;
import com.lt.service.AdminOperation;
import com.lt.service.NotificationInterface;
import com.lt.service.NotificationOperation;
import com.lt.service.RegistrationInterface;
import com.lt.service.RegistrationOperation;


public class AdminCRSMenu {

	AdminInterface adminOperation = AdminOperation.getInstance();
	Scanner in = new Scanner(System.in);
	NotificationInterface notificationInterface=NotificationOperation.getInstance();
	RegistrationInterface registrationInterface = RegistrationOperation.getInstance();
	
	/**
	 * Method to Create Admin Menu
	 */
	public void createMenu(){
		
		while(CRSApplication.loggedin) {
			
			System.out.println("Admin Menu");
			System.out.println(" ");
			System.out.println("1. View course catalog");
			System.out.println("2. Add Course to catalog");
			System.out.println("3. Delete Course from catalog");
			System.out.println("4. Approve Students");
			System.out.println("5. View Pending Approvals");
			System.out.println("6. Add Professor");
			System.out.println("7. Assign Professor To Courses");
			System.out.println("8. Generate Report Card");
			System.out.println("9. Logout");
			
			
			int choice = in.nextInt();
			in.nextLine();
			
			switch(choice) {
			case 1:
				viewCoursesInCatalogue();
				break;
				
			case 2:
				addCourseToCatalogue();
				break;
				
			case 3:
				removeCourse();
				break;
				
			case 4:
				approveStudent();
				break;
			
			case 5:
				viewPendingAdmissions();
				break;
			
			case 6:
				addProfessor();
				break;
			
			case 7:
				assignCourseToProfessor();
				break;
				
			case 8:
				generateReportCard();
				break;
			
			case 9:
				CRSApplication.loggedin = false;
				return;

			default:
				System.out.println("Wrong selections ");
			}
		}
	}
	
	private void generateReportCard() 
	{
		
		List<Grade> grade_card=null;
		boolean isReportGenerated = true;
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter the Student Id to generate report card : ");
		String studentId = in.nextLine();
		
		try 
		{
			adminOperation.setGeneratedReportCardTrue(studentId);
			if(isReportGenerated) {
				grade_card = registrationInterface.viewGradeCard(studentId);
				System.out.println(String.format("%-20s %-30s %-20s","COURSE CODE", "COURSE NAME", "GRADE"));
				
				if(grade_card.isEmpty())
				{
					System.out.println("You haven't registered for any course");
					return;
				}
				
				grade_card.forEach(obj->System.out.println(String.format("%-20s %-30s %-20s",obj.getCrsCode(), obj.getCrsName(),obj.getGrade())));
			}
			else
				System.out.println("Report card not yet generated");
		} 
		catch (SQLException e) 
		{

			System.out.println(e.getMessage());
		}
		
		
	}

	

	/**
	 * Method to assign Course to a Professor
	 */
	private void assignCourseToProfessor() {
		List<Professor> professorList= adminOperation.viewProfessors();
		System.out.println("*************************** Professor *************************** ");
		System.out.println(String.format("%20s | %30s | %20s ", "ProfessorId", "Name", "Designation"));

	
		professorList.forEach(professor->System.out.println(String.format("%20s | %30s | %20s ", professor.getUserId(), professor.getName(), professor.getDesignation())));
		
		System.out.println("\n\n");
		List<Course> courseList= adminOperation.viewCourses();
		System.out.println("**************** Course ****************");
		System.out.println(String.format("%20s | %30s | %20s", "CourseCode", "CourseName", "ProfessorId"));

		courseList.forEach(course->System.out.println(String.format("%20s | %30s | %20s", course.getCourseCode(), course.getCourseName(), course.getInstructorId())));
		
		System.out.println("Enter Course Code:");
		String courseCode = in.nextLine();
		
		System.out.println("Enter Professor's User Id:");
		String userId = in.nextLine();
		
		try {
			
			adminOperation.assignCourse(courseCode, userId);
		
		}
		catch(CourseNotFoundException | UserNotFoundException  e) {
			
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * Method to add Professor to DB
	 */
	private void addProfessor() {
		
		System.out.println("Enter User Id(integer):");
		String userId = in.nextLine();
		Professor professor = new Professor(userId);
		
		System.out.println("Enter Professor Name:");
		String professorName = in.nextLine();
		professor.setName(professorName);
		
		System.out.println("Enter Department:");
		String department = in.nextLine();
		professor.setDepartment(department);
		
		System.out.println("Enter Designation:");
		String designation = in.nextLine();
		professor.setDesignation(designation);
		
		System.out.println("Enter Password:");
		String password = in.nextLine();
		professor.setPassword(password);
		
		System.out.println("Enter Gender: \t 1: Male \t 2.Female \t 3.Other ");
		int gender = in.nextInt();
		in.nextLine();
		
		if(gender==1)
			professor.setGender(GenderConstant.MALE);
		else if(gender==2)
			professor.setGender(GenderConstant.FEMALE);
		else if(gender==3)
			professor.setGender(GenderConstant.OTHER);
		
		System.out.println("Enter Address:");
		String address = in.nextLine();
		professor.setAddress(address);
		
		professor.setRole(RoleConstant.PROFESSOR);
		
		try {
			adminOperation.addProfessor(professor);
		} catch (ProfessorNotAddedException | UserIdAlreadyInUseException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Method to view Students who are yet to be approved
	 * @return List of Students whose admissions are pending
	 */
	private List<Student> viewPendingAdmissions() {
		
		List<Student> pendingStudentsList= adminOperation.viewPendingAdmissions();
		if(pendingStudentsList.size() == 0) {
			System.out.println("No students pending approvals");
			return pendingStudentsList;
		}
		System.out.println(String.format("%20s | %30s | %20s", "StudentId", "Name", "Gender"));
		for(Student student : pendingStudentsList) {
			System.out.println(String.format("%20s | %30s | %20s", student.getStudentId(), student.getName(), student.getGender().toString()));
		}
		return pendingStudentsList;
	}

	/**
	 * Method to approve a Student using Student's ID
	 */
	private void approveStudent() {
		
		List<Student> studentList = viewPendingAdmissions();
		if(studentList.size() == 0) {
			
			
			return;
		}
		
		System.out.println("Enter Student's ID:");
		String studentUserIdApproval = in.nextLine();
		
		
		try {
			adminOperation.approveStudent(studentUserIdApproval, studentList);
			System.out.println("\nStudent Id : " +studentUserIdApproval+ " has been approved\n");
			//send notification from system
			notificationInterface.sendNotification(NotificationTypeConstant.REGISTRATION, studentUserIdApproval, null,0);
	
		} catch (StudentNotFoundForApprovalException e) {
			System.out.println(e.getMessage());
		}
	
		
	}

	/**
	 * Method to delete Course from catalogue
	 * @throws CourseNotFoundException 
	 */
	private void removeCourse() {
		List<Course> courseList = viewCoursesInCatalogue();
		System.out.println("Enter Course Code:");
		String courseCode = in.nextLine();
		
		try {
			adminOperation.removeCourse(courseCode, courseList);
			System.out.println("\nCourse Deleted : "+courseCode+"\n");
		} catch (CourseNotFoundException e) {
			
			System.out.println(e.getMessage());
		}
		catch (CourseNotDeletedException e) {
			
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Method to add Course to catalogue
	 * @throws CourseExistsAlreadyException 
	 */
	private void addCourseToCatalogue() {
		List<Course> courseList = viewCoursesInCatalogue();

		in.nextLine();
		System.out.println("Enter Course Code:");
		String courseCode = in.nextLine();
		
		System.out.println("Enter Course Name:");
		String courseName = in.nextLine();
		
		Course course = new Course(courseCode, courseName,"", 10);
		course.setCourseCode(courseCode);
		course.setCourseName(courseName);
		course.setSeats(10);
		
		try {
		adminOperation.addCourse(course, courseList);	
		System.out.println("Course added successfully!");
		}
		catch (CourseExistsAlreadyException e) {
			System.out.println("Course already existed "+e.getMessage());
		}

	}
	
	/**
	 * Method to display courses in catalogue
	 * @return List of courses in catalogue
	 */
	private List<Course> viewCoursesInCatalogue() {
		List<Course> courseList = adminOperation.viewCourses();
		if(courseList.size() == 0) {
			System.out.println("Nothing present in the catalogue!");
			return courseList;
		}
		System.out.println(String.format("%20s | %30s | %20s","COURSE CODE", "COURSE NAME", "INSTRUCTOR"));
		for(Course course : courseList) {
			System.out.println(String.format("%20s | %30s | %20s", course.getCourseCode(), course.getCourseName(), course.getInstructorId()));
		}
		return courseList;
	}
}
