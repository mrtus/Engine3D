package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.Scene;
import be.mrtus.engine.domain.scene.entity.Entity;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class Renderer {

	private final float FOV = (float)Math.toRadians(60.0f);
	private final float Z_FAR = 1000.f;
	private final float Z_NEAR = 0.01f;
	private ShaderProgram shaderProgram;
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

	public void init() throws Exception {
		this.shaderProgram = new ShaderProgram();
		this.shaderProgram.createVertexShader(this.loadResource("/shaders/vertex.vs"));
		this.shaderProgram.createFragmentShader(this.loadResource("/shaders/fragment.fs"));
		this.shaderProgram.link();
		this.shaderProgram.createUniform("projectionMatrix");
		this.shaderProgram.createUniform("modelViewMatrix");
		this.shaderProgram.createUniform("texture_sampler");
	}

	public String loadResource(String fileName) throws Exception {
		return new String(Files.readAllBytes(Paths.get(Renderer.class.getResource(fileName).toURI())));
	}

	public void render(Display display, Camera camera, Scene scene, float alpha) {
		this.clear();

		if(display.isResized()) {
			GL11.glViewport(0, 0, display.getWidth(), display.getHeight());
			display.setResized(false);
		}

		Matrix4f projectionMatrix = this.transformation.getProjectionMatrix(this.FOV, display.getWidth(), display.getHeight(), this.Z_NEAR, this.Z_FAR);
		Matrix4f viewMatrix = this.transformation.getViewMatrix(camera);
		this.renderScene(scene, projectionMatrix, viewMatrix);
	}

	public void renderModel(Model model, List<Entity> entities, Matrix4f viewMatrix) {
		this.shaderProgram.setUniform("texture_sampler", 0);

		model.startRender();
		entities.forEach(entity -> {
			Matrix4f modelViewMatrix = this.transformation.getModelViewMatrix(entity.getTransform(), viewMatrix);
			this.shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			model.render();
		});
		model.endRender();
	}

	private void renderScene(Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
		this.shaderProgram.bind();
		this.shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		boolean showWireFrame = false;
		if(showWireFrame) {
			GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
		}
		Map<Model, List<Entity>> modelEntities = scene.getEntityModels();
		modelEntities.forEach((model, entities) -> this.renderModel(model, entities, viewMatrix));
		if(showWireFrame) {
			GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		}
		this.shaderProgram.unbind();
	}
}
