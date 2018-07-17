package net.omniblock.core.config.type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import net.omniblock.core.OmniCore;
import net.omniblock.packets.network.tool.config.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class VersionConfig implements Config {

	protected FileConfiguration bundle;
	protected File file;
	
	protected String filename;
	
	@Override
	public VersionConfig create(String filename) throws URISyntaxException, IOException {
		
		this.filename = filename;
		
		File file = new File(Config.getJARDirectory(OmniCore.class) + "/config/" + filename);
		
		if(!file.exists()){
			
			InputStream stream = VersionConfig.class.getResourceAsStream("/config/" + filename);
			Config.copyFile(stream, file);
			
		} 
		
		
		this.bundle = YamlConfiguration.loadConfiguration(file);
		this.file = file;
		
		return this;
		
	}
	
	@Override
	public void save() {
		try { bundle.save(file);
		} catch (IOException e) { e.printStackTrace(); }
		return;
	}
	
	@Override
	public File getFile() {
		return file;
	}
	
	@Override
	public FileConfiguration getConfiguration() {
		return bundle;
	}
	
}
