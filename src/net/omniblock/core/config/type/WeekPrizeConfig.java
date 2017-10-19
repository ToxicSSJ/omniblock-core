package net.omniblock.core.config.type;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import net.omniblock.core.config.Config;

public class WeekPrizeConfig implements Config {

	protected Configuration bundle;
	protected String filename;
	
	@Override
	public WeekPrizeConfig create(String filename) throws URISyntaxException, MalformedURLException {
		
		this.filename = filename;
		
		File file = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "/config/" + filename);
		
		if(!file.exists()){
			
			file.mkdirs();
			Config.copyFile(ClassLoader.getSystemResourceAsStream("/config/" + filename), file);
			
		}
		
		try { this.bundle = getBuilder().getConfiguration(); } 
		catch (ConfigurationException e) { e.printStackTrace(); }
		
		return this;
		
	}
	
	@Override
	public FileBasedConfigurationBuilder<FileBasedConfiguration> getBuilder(){
		
		Parameters params = new Parameters();
		
		return new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.properties()
			    		  .setFileName(filename)
			    		  .setListDelimiterHandler(new DefaultListDelimiterHandler(',')
			    		  )
			   );
	}
	
	@Override
	public Configuration getConfiguration() {
		return bundle;
	}

}
