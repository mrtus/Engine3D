package be.mrtus.engine.domain.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class Model {

	private int colourVboId;
	private int indexVboId;
	private Texture texture;
	private final int vaoId;
	private int vboId;
	private int vertexCount;

	public Model() {
		this.vaoId = GL30.glGenVertexArrays();
		this.texture = new Texture();
	}

	public void destroy() {
		GL20.glDisableVertexAttribArray(0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.vboId);
		GL15.glDeleteBuffers(this.indexVboId);

		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(this.vaoId);

		if(this.texture != null) {
			this.texture.destroy();
		}
	}

	public void endRender() {
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void render() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.vertexCount, GL11.GL_UNSIGNED_INT, 0);
	}

	public void startRender() {
		GL30.glBindVertexArray(this.vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getId());
	}

	private void setPositions(float[] positions, int[] indices) {
		GL30.glBindVertexArray(this.vaoId);

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(positions.length);
		verticesBuffer.put(positions).flip();

		this.vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		this.vertexCount = indices.length;
		this.indexVboId = GL15.glGenBuffers();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexVboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(0);
	}

	private void setTexture(Texture texture, float[] textureCoordinates) {
		GL30.glBindVertexArray(this.vaoId);

		this.texture = texture;
		this.colourVboId = GL15.glGenBuffers();
		FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(textureCoordinates.length);
		colourBuffer.put(textureCoordinates).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colourVboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colourBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

		GL30.glBindVertexArray(0);
	}

	public static class ModelBuilder {

		private int[] indices;
		private float[] positions;
		private Texture texture;
		private float[] textureCoordinates;

		public Model build() {
			Model model = new Model();
			model.setPositions(this.positions, this.indices);
			model.setTexture(this.texture, this.textureCoordinates);
			return model;
		}

		public ModelBuilder setPositions(float[] positions, int[] indices) {
			this.positions = positions;
			this.indices = indices;
			return this;
		}

		public ModelBuilder setTexture(Texture texture, float[] textureCoordinates) {
			this.texture = texture;
			this.textureCoordinates = textureCoordinates;
			return this;
		}
	}
}
