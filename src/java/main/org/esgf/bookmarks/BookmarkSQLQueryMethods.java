package org.esgf.bookmarks;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class BookmarkSQLQueryMethods {

	public static String propertiesFile = "filename.properties";
	
	public static void issueUpdateQuery(DBConnection dbConnection, String query) {
		
		Connection connection = dbConnection.getCon();
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream(propertiesFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch(SQLException ex) {
			
			//need a better catch here
			dbConnection.createNonExistantTable(connection);
			
			try {
				stmt = connection.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   		
		}
		
	}
	
	public static ResultSet issueSelectQuery(DBConnection dbConnection, String query) {
		
		ResultSet rs = null;	
		Connection connection = dbConnection.getCon();
		PreparedStatement prest;
		try {
			prest = connection.prepareStatement(query);
			rs = prest.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	
}
