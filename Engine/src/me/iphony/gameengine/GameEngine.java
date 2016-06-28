package me.iphony.gameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.alexandeh.glaedr.Glaedr;

import me.iphony.gameengine.game.GameManager;
import me.iphony.gameengine.game.GameType;
import me.iphony.gameengine.player.PlayerStateManager;
import me.iphony.gameengine.scoreboard.ScoreboardManager;
import me.iphony.gameengine.state.StateManager;
import me.iphony.gameengine.util.UtilWorld;

public class GameEngine extends JavaPlugin implements Listener {

	GameManager _gameManager;
	StateManager _stateManager;
	ScoreboardManager _scoreboardManager;
	PlayerStateManager _playerStateManager;
	private HashMap<Object, List<Integer>> _gameTaskManager = new HashMap<Object, List<Integer>>();
	private GameType[] games = new GameType[] 
	{
		GameType.EXAMPLE
	};
	
	Glaedr glaedr;
	
	public void onEnable()
	{
		UtilWorld.init();
		
		_gameManager = new GameManager(this);
		_gameManager.setGames(games);
		_gameManager.enable();
		_scoreboardManager = new ScoreboardManager(this);
		_stateManager = new StateManager(this);
		_playerStateManager = new PlayerStateManager(this);
	}
	
	public void onDisable()
	{
		for (Player pl : Bukkit.getOnlinePlayers())
			pl.kickPlayer("Server is restarting");
		
		UtilWorld.destroy();
		UtilWorld.removeTempWorld();
	}
	
	public ScoreboardManager getScoreboardManager()
	{
		return _scoreboardManager;
	}
	
	public GameManager getGameManager()
	{
		return this._gameManager;
	}
	
	public StateManager getStateManager()
	{
		return this._stateManager;
	}
	
	public PlayerStateManager getPlayerStateManager()
	{
		return this._playerStateManager;
	}
	
	public void runGameTask(Object origin, Runnable runnable, long ticks)
	{
		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(this, runnable, ticks);
		addTask(origin, id);
	}
	
	public void runAsyncGameTask(Object origin, Runnable runnable, long ticks)
	{
		BukkitTask id = Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, ticks);
		addTask(origin, id.getTaskId());
	}
	
	public void runRepeatingGameTask(Object origin, Runnable runnable, long ticks)
	{
		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, runnable, 0, ticks);
		addTask(origin, id);
	}
	
	public void runAsyncRepeatingGameTask(Object origin, Runnable runnable, long ticks)
	{
		BukkitTask id = Bukkit.getScheduler().runTaskTimerAsynchronously(this, runnable, 0, ticks);
		addTask(origin, id.getTaskId());
	}
	
	public void cancelTasks(Object origin)
	{
		if (_gameTaskManager.get(origin) == null)
			return;
		
		for (int i : _gameTaskManager.get(origin))
		{
			Bukkit.getScheduler().cancelTask(i);
		}
		_gameTaskManager.get(origin).clear();
		_gameTaskManager.remove(origin);
	}
	
	private void addTask(Object origin, int taskid)
	{
		if(!_gameTaskManager.containsKey(origin))
			_gameTaskManager.put(origin, new ArrayList<Integer>());
		
		_gameTaskManager.get(origin).add(taskid);
	}
}
