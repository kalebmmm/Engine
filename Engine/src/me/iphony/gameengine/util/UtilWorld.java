package me.iphony.gameengine.util;

import org.bukkit.Bukkit;
import org.bukkit.World;

import me.iphony.gameengine.game.GameType;

public class UtilWorld
{

	// /maps/???/ <maps>
	// /tempMaps/currentMap
	
	public static void make(GameType type)
	{
		
	}
	
	public static void destroy(World world)
	{
		if (world != null)
			Bukkit.unloadWorld(world, false);
	}
	
	
}
