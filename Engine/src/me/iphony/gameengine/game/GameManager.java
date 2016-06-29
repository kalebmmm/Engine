package me.iphony.gameengine.game;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.HandlerList;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.exception.EngineExeption;
import me.iphony.gameengine.games.example.ExampleGame;
import me.iphony.gameengine.games.kill.KillGame;
import me.iphony.gameengine.map.GameMap;
import me.iphony.gameengine.state.EndState;
import me.iphony.gameengine.util.UtilWorld;

public class GameManager {

	private GameEngine _engine;
	private Game _currentGame;
	private GameType[] _games = new GameType[] {};
	private GameMap _gameMap;
	
	public GameManager(GameEngine engine)
	{
		_engine = engine;
	}
	
	public void enable()
	{
		selectNextGame();
	}
	
	public Game getCurrentGame()
	{
		return _currentGame;
	}
	
	public void loadGame(GameType type)
	{
		Game game = null;
		
		switch (type)
		{
			case EXAMPLE:
				game = new ExampleGame(_engine);
				break;
				
			case KILL:
				game = new KillGame(_engine);
				break;

			default:
				throw new EngineExeption("Gametype not mapped, cannot create instance of game.");
		}

		_currentGame = game;
		
		if (UtilWorld.hasMaps(_currentGame.getType()))
		{
			File file = UtilWorld.getRandomMap(_currentGame.getType());
			if (UtilWorld.hasConfig(file))
			{
				World world = UtilWorld.make(file);
				File config = UtilWorld.getConfig(file);
				_gameMap = new GameMap(world, config, _currentGame.getType());
			}
		}
	}
	
	public void selectNextGame()
	{
		System.out.println("Selecting next game...");
		
		
		if (_games.length == 0)
			throw new EngineExeption("No gametypes set, cannot load a game.");
		
		loadGame(_games[new Random().nextInt(_games.length)]);
	}
	
	public void startGame()
	{
		_currentGame.start();
		Bukkit.getPluginManager().registerEvents(_currentGame, getEngine());
	}
	
	public void stopGame()
	{
		if (_currentGame != null)
		{
			getEngine().cancelTasks(_currentGame);
			HandlerList.unregisterAll(_currentGame);
			_currentGame.stop();
		}
		
		getEngine().getStateManager().setState(new EndState(getEngine()));
	}
	
	public void setGames(GameType[] games)
	{
		_games = games;
	}
	
	public GameEngine getEngine()
	{
		return this._engine;
	}
	
	public GameMap getGameMap()
	{
		return _gameMap;
	}
	
}
