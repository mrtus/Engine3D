package be.mrtus.engine.domain.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class Model {

	private Vector3f colour;
	private final int colourVboId;
	private final int indexVboId;
	private final Material material;
	private final Texture texture;
	private final int normalsVboId;
	private Texture texture;
	private final int vaoId;
	private final int vboId;
	private final int vertexCount;

	public Model(float[] positions, float[] textCoords, float[] normals, int[] indices) {
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
		FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(textCoords.length);
		colourBuffer.put(textCoords).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colourVboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colourBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

		this.normalsVboId = GL15.glGenBuffers();
		FloatBuffer vecNormalsBuffer = BufferUtils.createFloatBuffer(normals.length);
		vecNormalsBuffer.put(normals).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.normalsVboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vecNormalsBuffer, GL15.GL_STATIC_DRAW);

		this.indexVboId = GL15.glGenBuffers();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexVboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

		GL30.glBindVertexArray(0);
	}

	public Model() {
		this.colourVboId = 0;
		this.normalsVboId = 0;
		this.indexVboId = 0;
		this.vaoId = 0;
		this.vboId = 0;
		this.vertexCount = 0;
		this.texture = null;
		this.material = null;
	}

	public void destroy() {
		GL20.glDisableVertexAttribArray(0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.vboId);
		GL15.glDeleteBuffers(this.indexVboId);
		GL15.glDeleteBuffers(this.colourVboId);
		GL15.glDeleteBuffers(this.normalsVboId);

		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(this.vaoId);

		if(this.texture != null) {
			this.texture.cleanup();
		}
	}

	public void endRender() {
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		if(this.texture != null) {
			this.texture.unbind();
		}
	}

	public Vector3f getColour() {
		return this.colour;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public boolean isTextured() {
		return this.texture != null;
	}

	public Material getMaterial() {
		return this.material;
	}

	public void render() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.vertexCount, GL11.GL_UNSIGNED_INT, 0);
	}

	public void startRender() {
		GL30.glBindVertexArray(this.vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		if(this.texture != null) {
			this.texture.bind();
		}
	}
}
