package me.iphony.gameengine.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;

import me.iphony.gameengine.GameEngine;

public class PlayerSpectatingState extends PlayerState
{

	public PlayerSpectatingState(GameEngine engine, Player player)
	{
		super(engine, player);
		player.setMaxHealth(20);
		player.setFoodLevel(20);
		player.setHealth(20);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(true);
		player.setFlying(true);
		
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
		
		if (e instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent en = (EntityDamageByEntityEvent) e;
			if (en.getDamager() == getPlayer())
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e)
	{
		if (e.getPlayer() == getPlayer())
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e)
	{
		if (e.getPlayer() == getPlayer())
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if (e.getPlayer() == getPlayer())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void hangingInteract(HangingBreakByEntityEvent e)
	{
		if (e.getEntity() == getPlayer())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		if (e.getPlayer() == getPlayer())
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e)
	{
		if (e.getPlayer() == getPlayer())
			e.setBuild(true);
	}
	
	@EventHandler
	public void onProjectile(ProjectileLaunchEvent e)
	{
		if (e.getEntity().getShooter() == getPlayer())
			e.setCancelled(true);
	}
	
}
