package be.mrtus.engine.demo;

import be.mrtus.engine.demo.domain.render.Renderer;
import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.Game;
import be.mrtus.engine.domain.GameEngine;
import be.mrtus.engine.domain.input.KeyListener;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.render.Mesh;
import be.mrtus.engine.domain.render.Texture;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Model;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.lwjgl.glfw.GLFW;

public class DemoGame implements Game, KeyListener {

	public static void main(String[] args) {
		new GameEngine(new DemoGame(), "GameEngine 3D", 800, 600, false, false).start();
	}
	private Camera camera;
	private Display display;
	private Entity entity;
	private Keyboard keyboard;
	private Mouse mouse;
	private ShaderProgram program;
	private Renderer renderer;

	@Override
	public void destroy() {
		this.renderer.destroy();
	}

	@Override
	public Camera getCamera() {
		return this.camera;
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
		this.camera = new Camera(keyboard, mouse);
		this.renderer = new Renderer(this.display, this);
		this.renderer.init();

		float[] positions = new float[]{
			// V0
			-0.5f, 0.5f, 0.5f,
			// V1
			-0.5f, -0.5f, 0.5f,
			// V2
			0.5f, -0.5f, 0.5f,
			// V3
			0.5f, 0.5f, 0.5f,
			// V4
			-0.5f, 0.5f, -0.5f,
			// V5
			0.5f, 0.5f, -0.5f,
			// V6
			-0.5f, -0.5f, -0.5f,
			// V7
			0.5f, -0.5f, -0.5f,
			// For text coords in top face
			// V8: V4 repeated
			-0.5f, 0.5f, -0.5f,
			// V9: V5 repeated
			0.5f, 0.5f, -0.5f,
			// V10: V0 repeated
			-0.5f, 0.5f, 0.5f,
			// V11: V3 repeated
			0.5f, 0.5f, 0.5f,
			// For text coords in right face
			// V12: V3 repeated
			0.5f, 0.5f, 0.5f,
			// V13: V2 repeated
			0.5f, -0.5f, 0.5f,
			// For text coords in left face
			// V14: V0 repeated
			-0.5f, 0.5f, 0.5f,
			// V15: V1 repeated
			-0.5f, -0.5f, 0.5f,
			// For text coords in bottom face
			// V16: V6 repeated
			-0.5f, -0.5f, -0.5f,
			// V17: V7 repeated
			0.5f, -0.5f, -0.5f,
			// V18: V1 repeated
			-0.5f, -0.5f, 0.5f,
			// V19: V2 repeated
			0.5f, -0.5f, 0.5f,};
		float[] textCoords = new float[]{
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.0f,
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			// For text coords in top face
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 1.0f,
			0.5f, 1.0f,
			// For text coords in right face
			0.0f, 0.0f,
			0.0f, 0.5f,
			// For text coords in left face
			0.5f, 0.0f,
			0.5f, 0.5f,
			// For text coords in bottom face
			0.5f, 0.0f,
			1.0f, 0.0f,
			0.5f, 0.5f,
			1.0f, 0.5f,};
		int[] indices = new int[]{
			// Front face
			0, 1, 3, 3, 1, 2,
			// Top Face
			8, 10, 11, 9, 8, 11,
			// Right face
			12, 13, 7, 5, 12, 7,
			// Left face
			6, 15, 14, 6, 14, 4,
			// Bottom face
			19, 18, 16, 19, 16, 17,
			// Back face
			7, 6, 4, 7, 4, 5
		};
		this.entity = new Entity.Builder()
				.model(new Model(new Mesh(positions, textCoords, indices, new Texture())))
				.transform(new Transform.Builder().position(0f, 0f, -2.5f).build())
				.build();
	}

	@Override
	public void keyPressed(int key, int action, int modifier) {
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
//			this.display.closeWindow();
			this.mouse.setMouseGrabbed(false);
		}
	}

	@Override
	public void render(float alpha) {
		this.renderer.render(alpha);
	}

	@Override
	public void update() {
		this.camera.update();
		Transform transform = this.entity.getTransform();
		float rotation = transform.getRotation().x - 1.0f;
		if(rotation > 360) {
			rotation = 0;
		}
		transform.getRotation().set(rotation, rotation, 0);
	}
}
