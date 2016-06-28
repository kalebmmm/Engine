package me.iphony.gameengine.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.iphony.gameengine.GameEngine;

public abstract class PlayerState implements Listener
{

	private GameEngine _engine;
	private Player _player;
	
	public PlayerState(GameEngine engine, Player player)
	{
		_engine = engine;
		_player = player;
	}
	
	public GameEngine getEngine()
	{
		return this._engine;
	}
	
	public Player getPlayer()
	{
		return this._player;
	}
	
}
