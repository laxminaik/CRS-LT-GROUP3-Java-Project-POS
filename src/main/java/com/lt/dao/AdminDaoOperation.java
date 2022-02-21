/**
 * 
 */
package com.lt.dao;

import java.util.ArrayList;  
import java.util.List;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lt.bean.Course;
import com.lt.bean.Professor;
import com.lt.bean.RegisteredCourse;
import com.lt.bean.Student;
import com.lt.bean.User;
import com.lt.constant.GenderConstant;
import com.lt.constant.RoleConstant;
import com.lt.constant.SQLQueriesConstant;
import com.lt.exception.CourseExistsAlreadyException;
import com.lt.exception.CourseNotDeletedException;
import com.lt.exception.CourseNotFoundException;
import com.lt.exception.ProfessorNotAddedException;
import com.lt.exception.StudentNotFoundForApprovalException;
import com.lt.exception.UserIdAlreadyInUseException;
import com.lt.exception.UserNotAddedException;
import com.lt.exception.UserNotFoundException;
import com.lt.utils.DBUtils;




public class AdminDaoOperation implements AdminDaoInterface{
	
	private static volatile AdminDaoOperation instance = null;
	private static Logger logger = Logger.getLogger(AdminDaoOperation.class);
	private PreparedStatement statement = null;
	
	
	private AdminDaoOperation(){}
	
	
	public static AdminDaoOperation getInstance()
	{
		if(instance == null)
		{
			synchronized(AdminDaoOperation.class){
				instance = new AdminDaoOperation();
			}
		}
		return instance;
	}
	
	Connection connection = DBUtils.getConnection();
	
	/*
	 
	 * @throws CourseNotFoundException
	 * @throws CourseNotDeletedException 
	 */
	@Override
	public void removeCourse(String courseCode) throws CourseNotFoundException, CourseNotDeletedException{
		
		statement = null;
		try {
			String sql = SQLQueriesConstant.DELETE_COURSE_QUERY;
			statement = connection.prepareStatement(sql);
			
			statement.setString(1,courseCode);
			int row = statement.executeUpdate();
			
			logger.info(row + " entries deleted.");
			if(row == 0) {
				logger.error(courseCode + " not in catalog!");
				throw new CourseNotFoundException(courseCode);
			}

			logger.info("Course with courseCode: " + courseCode + " deleted.");
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			throw new CourseNotDeletedException(courseCode);
		}
		
	}

	/*
	 
	 * @throws CourseFoundException
	 */
	@Override
	public void addCourse(Course course) throws CourseExistsAlreadyException{
		
		statement = null;
		try {
			
			String sql = SQLQueriesConstant.ADD_COURSE_QUERY;
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, course.getCourseCode());
			statement.setString(2, course.getCourseName());
			
			statement.setInt(3, 10);
			statement.setString(4, "NOT_Assigned");
			int row = statement.executeUpdate();
			
			logger.info(row + " course added");
			if(row == 0) {
				logger.error("Course with courseCode: " + course.getCourseCode() + "not added to catalog.");
				throw new CourseExistsAlreadyException(course.getCourseCode());
			}
			
			logger.info("Course with courseCode: " + course.getCourseCode() + " is added to catalog."); 
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			throw new CourseExistsAlreadyException(course.getCourseCode());
			
		}
		
	}
	
	
	@Override
	public List<Student> viewPendingAdmissions() {
		
		statement = null;
		List<Student> userList = new ArrayList<Student>();
		try {
			
			String sql = SQLQueriesConstant.VIEW_PENDING_ADMISSION_QUERY;
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			while(resultSet.next()) {
				
				Student user = new Student();
				user.setUserId(resultSet.getString(1));
				user.setName(resultSet.getString(2));
				user.setPassword(resultSet.getString(3));
				user.setRole(RoleConstant.stringToName(resultSet.getString(4)));
				user.setGender(GenderConstant.stringToGender( resultSet.getString(5)));
				user.setAddress(resultSet.getString(6));
				user.setStudentId(resultSet.getString(7));
				userList.add(user);
				
			}
			
			logger.info(userList.size() + " students have pending-approval.");
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			
		}
		
		return userList;
		
	}

	/*
	 throws StudentNotFoundException
	 */
	@Override
	public void approveStudent(String studentId) throws StudentNotFoundForApprovalException {
		
		statement = null;
		try {
			String sql = SQLQueriesConstant.APPROVE_STUDENT_QUERY;
			statement = connection.prepareStatement(sql);
			
			statement.setString(1,studentId);
			int row = statement.executeUpdate();
			
			logger.info(row + " student approved.");
			if(row == 0) {
				
				throw new StudentNotFoundForApprovalException(studentId);
			}
			
			logger.info("Student with studentId: " + studentId + " approved by admin.");
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			
		}
		
	}

	/*  throws UserIdAlreadyInUseException 
	 */
	@Override
	public void addUser(User user) throws UserNotAddedException, UserIdAlreadyInUseException{
		
		statement = null;
		try {
			
			String sql = SQLQueriesConstant.ADD_USER_QUERY;
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, user.getUserId());
			statement.setString(2, user.getName());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getRole().toString());
			statement.setString(5, user.getGender().toString());
			statement.setString(6, user.getAddress());
			
			int row = statement.executeUpdate();
			
			logger.info(row + " user added.");
			if(row == 0) {
				logger.error("User with userId: " + user.getUserId() + " not added.");
				throw new UserNotAddedException(user.getUserId()); 
			}

			logger.info("User with userId: " + user.getUserId() + " added."); 
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			throw new UserIdAlreadyInUseException(user.getUserId());
			
		}
		
	}

	/*
	 * @throws UserIdAlreadyInUseException 
	 * @throws ProfessorNotAddedException 
	 */
	@Override
	public void addProfessor(Professor professor) throws UserIdAlreadyInUseException, ProfessorNotAddedException {
		
		try {
			
			this.addUser(professor);
			
		}catch (UserNotAddedException e) {
			
			logger.error(e.getMessage());
			throw new ProfessorNotAddedException(professor.getUserId());
			
		}catch (UserIdAlreadyInUseException e) {
			
			logger.error(e.getMessage());
			throw e;
			
		}
		
		
		statement = null;
		try {
			
			String sql = SQLQueriesConstant.ADD_PROFESSOR_QUERY;
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, professor.getUserId());
			statement.setString(2, professor.getDepartment());
			statement.setString(3, professor.getDesignation());
			int row = statement.executeUpdate();

			logger.info(row + " professor added.");
			if(row == 0) {
				logger.error("Professor with professorId: " + professor.getUserId() + " not added.");
				throw new ProfessorNotAddedException(professor.getUserId());
			}
			
			logger.info("Professor with professorId: " + professor.getUserId() + " added."); 
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			throw new UserIdAlreadyInUseException(professor.getUserId());
			
		} 
		
	}
	
	/*
	 * @throws CourseNotFoundException
	 * @throws UserNotFoundException 
	 */
	@Override
	public void assignCourse(String courseCode, String professorId) throws CourseNotFoundException, UserNotFoundException{
		
		statement = null;
		try {
			String sql = SQLQueriesConstant.ASSIGN_COURSE_QUERY;
			statement = connection.prepareStatement(sql);
			
			statement.setString(1,professorId);
			statement.setString(2,courseCode);
			int row = statement.executeUpdate();
			
			logger.info(row + " course assigned.");
			if(row == 0) {
				logger.error(courseCode + " not found");
				throw new CourseNotFoundException(courseCode);
			}
			
			logger.info("Course with courseCode: " + courseCode + " is assigned to professor with professorId: " + professorId + ".");
		
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			throw new UserNotFoundException(professorId);
			
		}
		
	}
	
	
	public List<Course> viewCourses() {
		
		statement = null;
		List<Course> courseList = new ArrayList<>();
		try {
			
			String sql = SQLQueriesConstant.VIEW_COURSE_QUERY;
			statement = connection.prepareStatement(sql);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				
				Course course = new Course();
				course.setCourseCode(resultSet.getString(1));
				course.setCourseName(resultSet.getString(2));
				course.setInstructorId(resultSet.getString(3));
				courseList.add(course);
				
			}
			
			logger.info("Number of courses in the Catalog are : " + courseList.size());
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			
		}
		
		return courseList; 
		
	}
	
	
	@Override
	public List<Professor> viewProfessors() {
		
		statement = null;
		List<Professor> professorList = new ArrayList<Professor>();
		try {
			
			String sql = SQLQueriesConstant.VIEW_PROFESSOR_QUERY;
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				
				Professor professor = new Professor();
				professor.setUserId(resultSet.getString(1));
				professor.setName(resultSet.getString(2));
				professor.setGender(GenderConstant.stringToGender(resultSet.getString(3)));
				professor.setDepartment(resultSet.getString(4));
				professor.setDesignation(resultSet.getString(5));
				professor.setAddress(resultSet.getString(6));
				professor.setRole(RoleConstant.PROFESSOR);
				professor.setPassword("*********");
				professorList.add(professor);
				
			}
			
			logger.info(professorList.size() + " professors in the institute.");
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			
		}
		return professorList;
	}
	
	public void setGeneratedReportCardTrue(String Studentid)
	{
		String sql1 = SQLQueriesConstant.SET_GENERATED_REPORT_CARD_TRUE;
		try {
		statement = connection.prepareStatement(sql1);
		statement.setString(1, Studentid);
		int row = statement.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error(e.getMessage());
		}
	}

	@Override
	public List<RegisteredCourse> generateGradeCard(String Studentid) 
	{
		List<RegisteredCourse> CoursesOfStudent = new ArrayList<RegisteredCourse>();
		
		try {
					String sql = SQLQueriesConstant.VIEW_REGISTERED_COURSES;
					statement = connection.prepareStatement(sql);
					statement.setString(1, Studentid);
					ResultSet resultSet = statement.executeQuery();
					
					while(resultSet.next()) {
						
						Course course = new Course();
						RegisteredCourse temp = new RegisteredCourse() ;
						course.setCourseCode(resultSet.getString(1));
						course.setCourseName(resultSet.getString(2));
						course.setInstructorId(resultSet.getString(3));
						course.setSeats(resultSet.getInt(4));
						
						
						temp.setCourse(course);
						System.out.println("course object generated");
						temp.setstudentId(Studentid);
						
						
						temp.setGrade(resultSet.getString(8));
						
						System.out.println("graded");
						CoursesOfStudent.add(temp);
						
					}
					
					String sql1 = SQLQueriesConstant.SET_GENERATED_REPORT_CARD_TRUE;
					statement = connection.prepareStatement(sql1);
					statement.setString(1, Studentid);
					int row = statement.executeUpdate();
						
					
				}catch(SQLException se) {
					
					logger.error(se.getMessage());
					
				}
		
		return CoursesOfStudent;
		
		
	}

	@Override
	public void deleteCourse(String courseCode) throws CourseNotFoundException {
		statement = null;
		try {
			String sql = SQLQueriesConstant.DELETE_COURSE_QUERY;
			statement = connection.prepareStatement(sql);
			
			statement.setString(1,courseCode);
			int row = statement.executeUpdate();
			
			logger.info(row + " entries deleted.");
			if(row == 0) {
				logger.error(courseCode + " not in catalog!");
				throw new CourseNotFoundException(courseCode);
			}

			logger.info("Course with courseCode: " + courseCode + " deleted.");
			
		}catch(SQLException se) {
			
			logger.error(se.getMessage());
			
		}
		
	}

	
	
	

}
