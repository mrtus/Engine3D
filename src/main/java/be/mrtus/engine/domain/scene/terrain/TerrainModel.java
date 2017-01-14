package be.mrtus.engine.domain.scene.terrain;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class TerrainModel {

	private int colourVboId;
	private float[] colours;
	private int indexVboId;
	private int[] indices;
	private boolean initiated;
	private float[] positions;
	private int vaoId;
	private int vboId;
	private int vertexCount;

	private TerrainModel() {
	}

	public void destroy() {
		GL20.glDisableVertexAttribArray(0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.vboId);
		GL15.glDeleteBuffers(this.indexVboId);
		GL15.glDeleteBuffers(this.colourVboId);

		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(this.vaoId);
	}

	public void endRender() {
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public int[] getIndices() {
		return this.indices;
	}

	public float[] getPositions() {
		return this.positions;
	}

	public void init() {
		if(!this.initiated) {
			this.initiated = true;
			this.vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(this.vaoId);

			FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(this.positions.length);
			verticesBuffer.put(this.positions).flip();

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

			this.colourVboId = GL15.glGenBuffers();
			FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(colours.length);
			colourBuffer.put(colours).flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.colourVboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colourBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

			GL30.glBindVertexArray(0);
		}
	}

	public boolean isInitiated() {
		return this.initiated;
	}

	public void render() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.vertexCount, GL11.GL_UNSIGNED_INT, 0);
	}

	public void startRender() {
		GL30.glBindVertexArray(this.vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}

	private void setColours(float[] colours) {
		this.colours = colours;
	}

	private void setPositions(float[] positions, int[] indices) {
		this.positions = positions;
		this.indices = indices;
	}

	public static class TerrainModelBuilder {

		private float[] colours;
		private int[] indices;
		private float[] positions;

		public TerrainModel build() {
			TerrainModel model = new TerrainModel();
			model.setPositions(this.positions, this.indices);
			if(this.colours == null) {
				this.colours = this.calculateColours(this.positions);
			}
			model.setColours(this.colours);
			return model;
		}

		public TerrainModelBuilder setColours(float[] colours) {
			this.colours = colours;
			return this;
		}

		public TerrainModelBuilder setPositions(float[] positions, int[] indices) {
			this.positions = positions;
			this.indices = indices;
			return this;
		}

		private float[] calculateColours(float[] positions) {
			float[] colours = new float[positions.length];
//			Random random = new Random();
			float rgb = 0.0175f;
			for (int i = 0; i < colours.length; i += 3) {
//				colours[i] = random.nextFloat();
//				colours[i + 1] = random.nextFloat();
//				colours[i + 2] = random.nextFloat();
//				colours[i] = positions[i + 1] * rgb;
//				colours[i + 1] = positions[i + 1] * rgb;
//				colours[i + 2] = positions[i + 1] * rgb;
				float h = positions[i + 1];
				if(h >= 0 && h <= 32) {
					colours[i] = 0.0f;
					colours[i + 1] = h / 32f;
					colours[i + 2] = 1.0f - (h / 32f);
				} else if(h > 32 && h <= 64) {
					colours[i] = (h - 31f) / 32f;
					colours[i + 1] = 1.0f - ((h - 31f) / 32f);
					colours[i + 2] = 0.0f;
				}
			}
			return colours;
		}
	}
}
