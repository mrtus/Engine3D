package be.mrtus.engine.domain;

import be.mrtus.engine.domain.input.KeyListener;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class GameEngine implements Runnable, KeyListener {

	private final Display display;
	private final Thread engineThread;
	private final Game game;
	private final Keyboard keyboard;
	private final Mouse mouse;
	private final Timer timer;

	public GameEngine(Game game, String title, int width, int height, boolean fullscreen, boolean vsync) {
		this.game = game;
		this.display = new Display(title, width, height, fullscreen, vsync);
		this.timer = new Timer();
		this.mouse = new Mouse(this.display);
		this.keyboard = new Keyboard(this.display);
		this.keyboard.addKeyListener(this);
		this.engineThread = new Thread(this, "ENGINE_THREAD");
	}

	@Override
	public void keyPressed(int key, int action, int modifier) {
		if(key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_RELEASE) {
			this.toggleFullscreen();
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
		this.keyboard.destroy();
		this.mouse.destroy();
	}

	private void init() throws Exception {
		this.display.init();
		this.timer.init();
		this.initInput();
		this.game.init(this.display, this.keyboard, this.mouse);
		this.game.initKeyboardKeys();
		this.game.initMouseButtons();
	}

	private void initInput() {
		this.mouse.init();
		this.keyboard.init();
	}

	private void render(float alpha) {
		this.game.render(alpha);
		this.timer.updateFPS();
		this.display.update();
	}

	private void toggleFullscreen() {
		this.display.toggleFullscreen();
		this.destroyInput();
		this.initInput();
	}

	private void update() {
		this.mouse.update();
		this.game.update();
		this.timer.updateUPS();
		this.timer.update();
	}
}
