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
	public HashMap<String, List<Location>> Locations;
	public String Name = "???";
	public String Author = "???";
	
	public GameMap(File directory, GameType gametype)
	{
		parseYaml(directory);
	}
	
	private void parseYaml(File dir)
	{
		File file = new File(dir + File.separator + "Map.yml");
		
		if (file == null || !file.exists())
			throw new EngineExeption("Map.yml not found!");
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		this.Name = config.getString("Name");
		this.Author = config.getString("Author");

		for (String spawn : config.getStringList("spawns"))
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
		
	}
	
	
}
