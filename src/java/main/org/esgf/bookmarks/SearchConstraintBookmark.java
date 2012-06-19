package org.esgf.bookmarks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SearchConstraintBookmark extends Bookmark{

	public static String DIR_PREFIX = "src/java/main/org/esgf/bookmarks/xml/";//URLSElementTestData-WrongElementNames.xml";
    
    public static String insertionStringPrefix = "insert into searchconstraintbookmarks values";
	
	private String searchConstraints;
	
	public SearchConstraintBookmark() {
		super();
		this.searchConstraints = "";
	}
	
	public SearchConstraintBookmark(String name,String openId,String description,String searchConstraints,String timestamp) {
		super(name,openId,description,timestamp);
		this.searchConstraints = searchConstraints;
	}
	
	public String getSearchConstraints() {
		return searchConstraints;
	}

	public void setSearchConstraints(String searchConstraints) {
		this.searchConstraints = searchConstraints;
	}

	

	@Override
	public String toXML() {
		String xml = "";
        
        Element searchConstraintBookmarkEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(searchConstraintBookmarkEl);
        
        return xml;
	}
	
	public JSONObject toJSONObject() {
        JSONObject json = null;
        
        try {
            json = XML.toJSONObject(this.toXML());
        } catch (JSONException e) {
            System.out.println("Problem in toJSONObject");
            e.printStackTrace();
        }
        
        return json;
    }

	@Override
	public String toJSON() {
		String json = null;
    
		try {
			json = this.toJSONObject().toString(3);
		} catch (JSONException e) {
			System.out.println("Problem in toJSON");
			e.printStackTrace();
		}
    
		return json;
	}

	@Override
	public Element toElement() {

        Element searchConstraintBookmarkEl = new Element("bookmark");
        
        if(this.name != null) {
        	Element nameEl = new Element("name");
        	nameEl.addContent(this.name);
        	searchConstraintBookmarkEl.addContent(nameEl);
        	
        }
        if(this.openId != null) {
        	Element openIdEl = new Element("openId");
        	openIdEl.addContent(this.openId);
        	searchConstraintBookmarkEl.addContent(openIdEl);
        	
        }
        if(this.description != null) {
        	Element descriptionEl = new Element("description");
        	descriptionEl.addContent(this.description);
        	searchConstraintBookmarkEl.addContent(descriptionEl);
        	
        }
        if(this.searchConstraints != null) {
        	Element searchConstraintsEl = new Element("searchConstraints");
        	searchConstraintsEl.addContent(this.searchConstraints);
        	searchConstraintBookmarkEl.addContent(searchConstraintsEl);
        }
        if(this.timestamp != null) {
        	Element timestampEl = new Element("timestamp");
        	timestampEl.addContent(this.timestamp);
        	searchConstraintBookmarkEl.addContent(timestampEl);
        }
        
        return searchConstraintBookmarkEl;
	}
	
	
	public void toDB() {
		
		/*
		DBConnection dbConnection = new DBConnection(propertiesFile);
		//dbConnection.getConnection();
		
		
		System.out.println("toDB");
		//System.exit(0);

		String query = insertionStringPrefix + " ('ada','b','c','d','e')";
		
		//issueUpdateQuery(dbConnection, query);
		
		query = "select * from searchconstraintbookmarks;";
		
		ResultSet rs = this.issueSelectQuery(dbConnection, query);
		

		int count = 0;
		try {
			while (rs.next()){
				count++;

				String name = rs.getString(1);
				String openid = rs.getString(2);
				System.out.println("Name: " + name + " openid: " + openid);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(count);

		dbConnection.closeCon();
		*/
		
	}
	
	
	
	
	
	public void fromDB() {
		
	}
	
	
	public void toFile(String file) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(new XmlFormatter().format(this.toXML()));
            out.close();
        } 
        catch (IOException e) { 
            e.printStackTrace();
            System.out.println("Exception ");

        }
    }

	public void fromFile(String file) {
        
		this.name = null;
		this.description = null;
		this.openId = null;
		this.searchConstraints = null;
		this.timestamp = null;
		
		File fXmlFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            
            org.w3c.dom.Element bookmarkElement = doc.getDocumentElement();
            
            this.readHelper(bookmarkElement);
            
            
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
    }
	
	public void readHelper(org.w3c.dom.Element docElement) {
		if(docElement.getNodeName().equals("bookmark")) {
			NodeList docNodeList = docElement.getChildNodes();
			
			
			for(int i=0;i<docNodeList.getLength();i++) {
				Node topLevelDocNode = docNodeList.item(i);
				
				if(topLevelDocNode.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element topLevelElement = (org.w3c.dom.Element) topLevelDocNode;
					if(topLevelElement.getTagName().equals("name")) {
                        this.name = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("openId")) {
                        this.openId = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("description")) {
                        this.description = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("searchConstraints")) {
                        this.searchConstraints = topLevelElement.getTextContent();
                    } else if(topLevelElement.getTagName().equals("timestamp")) {
                    	this.timestamp = topLevelElement.getTextContent();
                    } 
				}
			}
			
            
		}
	}

	
	public static void main(String [] args) {
		SearchConstraintBookmark scb = new SearchConstraintBookmark("a","b","c","d","timestamp");
		
		
		String fileName = SearchConstraintBookmark.DIR_PREFIX + "bookmark.xml";
		//scb.toFile(fileName);
		
		scb.toFile(fileName);
		
		scb.toDB();
		//System.out.println(scb.getDescription());
		
		
	}
	
	
	
}
