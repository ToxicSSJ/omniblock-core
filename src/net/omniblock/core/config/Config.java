package net.omniblock.core.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;

public abstract interface Config {

	public Config create(String filename) throws URISyntaxException, MalformedURLException;
	
	public FileBasedConfigurationBuilder<FileBasedConfiguration> getBuilder();
	
	public Configuration getConfiguration();
	
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
