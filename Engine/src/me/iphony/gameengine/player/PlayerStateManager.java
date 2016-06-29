package me.iphony.gameengine.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.event.StateChangeEvent;
import me.iphony.gameengine.game.GameManager;
import me.iphony.gameengine.state.EndState;
import me.iphony.gameengine.state.EngineState;
import me.iphony.gameengine.state.IngameState;
import me.iphony.gameengine.state.LobbyState;
import me.iphony.gameengine.state.StartingState;

public class PlayerStateManager implements Listener
{

	private GameEngine _engine;
	private HashMap<Player, PlayerState> stateMap = new HashMap<Player, PlayerState>();
	
	private GameManager _manager;
	
	public PlayerStateManager(GameEngine engine)
	{
		_engine = engine;
		_manager = _engine.getGameManager();
		Bukkit.getPluginManager().registerEvents(this, engine);
	}
	
	public GameEngine getEngine()
	{
		return this._engine;
	}
	
	public void setState(Player player, PlayerState state)
	{
		System.out.println("[State] Setting " + player.getName() + " to " + state.getClass().getSimpleName());
		
		if (stateMap.get(player) != null)
			HandlerList.unregisterAll(stateMap.get(player));
		
		stateMap.put(player, state);
		Bukkit.getPluginManager().registerEvents(state, getEngine());
	}
	
	public int getSpectators()
	{
		int players = 0;
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (stateMap.get(player) instanceof PlayerSpectatingState)
				players++;
		}
		return players;
	}
	
	public int getPlayers()
	{
		int players = 0;
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (stateMap.get(player) instanceof PlayerLobbyState || stateMap.get(player) instanceof PlayerIngameState)
				players++;
		}
		return players;
	}
	
	public List<Player> getSpectatorList()
	{
		List<Player> players = new ArrayList<Player>();
		for (Player pl : Bukkit.getOnlinePlayers())
		{
			if (stateMap.get(pl) instanceof PlayerSpectatingState)
				players.add(pl);
		}
		return players;
	}
	
	public List<Player> getPlayerList()
	{
		List<Player> players = new ArrayList<Player>();
		for (Player pl : Bukkit.getOnlinePlayers())
		{
			if (stateMap.get(pl) instanceof PlayerLobbyState || stateMap.get(pl) instanceof PlayerIngameState)
				players.add(pl);
		}
		return players;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e)
	{
		e.getPlayer().getInventory().clear();
		EngineState current = getEngine().getStateManager().getCurrentState();
		if (current instanceof IngameState || current instanceof StartingState || current instanceof EndState)
		{
			e.getPlayer().teleport(_manager.getGameMap().SpectatorSpawn);
			setState(e.getPlayer(), new PlayerSpectatingState(getEngine(), e.getPlayer()));
		}
		else
		{
			e.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
			setState(e.getPlayer(), new PlayerLobbyState(getEngine(), e.getPlayer()));
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		HandlerList.unregisterAll(stateMap.get(e.getPlayer()));
		stateMap.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		setState(e.getPlayer(), new PlayerSpectatingState(getEngine(), e.getPlayer()));
	}
	
	@EventHandler
	public void onStateChange(StateChangeEvent e)
	{
		if (e.getNewState() instanceof StartingState)
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				setState(player, new PlayerIngameState(getEngine(), player));
			}
		}
		
		if (e.getNewState() instanceof LobbyState)
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				setState(player, new PlayerLobbyState(getEngine(), player));
			}
		}
	}
	
}
