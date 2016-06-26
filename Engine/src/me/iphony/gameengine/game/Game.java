package me.iphony.gameengine.game;

import org.bukkit.event.Listener;

import me.iphony.gameengine.GameEngine;

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

}
