package me.iphony.gameengine.state;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.alexandeh.glaedr.scoreboards.Entry;
import com.alexandeh.glaedr.scoreboards.PlayerScoreboard;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.scoreboard.ScoreboardManager;

public class LobbyWaitState extends EngineState
{

	private int countdownTime = 30;
	private ScoreboardManager _sb;
	
	public LobbyWaitState(GameEngine engine)
	{
		super(engine);
		_sb = engine.getScoreboardManager();
	}

	@Override
	public void start()
	{
		for (Player pl : Bukkit.getOnlinePlayers())
			scoreboard(pl);
		
		getEngine().runRepeatingGameTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				int online = Bukkit.getOnlinePlayers().size();
				if (online >= getEngine().getGameManager().getCurrentGame().getMinPlayers())
				{
					if (countdownTime == 0)
					{
						getEngine().getStateManager().setState(new StartingState(getEngine()));
						return;
					}
					else countdownTime--;
					
					for (Player p : Bukkit.getOnlinePlayers())
					{
						_sb.getPlayerScoreboard(p).getEntry("players").setText("&6»&f " + online).send();
						_sb.getPlayerScoreboard(p).getEntry("game").setText("&6»&f " + getEngine().getGameManager().getCurrentGame().getType().name).send();
					}
				}
				else
				{
					getEngine().getStateManager().setState(new LobbyState(getEngine()));
					return;
				}
			}
		}, 20);
	}
	
	@Override
	public void stop()
	{
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		scoreboard(e.getPlayer());
	}
	
	private void scoreboard(Player player)
	{
		PlayerScoreboard s = _sb.getPlayerScoreboard(player);
		new Entry("blank1", s).setText("").send();
		new Entry("gameTitle", s).setText("&e&lGame").send();
		new Entry("game", s).setText("&6»&f " + getEngine().getGameManager().getCurrentGame().getType().name).send();
		new Entry("blank2", s).setText("").send();
		new Entry("playersTitle", s).setText("&e&lPlayers").send();
		new Entry("players", s).setText("&6»&f " + Bukkit.getOnlinePlayers().size()).send();
		new Entry("blank3", s).setText("").send();
		new Entry("startingTitle", s).setText("&e&lStart In:").send();
		new Entry("starting", s).setCountdown(true).setTime(countdownTime).setText("&6»&f").send();
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onHurt(EntityDamageEvent e)
	{
		e.setCancelled(true);
	}
	
}
