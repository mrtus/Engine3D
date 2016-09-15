
import be.mrtus.gameengine.domain.Game;
import be.mrtus.gameengine.domain.GameEngine;

public class StartUp {

	public static void main(String[] args) {
		new GameEngine(new Game() {

			@Override
			public void destroy() {
			}

			@Override
			public void init() {
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
