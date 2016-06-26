package me.iphony.gameengine.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.iphony.gameengine.state.EngineState;

public class StateChangeEvent extends Event
{

	private static final HandlerList HANDLERS = new HandlerList();
	
	private EngineState _oldState;
	private EngineState _newState;
	
	public StateChangeEvent(EngineState oldState, EngineState newState)
	{
		_oldState = oldState;
		_newState = newState;
	}
	
	public EngineState getOldState()
	{
		return _oldState;
	}
	
	public EngineState getNewState()
	{
		return _newState;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}

	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}

}
