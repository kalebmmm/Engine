package me.iphony.gameengine.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.game.Game.WinType;

public class GameListener implements Listener
{

	private Game _game;
	private GameEngine _plugin;
	
	/**
	 * Seperate listener is needed beacuse of how bukkit works with java
	 * @param game
	 * @param plugin
	 */
	public GameListener(Game game, GameEngine plugin)
	{
		_game = game;
		_plugin = plugin;
	}
	
	public void start()
	{
		Bukkit.getPluginManager().registerEvents(this, _plugin);
	}
	
	public void stop()
	{
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		_game.deathOrder.add(e.getEntity().getName());
		e.getEntity().spigot().respawn();
		
		if (!_game.deathDrops)
			e.getDrops().clear();
		
		int playersLeft = _plugin.getPlayerStateManager().getPlayers();
		if (playersLeft == 1 && _game.winType == WinType.LAST_MAN_STANDING)
			_plugin.getGameManager().stopGame();
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		e.setRespawnLocation(_plugin.getGameManager().getGameMap().SpectatorSpawn);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if (e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER)
		{
			Player attacker = (Player) e.getDamager();
			Player attacked = (Player) e.getEntity();
			
			if (attacked.getHealth() - e.getFinalDamage() <= 0)
				_game.giveKill(attacker);
		}
	}
	
}
