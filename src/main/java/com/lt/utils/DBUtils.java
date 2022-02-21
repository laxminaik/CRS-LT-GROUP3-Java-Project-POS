
package com.lt.utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DBUtils {

	private static Connection connection = null;

	public static Connection getConnection() {
        
		 
		try {
			if (connection != null && !connection.isClosed()) {
				
				return connection;
			} else {
				try {
					
					Properties prop = new Properties();
					InputStream inputStream = DBUtils.class.getClassLoader()
							.getResourceAsStream("./config.properties");
					prop.load(inputStream);
					String driver = prop.getProperty("driver");
					String url = prop.getProperty("url");
					String user = prop.getProperty("user");
					String password = prop.getProperty("password");
					Class.forName(driver);
					connection = DriverManager.getConnection(url, user, password);
					
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return connection;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return connection;
		}
	}
}

