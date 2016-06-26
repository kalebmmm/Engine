package me.iphony.gameengine.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.alexandeh.glaedr.Glaedr;
import com.alexandeh.glaedr.scoreboards.Entry;
import com.alexandeh.glaedr.scoreboards.PlayerScoreboard;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.event.StateChangeEvent;

public class ScoreboardManager implements Listener
{

	private GameEngine _engine;
	private Glaedr _glaedr;
	
	private HashMap<Player, PlayerScoreboard> sbMap = new HashMap<Player, PlayerScoreboard>();
	
	public ScoreboardManager(GameEngine engine)
	{
		_engine = engine;
		Bukkit.getPluginManager().registerEvents(this, engine);
		init();
	}
	
	public void init()
	{
		_glaedr = new Glaedr(_engine, " &e&lGame Engine ");
		
		for (Player pl : Bukkit.getOnlinePlayers())
			resetScoreboard(pl);
	}
	
	@EventHandler
	public void clear(StateChangeEvent e)
	{
		for (Player player : Bukkit.getOnlinePlayers())
			resetScoreboard(player);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		resetScoreboard(e.getPlayer());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		sbMap.remove(e.getPlayer());
	}
	
	public void resetScoreboard(Player player)
	{
		if (sbMap.get(player) != null)
		{
			for (Entry entry : sbMap.get(player).getEntries())
				entry.cancel();
		}
		
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); // Cleans up glitched scoreboards/world scoreboards
		PlayerScoreboard sc = new PlayerScoreboard(_glaedr, player);
		sbMap.put(player, sc);
	}
	
	public PlayerScoreboard getPlayerScoreboard(Player player)
	{
		return sbMap.get(player);
	}
	
	
}
