package me.iphony.gameengine.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import me.iphony.gameengine.exception.EngineExeption;
import me.iphony.gameengine.game.GameType;

public class GameMap
{

	public World Map;
	public HashMap<String, List<Location>> Locations = new HashMap<String, List<Location>>();
	public Location SpectatorSpawn;
	public String Name = "???";
	public String Author = "???";
	
	public GameMap(World map, File configuration, GameType gametype)
	{
		this.Map = map;
		parseYaml(configuration);
	}
	
	private void parseYaml(File conf)
	{
		if (conf == null || !conf.exists())
			throw new EngineExeption("Map.yml not found!");
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(conf);
		
		this.Name = config.getString("Name");
		this.Author = config.getString("Author");

		for (String spawn : config.getStringList("Spawns"))
		{
			String[] parts = spawn.split(";");
			
			int x,y,z;
			float yaw,pitch;
			
			x = Integer.parseInt(parts[1]);
			y = Integer.parseInt(parts[2]);
			z = Integer.parseInt(parts[3]);
			yaw = Float.parseFloat(parts[4]);
			pitch = Float.parseFloat(parts[5]);
			
			Location loc = new Location(Map, x, y, z, yaw, pitch);
			
			if (!Locations.containsKey(parts[0]))
				Locations.put(parts[0], new ArrayList<Location>());
			
			Locations.get(parts[0]).add(loc);
		}
		
		String[] parts = config.getString("SpectatorSpawn").split(";");
		
		int x,y,z;
		float yaw,pitch;
		
		x = Integer.parseInt(parts[0]);
		y = Integer.parseInt(parts[1]);
		z = Integer.parseInt(parts[2]);
		yaw = Float.parseFloat(parts[3]);
		pitch = Float.parseFloat(parts[4]);
		
		SpectatorSpawn = new Location(Map, x, y, z, yaw, pitch);
		
	}
	
	
}
