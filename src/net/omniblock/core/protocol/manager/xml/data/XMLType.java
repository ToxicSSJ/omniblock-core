package net.omniblock.core.protocol.manager.xml.data;

import java.util.List;

import net.omniblock.core.protocol.manager.xml.XMLHandler;

public enum XMLType {

	PLUGINS_VERSIONS("versions.xml"),
	PERMISSIONS("permissions.xml"),
	
	;
	
	private String path;
	
	XMLType(String path) {
		
		this.path = path;
		
	}

	public XMLReader getReader() {
		
		return new XMLReader(getXml());
		
	}
	
	public String getXml(){
		
		List<String> lines = XMLHandler.getXML(path);
		return String.join("", lines);
		
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
