package me.iphony.gameengine.state;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.util.UtilMessage;

public class IngameState extends EngineState
{
	public IngameState(GameEngine engine)
	{
		super(engine);
	}

	@Override
	public void start()
	{
		getEngine().runRepeatingGameTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				if (getEngine().getPlayerStateManager().getPlayers() < 2)
				{
					UtilMessage.broadcast("&c&lStopping game: Not enough players!");
					getEngine().getStateManager().setState(new EndState(getEngine()));
				}
			}
		}, 20);
	}

	@Override
	public void stop()
	{
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent e)
	{
		if (e instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
			
			if (ee.getEntity().getType() == EntityType.PLAYER 
					&& ee.getDamager().getType() == EntityType.PLAYER
					&& !getEngine().getGameManager().getCurrentGame().pvp)
				e.setCancelled(true);
			
			if (ee.getEntity().getType() == EntityType.PLAYER 
					&& ee.getDamager().getType() != EntityType.PLAYER 
					&& !getEngine().getGameManager().getCurrentGame().hurtByEntities)
				e.setCancelled(true);
		}
		
		if (e.getEntity().getType() != EntityType.PLAYER
				&& !getEngine().getGameManager().getCurrentGame().hurtEntities)
		e.setCancelled(true);
			
		if (e.getCause() == DamageCause.FALL 
				&& e.getEntityType() == EntityType.PLAYER 
				&& !getEngine().getGameManager().getCurrentGame().fallDamage)
			e.setCancelled(true);
		
		if (e.getEntity().getType() == EntityType.PLAYER 
				&& getEngine().getGameManager().getCurrentGame().invincible)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e)
	{
		if (e.getEntity().getType() == EntityType.PLAYER
				&& !getEngine().getGameManager().getCurrentGame().hunger)
			e.setFoodLevel(20);
	}
}
