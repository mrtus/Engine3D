package be.mrtus.gameengine.domain;

import be.mrtus.gameengine.domain.input.Keyboard;
import be.mrtus.gameengine.domain.input.Mouse;

public interface Game {

	public void init(Display display, Keyboard keyboard, Mouse mouse);

	public void update();

	public void render(float delta);

	public void destroy();

}
