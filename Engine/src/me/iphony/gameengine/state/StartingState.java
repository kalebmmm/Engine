package me.iphony.gameengine.state;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.map.GameMap;
import me.iphony.gameengine.util.UtilMessage;

public class StartingState extends EngineState
{
	public StartingState(GameEngine engine)
	{
		super(engine);
	}
	
	private int countdown = 10;

	@Override
	public void start()
	{
		//TODO WIP
		GameMap map = getEngine().getGameManager().getGameMap();
		for (Player player : Bukkit.getOnlinePlayers())
		{
			player.teleport(map.Locations.get("yellow").get(0));
		}
		
		getEngine().runRepeatingGameTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				if (getEngine().getPlayerStateManager().getPlayers() < 2)
				{
					UtilMessage.broadcast("&c&lStopping game: Not enough players!");
					getEngine().getStateManager().setState(new EndState(getEngine()));
					return;
				}
				
				if (countdown == 0)
				{
					getEngine().getGameManager().startGame();
					getEngine().getStateManager().setState(new IngameState(getEngine()));
				}
				
				else countdown--;
			}
		}, 20);
	}

	@Override
	public void stop()
	{
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if (getEngine().getGameManager().getCurrentGame().moveDuringWait)
			return;
		
		if(e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())
		{
			Location from = e.getFrom();
			e.getPlayer().teleport(from);
		}
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
