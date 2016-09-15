package be.mrtus.engine.demo;

import be.mrtus.engine.demo.domain.render.Renderer;
import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.Game;
import be.mrtus.engine.domain.GameEngine;
import be.mrtus.engine.domain.input.KeyListener;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.render.Mesh;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Model;
import be.mrtus.engine.domain.scene.entity.component.Transform;
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
	private Entity entity;

	@Override
	public void destroy() {
		this.renderer.destroy();
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	@Override
	public void init(Display display, Keyboard keyboard, Mouse mouse) throws Exception {
		this.display = display;
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.keyboard.addKeyListener(this);
		this.renderer = new Renderer(this.display, this);
		this.renderer.init();

		float[] positions = new float[]{
			-0.5f, 0.5f, 0f,
			-0.5f, -0.5f, 0f,
			0.5f, -0.5f, 0f,
			0.5f, 0.5f, 0f
		};
		int[] indices = new int[]{
			0, 1, 3, 3, 1, 2
		};
		float[] colours = new float[]{
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f
		};
		this.entity = new Entity.Builder()
				.model(new Model(new Mesh(positions, colours, indices)))
				.transform(new Transform.Builder().position(0f, 0f, -2.5f).build())
				.build();
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
		Transform transform = this.entity.getTransform();
		float rotation = transform.getRotation().z - 1.0f;
		if(rotation > 360) {
			rotation = 0;
		}
		transform.getRotation().set(rotation, rotation, rotation);
	}
}
