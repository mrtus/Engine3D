package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.render.Mesh;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Model;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class Renderer {

	private final float FOV = (float)Math.toRadians(60.0f);
	private final float Z_FAR = 1000.f;
	private final float Z_NEAR = 0.01f;
	private final Display display;
	private Entity entity;
	private ShaderProgram shaderProgram;
	private final Transformation transformation;

	public Renderer(Display display) {
		this.display = display;
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
		this.shaderProgram.createUniform("worldMatrix");

		float[] positions = new float[]{-0.5f, 0.5f, -1.05f, -0.5f, -0.5f, -1.05f, 0.5f, -0.5f, -1.05f, 0.5f, 0.5f, -1.05f,};
		int[] indices = new int[]{0, 1, 3, 3, 1, 2,};
		float[] colours = new float[]{0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 0.5f,};
		this.entity = new Entity.Builder().model(new Model(new Mesh(positions, colours, indices))).build();
	}

	public String loadResource(String fileName) throws Exception {
		return new String(Files.readAllBytes(Paths.get(Renderer.class.getResource(fileName).toURI())));
	}

	public void render(float alpha) {
		this.clear();
		if(this.display.isResized()) {
			GL11.glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
			this.display.setResized(false);
		}
		this.renderEntity(this.entity);
	}

	public void renderEntity(Entity entity) {
		this.shaderProgram.bind();
		Matrix4f projectionMatrix = this.transformation.getProjectionMatrix(this.FOV, this.display.getWidth(), this.display.getHeight(), this.Z_NEAR, this.Z_FAR);
		this.shaderProgram.setUniform("projectionMatrix", projectionMatrix);

		Matrix4f worldMatrix = this.transformation.getWorldMatrix(entity.getTransform());
		this.shaderProgram.setUniform("worldMatrix", worldMatrix);

		Mesh mesh = entity.getMesh();
		mesh.startRender();
		mesh.render();
		mesh.endRender();

		this.shaderProgram.unbind();
	}
}
