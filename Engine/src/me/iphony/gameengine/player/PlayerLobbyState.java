package me.iphony.gameengine.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;

import me.iphony.gameengine.GameEngine;

public class PlayerLobbyState extends PlayerState
{

	public PlayerLobbyState(GameEngine engine, Player player)
	{
		super(engine, player);
		player.setFoodLevel(20);
		player.setHealth(20);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setFlying(false);
		
		for(PotionEffect effect : player.getActivePotionEffects())
		    player.removePotionEffect(effect.getType());
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e)
	{
		if (e.getEntity() == getPlayer())
			e.setFoodLevel(20);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		if (e.getEntity() == getPlayer())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e)
	{
		if (e.getPlayer() == getPlayer())
		{
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e)
	{
		if (e.getPlayer() == getPlayer())
		{
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
				e.setCancelled(true);
		}
	}

}
