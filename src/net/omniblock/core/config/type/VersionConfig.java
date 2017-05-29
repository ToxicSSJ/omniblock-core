package net.omniblock.core.config.type;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import net.omniblock.core.config.Config;

public class VersionConfig implements Config {

	protected ResourceBundle bundle;
	
	@Override
	public VersionConfig create(String filename) throws URISyntaxException, MalformedURLException {
		
		File file = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "/config/" + filename);
		
		if(!file.exists()){
			
			file.mkdirs();
			Config.copyFile(ClassLoader.getSystemResourceAsStream("/config/" + filename), file);
			return this;
			
		}
		
		URL[] urls = { file.toURI().toURL() };
		ClassLoader loader = new URLClassLoader(urls);
		ResourceBundle bundle = ResourceBundle.getBundle(filename, Locale.getDefault(), loader);
		
		this.bundle = bundle;
		return this;
		
	}
	
	@Override
	public ResourceBundle getResource() {
		return bundle;
	}

	@Override
	public String getData(String key) {
		return bundle.getString(key);
	}

}
