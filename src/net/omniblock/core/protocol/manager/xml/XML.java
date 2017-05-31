package net.omniblock.core.protocol.manager.xml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public interface XML {

	public XML create(String filename) throws URISyntaxException, FileNotFoundException;
	
	public static List<String> readInputStream(InputStream stream){
		
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
	    String line;

	    List<String> responseData = new ArrayList<String>();
	    
	    try {
	    	
			while ((line = in.readLine()) != null) {
			    responseData.add(line);
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	    return responseData;
	    
	}
	
}
