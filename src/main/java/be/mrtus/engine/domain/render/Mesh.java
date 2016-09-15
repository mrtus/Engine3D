package be.mrtus.engine.domain.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {

	private final int colourVboId;
	private final int indexVboId;
	private final int vaoId;
	private final int vboId;
	private final int vertexCount;

	public Mesh(float[] positions, float[] colours, int[] indices) {
		this.vertexCount = indices.length;
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(positions.length);
		verticesBuffer.put(positions).flip();

		this.vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(this.vaoId);

		this.vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		this.colourVboId = GL15.glGenBuffers();
		FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(colours.length);
		colourBuffer.put(colours).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colourVboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colourBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

		this.indexVboId = GL15.glGenBuffers();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexVboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(0);
	}

	public Mesh() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void destroy() {
		GL20.glDisableVertexAttribArray(0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.vboId);
		GL15.glDeleteBuffers(this.indexVboId);

		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(this.vaoId);
	}

	public void endRender() {
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public void render() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.vertexCount, GL11.GL_UNSIGNED_INT, 0);
	}

	public void startRender() {
		GL30.glBindVertexArray(this.vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
}