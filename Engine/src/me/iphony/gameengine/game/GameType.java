package me.iphony.gameengine.game;

public enum GameType {

	KILL("Last Man Standing", "kill"),
	EXAMPLE("Example Game", "example");
	

	public String name;
	public String mapPrefix;
	
	private GameType(String name, String mapPrefix)
	{
		this.name = name;
		this.mapPrefix = mapPrefix;
	}
	
}
