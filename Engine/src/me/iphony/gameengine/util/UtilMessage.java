package me.iphony.gameengine.util;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class UtilMessage
{

	public static void broadcast(String message)
	{
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public static void broadcastWin(String first, String second, String third)
	{
		broadcast("&b===========================");
		broadcast("&a&l1st: &f" + first);
		broadcast("&a&l2nd: &f" + second);
		broadcast("&a&l3rd: &f" + third);
		broadcast("&b===========================");
	}
	
	
}
