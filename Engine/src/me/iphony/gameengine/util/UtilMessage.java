package me.iphony.gameengine.util;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;

public class UtilMessage
{

	public static void broadcast(String message)
	{
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	
}
