package be.mrtus.engine.domain;

import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.Entity;

public interface Game {

	public Entity getEntity();

	public void init(Display display, Keyboard keyboard, Mouse mouse) throws Exception;

	public void update();

	public void render(float alpha);

	public void destroy();

}
