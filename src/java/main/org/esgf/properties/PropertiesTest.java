package org.esgf.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesTest {

	public static void main(String [] args) {
		// Read properties file.
		Properties properties = new Properties();
		
		try {
		    //properties.load(new FileInputStream("/Users/8xo/esgProjects/esgf-bookmarks/src/java/main/org/esgf/properties/filename.properties"));
		    properties.load(new FileInputStream("filename.properties"));
		    
		    String a = (String)properties.get("a");
		    System.out.println(a);
		} catch (IOException e) {
			System.out.println("couldn't find properties file");
		}

		/*
		// Write properties file.
		try {
		    properties.store(new FileOutputStream("filename.properties"), null);
		} catch (IOException e) {
		
		}
		*/
	}
	
}
