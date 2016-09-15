package be.mrtus.gameengine.domain;

public interface Game {

	public void init();

	public void update();

	public void render(float delta);
	
	public void destroy();

}
