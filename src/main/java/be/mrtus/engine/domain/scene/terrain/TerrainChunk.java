package be.mrtus.engine.domain.scene.terrain;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class TerrainChunk {

	public static final int SIZE = 41;
	private float[][] heightMap;
	private TerrainModel model;
	private Vector2i position;
	private boolean render = true;

	public float calculateSmoothHeight(Vector3f position) {
		float cx0 = (float)Math.floor(position.x);
		float cz0 = (float)Math.floor(position.z);
		Vector3f c0 = new Vector3f(cx0, this.heightMap[(int)cx0][(int)cz0], cz0);
		Vector3f c1 = new Vector3f(cx0 + 1, this.heightMap[(int)cx0 + 1][(int)cz0 + 1], cz0 + 1);
		Vector3f c05;
		if(position.z < ((c0.x - c0.z) / (c1.x - c1.z)) * (position.x - c1.x) + c0.z) {
			c05 = new Vector3f(cx0 + 1, this.heightMap[(int)cx0 + 1][(int)cz0], cz0);
		} else {
			c05 = new Vector3f(cx0, this.heightMap[(int)cx0][(int)cz0 + 1], cz0 + 1);
		}
		float h = this.interpolateHeight(c0, c1, c05, position.x, position.z);
		System.out.println(h);
		return h;
	}

	public boolean canRender() {
		return this.model != null && this.render;
	}

	public boolean contains(Vector3f pos) {
		return pos.x > this.position.x && pos.z > this.position.y && pos.x < this.position.x + SIZE - 1 && pos.z < this.position.y + SIZE - 1;
	}

	public void setHeightMap(float[][] heightMap) {
		this.heightMap = heightMap;
	}

	public TerrainModel getModel() {
		return this.model;
	}

	public void setModel(TerrainModel model) {
		this.model = model;
	}

	public Vector2i getPosition() {
		return this.position;
	}

	public void printHeightMap() {
		for (int x = 0; x < SIZE; x++) {
			String line = "";
			for (int y = 0; y < SIZE; y++) {
				line += this.heightMap[x][y] + " ";
			}
			System.out.println(line);
		}
	}

	private void setPosition(Vector2i position) {
		this.position = position;
	}

	private float interpolateHeight(Vector3f pA, Vector3f pB, Vector3f pC, float x, float z) {
		float a = (pB.y - pA.y) * (pC.z - pA.z) - (pC.y - pA.y) * (pB.z - pA.z);
		float b = (pB.z - pA.z) * (pC.x - pA.x) - (pC.z - pA.z) * (pB.x - pA.x);
		float c = (pB.x - pA.x) * (pC.y - pA.y) - (pC.x - pA.x) * (pB.y - pA.y);
		float d = -(a * pA.x + b * pA.y + c * pA.z);
		return (-d - a * x - c * z) / b;
	}

	public static final class TerrainBuilder {

		private Vector2i position = new Vector2i();

		public TerrainBuilder() {
		}

		public TerrainChunk build() {
			TerrainChunk terrain = new TerrainChunk();
			terrain.setPosition(position);
			return terrain;
		}

		public TerrainBuilder setPosition(int x, int y) {
			this.position.set(x, y);
			return this;
		}
	}
}
