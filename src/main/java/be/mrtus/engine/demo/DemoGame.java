package be.mrtus.engine.demo;

import be.mrtus.engine.demo.domain.Scene;
import be.mrtus.engine.demo.domain.render.Renderer;
import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.Game;
import be.mrtus.engine.domain.GameEngine;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.Camera;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class DemoGame implements Game {

	public static void main(String[] args) {
		new GameEngine(new DemoGame(), "GameEngine 3D", 800, 600, false, false).start();
	}
	private Camera camera;
	private Display display;
	private Keyboard keyboard;
	private Mouse mouse;
	private Renderer renderer;
	private Scene scene;

	public DemoGame() {
	}

	@Override
	public void destroy() {
		this.renderer.destroy();
	}

	@Override
	public Camera getCamera() {
		return this.camera;
	}

	@Override
	public Scene getScene() {
		return this.scene;
	}

	@Override
	public void init(Display display, Keyboard keyboard, Mouse mouse) throws Exception {
		this.display = display;
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.camera = new Camera(this.keyboard, this.mouse);
		this.renderer = new Renderer();
		this.renderer.init();
		this.scene = new Scene();
	}

	@Override
	public void initKeyboardKeys() {
		this.keyboard.registerKey("forward", GLFW.GLFW_KEY_W);
		this.keyboard.registerKey("backward", GLFW.GLFW_KEY_S);
		this.keyboard.registerKey("left", GLFW.GLFW_KEY_A);
		this.keyboard.registerKey("right", GLFW.GLFW_KEY_D);
		this.keyboard.registerKey("up", GLFW.GLFW_KEY_SPACE);
		this.keyboard.registerKey("down", GLFW.GLFW_KEY_LEFT_CONTROL);
		this.keyboard.registerKey("reset_pos", GLFW.GLFW_KEY_R);
		this.keyboard.registerKey("escape", GLFW.GLFW_KEY_ESCAPE);
	}

	@Override
	public void initMouseButtons() {
	}

	@Override
	public void render(float alpha) {
		this.renderer.clear();
		if(this.display.isResized()) {
			GL11.glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
			this.display.setResized(false);
		}
		this.renderer.render(this.display, this.camera, this.scene, alpha);
	}

	@Override
	public void update() {
		if(this.keyboard.isKeyPressed("escape")) {
			this.mouse.setMouseGrabbed(false);
		}
		if(this.mouse.isLeftButtonPressed() && !this.mouse.isMouseGrabbed()) {
			this.mouse.setMouseGrabbed(true);
		}

		this.camera.update();
		this.scene.update();
	}
}
