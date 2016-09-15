
import be.mrtus.gameengine.domain.Display;
import be.mrtus.gameengine.domain.Game;
import be.mrtus.gameengine.domain.GameEngine;
import be.mrtus.gameengine.domain.input.Keyboard;
import be.mrtus.gameengine.domain.input.Mouse;

public class StartUp {

	public static void main(String[] args) {
		new GameEngine(new Game() {

			@Override
			public void destroy() {
			}

			@Override
			public void init(Display display, Keyboard keyboard, Mouse mouse) {
			}

			@Override
			public void render(float delta) {
			}

			@Override
			public void update() {
			}
		}).start();
	}
}
