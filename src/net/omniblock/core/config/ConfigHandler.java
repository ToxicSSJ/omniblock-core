package net.omniblock.core.config;

import java.io.IOException;
import java.net.URISyntaxException;

import net.omniblock.core.config.type.DatabaseConfig;
import net.omniblock.core.config.type.DefaultConfig;
import net.omniblock.core.config.type.VersionConfig;
import net.omniblock.core.config.type.WeekPrizeConfig;

public class ConfigHandler {

	protected static DefaultConfig DEFAULT_CONFIG;
	protected static DatabaseConfig DATABASE_CONFIG;
	protected static VersionConfig VERSION_CONFIG;
	
	protected static WeekPrizeConfig WEEKPRIZE_CONFIG;
	
	public static void create(){
		
		try {
			
			DEFAULT_CONFIG = new DefaultConfig().create("config.yml");
			DATABASE_CONFIG = new DatabaseConfig().create("database.yml");
			VERSION_CONFIG = new VersionConfig().create("version.yml");
			
			WEEKPRIZE_CONFIG = new WeekPrizeConfig().create("addons/weekprize.yml");
			
		} catch (URISyntaxException | IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static DefaultConfig getDefaultConfig(){
		return DEFAULT_CONFIG;
	}
	
	public static DatabaseConfig getDatabaseConfig(){
		return DATABASE_CONFIG;
	}
	
	public static VersionConfig getVersionConfig(){
		return VERSION_CONFIG;
	}
	
	public static WeekPrizeConfig getWeekPrizeConfig(){
		return WEEKPRIZE_CONFIG;
	}
	
}
