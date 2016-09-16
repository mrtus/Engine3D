package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.Scene;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.light.PointLight;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class Renderer {

	private final float FOV = (float)Math.toRadians(60.0f);
	private final float Z_FAR = 1000.f;
	private final float Z_NEAR = 0.01f;
	private ShaderProgram shaderProgram;
	private final Transformation transformation;
	private PointLight pointLight;

	public Renderer() {
		this.transformation = new Transformation();

		Vector3f lightColour = new Vector3f(1, 1, 1);
		Vector3f lightPosition = new Vector3f(0, 0, 1);
		this.pointLight = new PointLight(lightColour, lightPosition, 1f);
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
		this.shaderProgram.createMaterialUniform("material");
//		this.shaderProgram.createUniform("camera_pos");
//		this.shaderProgram.createUniform("specularPower");
//		this.shaderProgram.createUniform("ambientLight");
//		this.shaderProgram.createPointLightUniform("pointLight");
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
		this.renderScene(camera, scene, projectionMatrix, viewMatrix);
	}

	public void renderModel(Model model, List<Entity> entities, Matrix4f viewMatrix) {
		this.shaderProgram.setUniform("material", model.getMaterial());

		model.startRender();
		entities.forEach(entity -> {
			Matrix4f modelViewMatrix = this.transformation.getModelViewMatrix(entity.getTransform(), viewMatrix);
			this.shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			model.render();
		});
		model.endRender();
	}

	private void renderScene(Camera camera, Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
		this.shaderProgram.bind();
		this.shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		this.shaderProgram.setUniform("texture_sampler", 0);

//		this.shaderProgram.setUniform("camera_pos", camera.getPosition());
//		this.shaderProgram.setUniform("ambientLight", new Vector3f(0, 0, 0));
//		this.shaderProgram.setUniform("specularPower", 10f);
//		Vector3f lightPos = new Vector3f(this.pointLight.getPosition());
//		Vector4f aux = new Vector4f(lightPos, 1);
//		aux.mul(viewMatrix);
//		lightPos.x = aux.x;
//		lightPos.y = aux.y;
//		lightPos.z = aux.z;
//		this.shaderProgram.setUniform("pointLight", this.pointLight);
//		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
		Map<Model, List<Entity>> modelEntities = scene.getEntityModels();
		modelEntities.forEach((model, entities) -> this.renderModel(model, entities, viewMatrix));

//		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		this.shaderProgram.unbind();
	}
}
