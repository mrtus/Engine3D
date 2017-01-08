package be.mrtus.engine.domain.scene.terrain;

import org.apache.commons.math3.util.FastMath;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class TerrainChunk {

	public static final int SIZE = 41;
	private float[][] heightMap;
	private TerrainModel model;
	private Vector2i position;
	private boolean render = true;

	public float calculateSmoothHeight(Vector3f position) {
		float cx0 = (float)FastMath.abs(FastMath.floor(position.x));
		float cz0 = (float)FastMath.abs(FastMath.floor(position.z));
		Vector3f c0 = new Vector3f(cx0, this.getHeight(cx0, cz0), cz0);
		Vector3f c1 = new Vector3f(cx0 + 1, this.getHeight(cx0 + 1, cz0 + 1), cz0 + 1);
		Vector3f c05;
		if(position.z < ((((c0.z - c1.z) / (c0.x - c1.x)) * (position.x - c1.x)) + c1.z)) {
			c05 = new Vector3f(cx0, this.getHeight(cx0, cz0 + 1), cz0 + 1);
		} else {
			c05 = new Vector3f(cx0 + 1, this.getHeight(cx0 + 1, cz0), cz0);
		}
		return this.interpolateHeight(c0, c1, c05, position.x, position.z);
	}

	public boolean canRender() {
		return this.model != null && this.render;
	}

	public boolean contains(Vector3f pos) {
		return pos.x >= this.position.x && pos.z >= this.position.y && pos.x < this.position.x + SIZE && pos.z < this.position.y + SIZE;
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
		String line = "";
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				line += this.heightMap[x][y] + " ";
			}
			line += "\n";
		}
		line += "=====";
		System.out.println(line);
	}

	private float getHeight(float x, float z) {
		if(this.heightMap == null) {
			return 0;
		}
		return this.heightMap[(int)x % SIZE][(int)z % SIZE];
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
