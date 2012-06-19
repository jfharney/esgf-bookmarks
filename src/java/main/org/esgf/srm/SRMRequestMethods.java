package org.esgf.srm;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
 
public class SRMRequestMethods {
 
	public static void main(String[] argv) {
 
		Properties properties = new Properties();
		
		try {
		    //properties.load(new FileInputStream("/Users/8xo/esgProjects/esgf-bookmarks/src/java/main/org/esgf/properties/filename.properties"));
		    properties.load(new FileInputStream("filename.properties"));
		    
		    
		} catch (IOException e) {
			System.out.println("couldn't find properties file");
		}
		
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");
 
		try {
 
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;
 
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		Connection connection = null;
 
		try {
			String db = properties.getProperty("database_name");
			String userName = properties.getProperty("user_name");
			String userPass = properties.getProperty("user_password");
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + db, userName,
					userPass);
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		
		
		
		
		String insertString1;
		insertString1 = "insert into phonebook values('63423', 'Hemanth')";
		
		Statement stmt;
		
		try {

			stmt = connection.createStatement();
			stmt.executeUpdate(insertString1);
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 
}