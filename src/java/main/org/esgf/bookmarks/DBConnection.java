package org.esgf.bookmarks;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnection {

	//public static Statement stmt;
	private Connection con;
	

	private String db;
	private String userid;
	private String password;
	private String psqlStr;
	private String tableName;
	private String tableSchema;
	
	
	
	public DBConnection () {
		this.db = "";
		this.userid = "";
		this.password = "";
		this.psqlStr = "";
		this.tableName = "";
		this.tableSchema = "";
	}
	
	public DBConnection(String propertiesFile) {
		
		Properties properties = new Properties();
		
		try {
			
			properties.load(new FileInputStream(propertiesFile));
		
			this.db = properties.getProperty("database_name");
			this.userid = properties.getProperty("user_name");
			this.password = properties.getProperty("user_password");
			this.psqlStr = properties.getProperty("psqlStr");
			this.tableName = properties.getProperty("table_name");
			this.tableSchema = properties.getProperty("table_schema");
		
			
			this.getConnection();
			//DBConnection dbConn = new DBConnection(db,userid,password,psqlStr,tableName,tableSchema);
			//dbConn.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();

			this.db = "";
			this.userid = "";
			this.password = "";
			this.psqlStr = "";
			this.tableName = "";
			this.tableSchema = "";
		}
	}
	
	public DBConnection (String db,String userid,String password,String psqlStr,String tableName,String tableSchema) {
		this.db = db;
		this.userid = userid;
		this.password = password;
		this.psqlStr = psqlStr;
		this.tableName = tableName;
		this.tableSchema = tableSchema;
	}
	
	
	public void getConnection()
	{
		
		 
		try {
			this.con = DriverManager.getConnection(
					this.psqlStr + this.db, this.userid,
					this.password);
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
 
		}
 
		if (con != null) {
			//System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
	}
	

	public Connection getCon() {
		return con;
	}


	public void setCon(Connection con) {
		this.con = con;
	}


	public String getDb() {
		return db;
	}


	public void setDb(String db) {
		this.db = db;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	public void closeCon() {
		try {
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createNonExistantTable(Connection connection) {
		
		System.out.println("CREATING TABLE");
		
		String tableCreation = "create table " + this.tableName + " " + this.tableSchema + ";";
		
		Statement stmt;
		
		try {
			stmt = connection.createStatement();
			System.out.println("Table creation string: " + tableCreation);
			stmt.executeUpdate(tableCreation);
			
			stmt.close();
			
			
		} catch(SQLException ex) {
			System.out.println("Should never get here");
		}
		
	}
	
	public static void main(String [] args) {
		
		String propertiesFile = "filename.properties";
		DBConnection dbConnection = new DBConnection(propertiesFile);
		//dbConnection.getConnection();
		
		Connection connection = dbConnection.getCon();
		
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream(propertiesFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String tableName = properties.getProperty("table_name");
		
		System.out.println(tableName);
		
		String insertString1;
		insertString1 = "insert into " + tableName + " values('1634231', 'Hemanth','y','z')";
		
		Statement stmt;
		
		try {
			stmt = connection.createStatement();
	   		//stmt.executeUpdate(tableCreation);
			stmt.executeUpdate(insertString1);
			
			stmt.close();
			//con.close();
			dbConnection.closeCon();
			
			
		} catch(SQLException ex) {
			
			//need a better catch here
			dbConnection.createNonExistantTable(connection);
			
			try {
				stmt = connection.createStatement();
				//stmt.executeUpdate(tableCreation);
				
				
				stmt.executeUpdate(insertString1);
				
				stmt.close();
				//con.close();
				dbConnection.closeCon();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   		
		}
		
		
	}
	
}
