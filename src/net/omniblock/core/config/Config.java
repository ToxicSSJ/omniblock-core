package net.omniblock.core.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.CodeSource;

import org.bukkit.configuration.file.FileConfiguration;

import net.omniblock.core.OmniCore;

public abstract interface Config {

	public Config create(String filename) throws URISyntaxException, MalformedURLException, IOException;
	
	public FileConfiguration getConfiguration();
	
	public File getFile();
	
	public void save();
	
	public static String getJARDirectory(){
		
		try {
			
			CodeSource codeSource = OmniCore.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			return jarFile.getParentFile().getPath();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return ".";
		
	}
	
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
