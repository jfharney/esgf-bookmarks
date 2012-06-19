package org.esgf.bookmarks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

public class SearchConstraintBookmarks {

	public static boolean isConnectedToDB = true;
	
	public static String propertiesFile = "filename.properties";
	
	
	private List<SearchConstraintBookmark> bookmarks;
	
	public SearchConstraintBookmarks() {
		bookmarks = new ArrayList<SearchConstraintBookmark>();
	}

	public List<SearchConstraintBookmark> getBookmarks() {
		return bookmarks;
	}
	
	
	public void setBookmarks(List<SearchConstraintBookmark> bookmarks) {
		if(bookmarks != null) {
			this.bookmarks = bookmarks;
		}
	}
	
	public void fromDB() {
		
	}
	
	public void toDB() {
		
	}
	
	public Element toElement() {
		if(this.bookmarks != null) {
            Element bookmarksEl = new Element("bookmarks");
            
            for(int i=0;i<this.bookmarks.size();i++) {
                Bookmark bookmark = this.bookmarks.get(i);
                bookmarksEl.addContent(bookmark.toElement());
            }
            
            return bookmarksEl;
        }
		return null;
        
	}
	
	public String toXML() {
		String xml = "";
        
        Element searchConstraintBookmarksEl = this.toElement();

        XMLOutputter outputter = new XMLOutputter();
        xml = outputter.outputString(searchConstraintBookmarksEl);
        
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
	
	
	public void fromFile(String file) {
        
		this.bookmarks = null;
		
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
	
	
	public void readHelper(org.w3c.dom.Element bookmarksElement) {
        if(bookmarksElement.getNodeName().equals("bookmarks")) {
        	NodeList fileNodeList = bookmarksElement.getChildNodes();//doc.getDocumentElement().getChildNodes();
            
            List<SearchConstraintBookmark> bookmarkList = new ArrayList<SearchConstraintBookmark>();
            for(int j=0;j<fileNodeList.getLength();j++) {
                
                Node topLevelFileNode = fileNodeList.item(j);
                if (topLevelFileNode.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element topLevelFileElement = (org.w3c.dom.Element) topLevelFileNode;
                    SearchConstraintBookmark scb = new SearchConstraintBookmark();
                    scb.readHelper(topLevelFileElement);
                    bookmarkList.add(scb);
                }
                
                this.setBookmarks(bookmarkList);
            }
        }
	}
	

	//---------BEGIN CRUD Ops-----------//
	/**
	 * Get Bookmark
	 * @param openid
	 * @return
	 */
	public SearchConstraintBookmark getBookmark(String openid,String name) {
		
		if(openid != null) {
			for(int i=0;i<this.bookmarks.size();i++) {
				SearchConstraintBookmark scb = this.bookmarks.get(i);
				if(scb.getOpenId().equals(openid) && scb.getName().equals(name)) {
					return this.bookmarks.get(i);
				}
			}
		}
		
		return null;
	}
	
	public List<SearchConstraintBookmark> getBookmarksForOpenId(String openid) {
		
		List<SearchConstraintBookmark> bookmarks = new ArrayList<SearchConstraintBookmark>();
		
		DBConnection dbConnection = new DBConnection(propertiesFile);
		
		String query = "select * from searchconstraintbookmarks where openid='" + openid + "';"; 
		
		if(openid != null) {
			
			if(isConnectedToDB) {
				ResultSet rs = BookmarkSQLQueryMethods.issueSelectQuery(dbConnection, query);
				
				
				try {
					
					while(rs.next()) {
						String resultName = rs.getString(1);
						String resultOpenid = rs.getString(2);
						String resultSearchConstraints = rs.getString(3);
						String resultDescription = rs.getString(4);
						String resultTimestamp = rs.getString(5);
						
						SearchConstraintBookmark bookmark = new SearchConstraintBookmark(resultName,
																					resultOpenid,
																					resultSearchConstraints,
																					resultDescription,
																					resultTimestamp);
						
						bookmarks.add(bookmark);
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				for(int i=0;i<this.bookmarks.size();i++) {
					SearchConstraintBookmark scb = this.bookmarks.get(i);
					if(scb.getOpenId().equals(openid)) {
						bookmarks.add(this.bookmarks.get(i));
					}
				}
			}
			
			
		}
		
		return bookmarks;
		
	}
	
	/**
	 * Add Bookmark
	 * @param bookmark
	 */
	public void addBookmark(SearchConstraintBookmark bookmark) {
		
		DBConnection dbConnection = new DBConnection(propertiesFile);
		
		String name = bookmark.getName();
		String openid = bookmark.getOpenId();
		String searchConstraints = bookmark.getSearchConstraints();
		String description = bookmark.getDescription();
		String timestamp = bookmark.getTimestamp();
		
		String query = "insert into searchconstraintbookmarks values (" + 
						name + "," +
						openid + "," + 
						searchConstraints + "," + 
						description + "," +
						timestamp + ")";//"select * from searchconstraintbookmarks where openid='" + openid + "';"; 
		
		
		if(bookmark != null) {
			
			if(isConnectedToDB) {
				BookmarkSQLQueryMethods.issueUpdateQuery(dbConnection, query);
				
			} else {
				this.bookmarks.add(bookmark);
			}
			
		}
	}

	
	/**
	 * Delete Bookmark
	 * @param bookmark
	 */
	public void deleteBookmark(SearchConstraintBookmark bookmark) {
		if(bookmark != null) {
			this.bookmarks.remove(bookmark);
		}
	}
	
	/**
	 * Update Bookmark
	 * @param openid
	 * @param newBookmark
	 */
	public void updateBookmark(String openid,String name,SearchConstraintBookmark newBookmark) {
		SearchConstraintBookmark oldBookmark = this.getBookmark(openid,name);
		oldBookmark.setName(newBookmark.getName());
		oldBookmark.setDescription(newBookmark.getDescription());
		oldBookmark.setSearchConstraints(newBookmark.getSearchConstraints());
		oldBookmark.setName(newBookmark.getName());
		
	}
	
	//---------END CRUD Ops-----------//
	
	
	
	public static void main(String [] args) {
		SearchConstraintBookmarks scbs = new SearchConstraintBookmarks();
		
		String fileName = SearchConstraintBookmark.DIR_PREFIX + "bookmarks.xml";
		
		scbs.fromFile(fileName);
		
		String openid = "b";
		String name = "name1";
		List<SearchConstraintBookmark> searchedBookmarks = scbs.getBookmarksForOpenId(openid);
		
		System.out.println(searchedBookmarks.size());
		
		SearchConstraintBookmark bookmark = new SearchConstraintBookmark("11","22","33","44","55");
		scbs.addBookmark(bookmark);
		//SearchConstraintBookmark scb = scbs.getBookmark(openid, name);
		
		//System.out.println(scb.getDescription());
		
	}
	
	
	
	
}
