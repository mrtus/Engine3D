package be.mrtus.engine.demo;

import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.Game;
import be.mrtus.engine.domain.GameEngine;
import be.mrtus.engine.domain.input.KeyListener;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import org.lwjgl.glfw.GLFW;

public class DemoGame implements Game, KeyListener {

	public static void main(String[] args) {
		new GameEngine(new DemoGame(), "GameEngine 3D", 800, 600, false, false).start();
	}
	private Display display;
	private Keyboard keyboard;
	private Mouse mouse;
	private ShaderProgram program;
	private Renderer renderer;

	@Override
	public void destroy() {
		this.renderer.destroy();
	}

	@Override
	public void init(Display display, Keyboard keyboard, Mouse mouse) throws Exception {
		this.display = display;
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.keyboard.addKeyListener(this);
		this.renderer = new Renderer(this.display);
		this.renderer.init();
	}

	@Override
	public void keyPressed(int key, int action, int modifier) {
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
			this.display.closeWindow();
		}
	}

	@Override
	public void render(float alpha) {
		this.renderer.render(alpha);
	}

	@Override
	public void update() {
	}
}
