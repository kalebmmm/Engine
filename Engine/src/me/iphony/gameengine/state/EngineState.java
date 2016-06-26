package me.iphony.gameengine.state;

import org.bukkit.event.Listener;

import me.iphony.gameengine.GameEngine;

public abstract class EngineState implements Listener
{
	
	private GameEngine _engine;
	
	public EngineState(GameEngine engine)
	{
		_engine = engine;
	}
	
	public GameEngine getEngine()
	{
		return this._engine;
	}
	
	public abstract void start();
	public abstract void stop();
}
