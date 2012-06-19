package org.esgf.bookmarks;

import org.jdom.Element;

public abstract class Bookmark {

	protected String name;
	protected String openId;
	protected String description;
	protected String timestamp;
	
	public Bookmark() {
		this.name = "";
		this.openId = "";
		this.description = "";
		this.timestamp = "";
	}
	
	public Bookmark(String name,String openId,String description,String timestamp) {
		this.name = name;
		this.openId = openId;
		this.description = description;
		this.timestamp = timestamp;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTimestamp() {
		return this.description;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public abstract Element toElement();
	
	public abstract String toXML();
	
	public abstract String toJSON();
	
	public abstract void fromFile(String fileName);
	
	public abstract void toFile(String fileName);
	
	
	
}
