package me.iphony.gameengine.games.example;

import org.bukkit.Bukkit;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.game.Game;
import me.iphony.gameengine.game.GameType;

public class ExampleGame extends Game
{

	public ExampleGame(GameEngine engine)
	{
		super(engine, GameType.EXAMPLE, 1);
		moveDuringWait = true;
	}

	@Override
	public void onStart()
	{
		getEngine().runRepeatingGameTask(this, new Runnable()
		{
			int i = 15;
			@Override
			public void run()
			{
				if (i != 0) Bukkit.broadcastMessage("Time remaining: " + i);
				else 
				{
					Bukkit.broadcastMessage("End");
					getEngine().getGameManager().stopGame();
				}
				i--;
			}
		}, 20);
	}

	@Override
	public void onStop()
	{
	}

}
