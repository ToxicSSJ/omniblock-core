package net.omniblock.core.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

public abstract interface Config {

	public Config create(String filename) throws URISyntaxException, MalformedURLException;
	
	public ResourceBundle getResource();
	
	public String getData(String key);
	
	public static void copyFile(InputStream in , File file) {
		
		try {
			
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte['?'];
			int len;
			
			while((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			
			out.close(); 
			in.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
