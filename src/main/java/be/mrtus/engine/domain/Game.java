package be.mrtus.engine.domain;

import be.mrtus.engine.demo.domain.Scene;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.Camera;

public interface Game {

	public void destroy();

	public Camera getCamera();

	public Scene getScene();

	public void init(Display display, Keyboard keyboard, Mouse mouse, Timer timer) throws Exception;

	public void render(float alpha);

	public void update();

	public void initKeyboardKeys();

	public void initMouseButtons();
}
