package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.demo.domain.Scene;
import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.input.KeyListener;
import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.terrain.TerrainChunk;
import be.mrtus.engine.domain.scene.terrain.TerrainModel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.math3.util.FastMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Renderer implements KeyListener {

	private final float FOV = (float)FastMath.toRadians(60.0f);
	private final float Z_FAR = 1000.f;
	private final float Z_NEAR = 0.01f;
	private ShaderProgram shaderProgram;
	private boolean showWireFrame = false;
	private ShaderProgram terrainShaderProgram;
	private final Transformation transformation;

	public Renderer() {
		this.transformation = new Transformation();
	}

	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void destroy() {
		if(this.shaderProgram != null) {
			this.shaderProgram.destroy();
		}
	}

	public void init(Display display) throws Exception {
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.createVertexShader(this.loadResource("/shaders/vertex.vs"));
		this.shaderProgram.createFragmentShader(this.loadResource("/shaders/fragment.fs"));
		this.shaderProgram.link();
		this.shaderProgram.createUniform("projectionMatrix");
		this.shaderProgram.createUniform("modelViewMatrix");
		this.shaderProgram.createUniform("texture_sampler");

		this.terrainShaderProgram = new ShaderProgram();
		this.terrainShaderProgram.createVertexShader(this.loadResource("/shaders/terrain/vertex.vs"));
		this.terrainShaderProgram.createFragmentShader(this.loadResource("/shaders/terrain/fragment.fs"));
		this.terrainShaderProgram.link();
		this.terrainShaderProgram.createUniform("projectionMatrix");
		this.terrainShaderProgram.createUniform("modelViewMatrix");

		this.transformation.setProjectionMatrix(this.FOV, display.getWidth(), display.getHeight(), this.Z_NEAR, this.Z_FAR);
	}

	@Override
	public void keyPressed(int key, int action, int modifier) {
		if(key == GLFW.GLFW_KEY_F3 && action == GLFW.GLFW_RELEASE) {
			this.showWireFrame = !this.showWireFrame;
		}
	}

	public String loadResource(String fileName) throws Exception {
		return new String(Files.readAllBytes(Paths.get(Renderer.class.getResource(fileName).toURI())));
	}

	public void render(Display display, Camera camera, Scene scene, float alpha) {
		if(display.isResized()) {
			this.transformation.setProjectionMatrix(this.FOV, display.getWidth(), display.getHeight(), this.Z_NEAR, this.Z_FAR);
		}
		this.transformation.setViewMatrix(camera);

		this.shaderProgram.bind();
		this.shaderProgram.setUniform("projectionMatrix", this.transformation.getProjectionMatrix());
		Model model = camera.getModel();
		model.startRender();
		Matrix4f modelViewMatrix = this.transformation.getModelViewMatrix(new Vector3f(0f, -2.3f, 0f).add(camera.getTransform().getPosition()));
		this.shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
		model.render();
		model.endRender();
		this.shaderProgram.unbind();
		this.renderScene(scene);
	}

	public void renderModel(Model model, List<Entity> entities) {
		this.shaderProgram.setUniform("texture_sampler", 0);
		model.startRender();
		entities.stream()
				.filter(e -> e.canRender())
				.forEach(entity -> {
					Matrix4f modelViewMatrix = this.transformation.getModelViewMatrix(entity.getTransform());
					this.shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
					model.render();
				});
		model.endRender();
	}

	public void update() {

	}

	private void renderScene(Scene scene) {
		if(this.showWireFrame) {
			GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
		}
		this.shaderProgram.bind();
		this.shaderProgram.setUniform("projectionMatrix", this.transformation.getProjectionMatrix());
		scene.getEntityModels().forEach((model, entities) -> this.renderModel(model, entities));
		this.shaderProgram.unbind();

		this.terrainShaderProgram.bind();
		this.terrainShaderProgram.setUniform("projectionMatrix", this.transformation.getProjectionMatrix());
		scene.getChunks().forEach(c -> this.renderTerrain(c));
		this.terrainShaderProgram.unbind();
		if(this.showWireFrame) {
			GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		}
	}

	private void renderTerrain(TerrainChunk chunk) {
		if(chunk.canRender()) {
			TerrainModel model = chunk.getModel();
			if(!model.isInitiated()) {
				model.init();
			}
			model.startRender();
			Matrix4f modelViewMatrix = this.transformation.getModelViewMatrix(chunk.getPosition());
			this.shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			model.render();
			model.endRender();
		}
	}
}
