package me.iphony.gameengine.state;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.util.UtilWorld;

public class EndState extends EngineState
{

	public EndState(GameEngine engine)
	{
		super(engine);
	}
	
	int timeUntilLobby = 5;

	@Override
	public void start()
	{
		getEngine().runRepeatingGameTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				if (timeUntilLobby == 0)
				{
					getEngine().getStateManager().setState(new LobbyState(getEngine()));
					return;
				}
				else timeUntilLobby--;
			}
		}, 20);
	}

	@Override
	public void stop()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			player.teleport(Bukkit.getWorld("world").getSpawnLocation());
		}
		
		UtilWorld.destroy();
		getEngine().getGameManager().selectNextGame();
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
