package me.iphony.gameengine.games.kill;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.game.Game;
import me.iphony.gameengine.game.GameType;
import me.iphony.gameengine.util.UtilMessage;

public class KillGame extends Game
{

	public KillGame(GameEngine engine)
	{
		super(engine, GameType.KILL, 2);
		super.fallDamage = false;
		super.moveDuringWait = false;
		super.pvp = true;
	}

	@Override
	public void onStart()
	{
		ItemStack sword = new ItemStack(Material.STONE_SWORD);
		ItemMeta meta = sword.getItemMeta();
		meta.spigot().setUnbreakable(true);
		sword.setItemMeta(meta);
		
		for (Player pl : Bukkit.getOnlinePlayers())
			pl.getInventory().addItem(sword);
	
		UtilMessage.broadcast("&6&lYou have a sword and a dream.");
		UtilMessage.broadcast("&6&lLast player alive wins.");
	}

	@Override
	public void onStop()
	{
	}

}
