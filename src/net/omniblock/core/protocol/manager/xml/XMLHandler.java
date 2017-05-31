package net.omniblock.core.protocol.manager.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.omniblock.core.protocol.console.Console;

public class XMLHandler {

	@SuppressWarnings("unused")
	public static List<String> getXML(String filename){
		
		String path = "/plugins/" + filename;
		File file = new File(path);
		
		if(file.exists()){
			
			try {
				
				InputStream stream = new FileInputStream(file.getAbsolutePath());
				
				if(stream == null) return new ArrayList<String>();
				return readInputStream(stream);
				
			} catch (FileNotFoundException e) {
				
				Console.WRITTER.printError("No se ha podido leer el archivo " + path + " .");
				e.printStackTrace();
				return new ArrayList<String>();
				
			}
			
		} else { throw new NullPointerException("El archivo " + path + " no ha sido encontrado."); }
		
	}
	
	private static List<String> readInputStream(InputStream stream){
		
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
