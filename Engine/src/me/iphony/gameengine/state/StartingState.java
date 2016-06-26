package me.iphony.gameengine.state;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.iphony.gameengine.GameEngine;

public class StartingState extends EngineState
{
	public StartingState(GameEngine engine)
	{
		super(engine);
	}
	
	private int countdown = 5;

	@Override
	public void start()
	{
		getEngine().runRepeatingGameTask(this, new Runnable()
		{
			@Override
			public void run()
			{
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
