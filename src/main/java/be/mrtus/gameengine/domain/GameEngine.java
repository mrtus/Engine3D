package be.mrtus.gameengine.domain;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class GameEngine implements Runnable {

	private final Display display;
	private final Thread engineThread;
	private final Game game;
	private final Timer timer;

	public GameEngine(Game game) {
		this.game = game;
		this.engineThread = new Thread(this, "ENGINE_THREAD");
		this.display = new Display("GameEngine 3D", 800, 600, false, false);
		this.timer = new Timer();
	}

	@Override
	public void run() {
		try {
			this.init();

			float accumulator = 0f;
			final float interval = 1f / 30;
			float alpha;

			while (!this.display.isClosing()) {
				accumulator += this.timer.getDelta();
				while (accumulator >= interval) {
					this.update();
					accumulator -= interval;
				}
				alpha = accumulator / interval;
				this.render(alpha);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.destroy();
		}
	}

	public void start() {
		this.engineThread.start();
	}

	private void destroy() {
		this.game.destroy();
		this.display.destroy();
		GL.destroy();
		GLFW.glfwTerminate();
	}

	private void init() {
		this.display.init();
		this.timer.init();
		this.game.init();
	}

	private void render(float delta) {
		this.game.render(delta);
		this.timer.updateFPS();
		this.timer.update();
		this.display.update();
	}

	private void update() {
		this.game.update();
		this.timer.updateUPS();
	}
}
