package me.iphony.gameengine.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.util.UtilMessage;

public abstract class Game implements Listener {

	GameEngine _engine;
	GameType _type;
	int _minPlayers;
	GameListener _listener;
	
	public Game(GameEngine engine, GameType type, int minPlayers)
	{
		_engine = engine;
		_type = type;
		_minPlayers = minPlayers;
		_listener = new GameListener(this, getEngine());
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
		_listener.start();
		onStart();
	}

	public void stop()
	{
		_listener.stop();
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
	
	public List<String> deathOrder = new ArrayList<String>();
	public HashMap<String, Integer> kills = new HashMap<String, Integer>();
	
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
