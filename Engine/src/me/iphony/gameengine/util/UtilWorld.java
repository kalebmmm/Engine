package me.iphony.gameengine.util;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

import me.iphony.gameengine.game.GameType;

public class UtilWorld
{

	// /maps/???/ <maps>
	// /tempMaps/currentMap
	
	private static final File mapDir = new File(new File("").getAbsolutePath() + File.separator + "maps");
	private static final File tempDir = new File(new File("").getAbsolutePath() + File.separator + "tempMap");
	
	public static void init()
	{
		removeTempWorld();
		mapDir.mkdir();
		for (GameType type : GameType.values())
		{
			File file = new File(mapDir + File.separator + type.mapPrefix);
			file.mkdir();
		}
	}
	
	public static boolean hasMaps(GameType type)
	{
		File file = new File(mapDir + File.separator + type.mapPrefix);
		return file.list().length != 0;
	}
	
	public static boolean hasConfig(File map)
	{
		return new File(map + File.separator + "Map.yml").exists();
	}
	
	public static File getMapDirectory(GameType type)
	{
		File file = new File(mapDir + File.separator + type.mapPrefix);
		file.mkdir();
		return file;
	}
	
	public static File getRandomMap(GameType type)
	{
		File folder = getMapDirectory(type);
		return folder.listFiles()[new Random().nextInt(folder.listFiles().length)];
	}
	
	public static void move(File world)
	{
		try
		{
			FileUtils.copyDirectory(world, tempDir);
		} 
		catch (IOException e)
		{
			System.out.println("[Game Engine] Error moving temporary world!");
		}
	}
	
	// Super laggy, can't do anything about it. 
	// Oh well ¯\_(ツ)_/¯
	public static World make(File map)
	{
		move(map);
		World world = Bukkit.getServer().createWorld(new WorldCreator("tempMap").environment(Environment.NORMAL).seed(0));
		world.setAutoSave(false);
		world.setTime(6000);
		world.setGameRuleValue("doDaylightCycle", "false");
		return world;
	}
	
	public static File getConfig(File map)
	{
		return new File(map + File.separator + "Map.yml");
	}
	
	public static void removeTempWorld()
	{
		try
		{
			FileUtils.forceDelete(tempDir);
		} 
		catch (IOException e)
		{
			System.out.println("[Game Engine] Error deleting temporary world!");
		}
		tempDir.mkdir();
	}
	
	public static void destroy()
	{
		if (Bukkit.getWorld("tempMap") != null)
			Bukkit.unloadWorld(Bukkit.getWorld("tempMap"), false);
		
		removeTempWorld();
	}
	
	
}
