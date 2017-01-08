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

public class DemoGame implements Game {

	public static void main(String[] args) {
		new GameEngine(new DemoGame(), "GameEngine 3D", 800, 600, false, false).start();

//		Vector3f pos = new Vector3f(0, 0, 0);
//		Vector3f rot = new Vector3f(0, -90 - 35, 0);
//		Vector3f obj = new Vector3f(-10, 0, 0);
//		float x = (float)(FastMath.cos(FastMath.toRadians(rot.x)) * FastMath.cos(FastMath.toRadians(rot.y)));
//		float y = (float)(FastMath.sin(FastMath.toRadians(rot.x)) * FastMath.cos(FastMath.toRadians(rot.y)));
//		float z = (float)FastMath.sin(FastMath.toRadians(rot.y));
//		System.out.println(obj.negate(pos).normalize().dot(x, y, z));
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
		this.scene = new Scene();
		this.camera = new Camera(this.keyboard, this.mouse, this.scene);
		this.scene.setCamera(this.camera);
		this.renderer = new Renderer();
		this.keyboard.addKeyListener(this.renderer);
		this.renderer.init(this.display);
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
		this.keyboard.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT);
		this.keyboard.registerKey("noclip", GLFW.GLFW_KEY_F5);
	}

	@Override
	public void initMouseButtons() {
	}

	@Override
	public void render(float alpha) {
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
