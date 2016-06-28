package me.iphony.gameengine.state;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.event.StateChangeEvent;

public class StateManager
{
	
	private GameEngine _engine;
	private EngineState _currentState;
	
	public StateManager(GameEngine engine)
	{
		_engine = engine;
		setState(new LobbyState(_engine));
	}
	
	/**
	 * _currentState is null on first setState!
	 */
	public void setState(EngineState state)
	{
		if (_currentState != null)
		{
			_currentState.stop();
			_engine.cancelTasks(_currentState);
			HandlerList.unregisterAll(_currentState);
		}
		
		Bukkit.getPluginManager().callEvent(new StateChangeEvent(_currentState, state));
		Bukkit.getPluginManager().registerEvents(state, _engine);
		_currentState = state;
		_currentState.start();
	}
	
	public EngineState getCurrentState()
	{
		return _currentState;
	}
	
	
	
}
