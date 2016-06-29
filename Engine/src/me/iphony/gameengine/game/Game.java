package me.iphony.gameengine.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.util.UtilMessage;

public abstract class Game implements Listener {

	GameEngine _engine;
	GameType _type;
	int _minPlayers;
	
	public Game(GameEngine engine, GameType type, int minPlayers)
	{
		_engine = engine;
		_type = type;
		_minPlayers = minPlayers;
	}
	
	public GameType getGameType()
	{
		return this._type;
	}
	
	public int getMinPlayers()
	{
		return this._minPlayers;
	}
	
	public GameEngine getEngine()
	{
		return this._engine;
	}
	
	public void start() 
	{
		onStart();
	}

	public void stop()
	{
		win();
		onStop();
	}
	
	public abstract void onStart();
	public abstract void onStop();

	public GameType getType() 
	{
		return _type;
	}
	
	// Settings
	public boolean moveDuringWait = false;
	public boolean pvp = true;
	public boolean invincible = false;
	public boolean fallDamage = true;
	public boolean hurtByEntities = true;
	public boolean hurtEntities = true;
	public boolean hunger = false;
	public boolean deathDrops = false;
	public WinType winType = WinType.LAST_MAN_STANDING;
	
	private List<String> deathOrder = new ArrayList<String>();
	private HashMap<String, Integer> kills = new HashMap<String, Integer>();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		deathOrder.add(e.getEntity().getName());
		e.getEntity().spigot().respawn();
		
		if (!deathDrops)
			e.getDrops().clear();
		
		int playersLeft = getEngine().getPlayerStateManager().getPlayers();
		if (playersLeft == 1 && winType == WinType.LAST_MAN_STANDING)
			getEngine().getGameManager().stopGame();
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		e.setRespawnLocation(getEngine().getGameManager().getGameMap().SpectatorSpawn);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if (e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER)
		{
			Player attacker = (Player) e.getDamager();
			Player attacked = (Player) e.getEntity();
			
			if (attacked.getHealth() - e.getFinalDamage() <= 0)
				giveKill(attacker);
		}
	}
	
	public void giveKill(Player attacker)
	{
		if (!kills.containsKey(attacker.getName()))
			kills.put(attacker.getName(), 0);
		
		kills.put(attacker.getName(), kills.get(attacker.getName()) + 1);
	}
	
	public enum WinType
	{
		LAST_MAN_STANDING,
		MOST_KILLS,
		CUSTOM;
	}
	
	public void win()
	{
		switch (this.winType)
		{
		case CUSTOM:
			// Handled by game
			break;
		case LAST_MAN_STANDING:
			
			Player winner = getEngine().getPlayerStateManager().getPlayerList().get(0);
			String second = "Nobody";
			String third = "Nobody";
			
			try{ second = deathOrder.get(deathOrder.size() - 1); } catch (Exception e) {}
			try{ third = deathOrder.get(deathOrder.size() - 2); } catch (Exception e) {}
			
			UtilMessage.broadcastWin(winner.getName(), second, third);
			
			break;
		case MOST_KILLS:
			//TODO
			break;
		default:
			break;
		}
	}

}
