package net.omniblock.core.config;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import net.omniblock.core.config.type.DatabaseConfig;
import net.omniblock.core.config.type.DefaultConfig;
import net.omniblock.core.config.type.VersionConfig;

public class ConfigHandler {

	public static DefaultConfig DEFAULT_CONFIG;
	public static DatabaseConfig DATABASE_CONFIG;
	public static VersionConfig VERSION_CONFIG;
	
	static {
		
		try {
			
			DEFAULT_CONFIG = new DefaultConfig().create("config.cfg");
			DATABASE_CONFIG = new DatabaseConfig().create("database.cfg");
			VERSION_CONFIG = new VersionConfig().create("version.cfg");
			
		} catch (MalformedURLException | URISyntaxException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
}
