package be.mrtus.engine.domain;

import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;

public interface Game {

	public void init(Display display, Keyboard keyboard, Mouse mouse);

	public void update();

	public void render(float delta);

	public void destroy();

}
