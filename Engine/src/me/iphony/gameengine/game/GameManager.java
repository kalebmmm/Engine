package me.iphony.gameengine.game;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import me.iphony.gameengine.GameEngine;
import me.iphony.gameengine.exception.EngineExeption;
import me.iphony.gameengine.games.example.ExampleGame;
import me.iphony.gameengine.state.EndState;

public class GameManager {

	private GameEngine _engine;
	private Game _currentGame;
	private GameType[] _games = new GameType[] {};
	
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

			default:
				throw new EngineExeption("Gametype not mapped, cannot create instance of game.");
		}
		
		_currentGame = game;
	}
	
	public void selectNextGame()
	{
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
		selectNextGame();
	}
	
	public void setGames(GameType[] games)
	{
		_games = games;
	}
	
	public GameEngine getEngine()
	{
		return this._engine;
	}
	
}
