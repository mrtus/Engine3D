package be.mrtus.engine.demo;

import be.mrtus.engine.demo.domain.render.Renderer;
import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.Game;
import be.mrtus.engine.domain.GameEngine;
import be.mrtus.engine.domain.input.KeyListener;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.Scene;
import org.lwjgl.glfw.GLFW;

public class DemoGame implements Game, KeyListener {

	public static void main(String[] args) {
		new GameEngine(new DemoGame(), "GameEngine 3D", 800, 600, false, false).start();
	}
	private Camera camera;
	private Display display;
	private Keyboard keyboard;
	private Mouse mouse;
	private Renderer renderer;
	private Scene scene;

	@Override
	public void destroy() {
		this.renderer.destroy();
		this.scene.destroy();
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
		this.keyboard.addKeyListener(this);
		this.scene = new Scene();
		this.camera = new Camera(keyboard, mouse);
		this.renderer = new Renderer();
		this.renderer.init();
	}

	@Override
	public void keyPressed(int key, int action, int modifier) {
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
			this.mouse.setMouseGrabbed(false);
		}
	}

	@Override
	public void render(float alpha) {
		this.renderer.render(this.display, this.camera, this.scene, alpha);
	}

	@Override
	public void update() {
		if(this.mouse.isLeftButtonPressed() && !this.mouse.isMouseGrabbed()) {
			this.mouse.setMouseGrabbed(true);
		}
		if(this.mouse.isMiddleButtonPressed()) {
			this.camera.reset();
		}

		this.camera.update();
		this.scene.update();
	}
}
