package be.mrtus.engine.demo;

import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.Utils;
import be.mrtus.engine.domain.render.Mesh;
import be.mrtus.engine.domain.render.shader.ShaderProgram;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

	private Mesh mesh;
	private ShaderProgram shaderProgram;

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
		this.shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
		this.shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
		this.shaderProgram.link();

		float[] positions = new float[]{-0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f,};
		int[] indices = new int[]{0, 1, 3, 3, 1, 2,};
		float[] colours = new float[]{0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.5f, 0.5f,};
		this.mesh = new Mesh(positions, colours, indices);
	}

	public void render(Display display, float alpha) {
		this.clear();
		if(display.isResized()) {
			GL11.glViewport(0, 0, display.getWidth(), display.getHeight());
			display.setResized(false);
		}
		this.renderMesh(this.mesh);
	}

	public void renderMesh(Mesh mesh) {
		shaderProgram.bind();

		GL30.glBindVertexArray(mesh.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		shaderProgram.unbind();
	}
}
