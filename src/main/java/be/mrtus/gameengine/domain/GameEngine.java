package be.mrtus.gameengine.domain;

import be.mrtus.gameengine.domain.input.KeyListener;
import be.mrtus.gameengine.domain.input.Keyboard;
import be.mrtus.gameengine.domain.input.Mouse;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class GameEngine implements Runnable, KeyListener {

	private final Display display;
	private final Thread engineThread;
	private final Game game;
	private final Keyboard keyboard;
	private final Mouse mouse;
	private final Timer timer;

	public GameEngine(Game game) {
		this.game = game;
		this.engineThread = new Thread(this, "ENGINE_THREAD");
		this.display = new Display("GameEngine 3D", 800, 600, false, false);
		this.timer = new Timer();
		this.mouse = new Mouse(this.display);
		this.keyboard = new Keyboard(this.display);
		this.keyboard.addKeyListener(this);
	}

	@Override
	public void keyPressed(int key, int action, int modifier) {
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
			this.display.closeWindow();
		} else if(key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_RELEASE) {
			this.display.toggleFullscreen();
//			this.destroyInput();
//			this.initInput();
		}
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
					accumulator -= interval;
					this.update();
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
		this.destroyInput();
		this.display.destroy();
		GL.destroy();
		GLFW.glfwTerminate();
	}

	private void destroyInput() {
		this.mouse.destroy();
		this.keyboard.destroy();
	}

	private void init() {
		this.display.init();
		this.timer.init();
		this.initInput();
		this.game.init(this.display, this.keyboard, this.mouse);
	}

	private void initInput() {
		this.mouse.init();
		this.keyboard.init();
	}

	private void render(float delta) {
		this.game.render(delta);
		this.timer.updateFPS();
		this.display.update();
	}

	private void update() {
		this.game.update();
		this.timer.updateUPS();
		this.timer.update();
	}
}
