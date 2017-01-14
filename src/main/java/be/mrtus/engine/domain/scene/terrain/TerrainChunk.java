package be.mrtus.engine.domain.scene.terrain;

import org.apache.commons.math3.util.FastMath;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class TerrainChunk {

	public static int calculateChunkPosition(float val) {
		return (int)FastMath.floor(val / TerrainChunk.SIZE) * TerrainChunk.SIZE;
	}

	public static final int SIZE = 40;
	private float[][] heightMap;
	private TerrainModel model;
	private Vector2i position;
	private boolean render = true;

	public float calculateSmoothHeight(Vector3f position) {
		int cx0 = (int)FastMath.floor(position.x);
		int cz0 = (int)FastMath.floor(position.z);
		int x = cx0 % SIZE;
		int z = cz0 % SIZE;
		if(position.x <= 0) {
			x += SIZE;
			if(x == 40) {
				x = 0;
			}
		}
		if(position.z <= 0) {
			z += SIZE;
			if(z == 40) {
				z = 0;
			}
		}
		Vector3f c01 = new Vector3f(cx0, this.getHeight(x, z + 1), cz0 + 1);
		Vector3f c10 = new Vector3f(cx0 + 1, this.getHeight(x + 1, z), cz0);
		Vector3f c0011;
		if(position.z < this.calculateDiagonalZCoordinate(c01.x, c01.z, c10.x, c10.z, position.x)) {
			c0011 = new Vector3f(cx0, this.getHeight(x, z), cz0);
		} else {
			c0011 = new Vector3f(cx0 + 1, this.getHeight(x + 1, z + 1), cz0 + 1);
		}
		return this.interpolateHeight(c01, c0011, c10, position.x, position.z);
	}

	private float calculateDiagonalZCoordinate(float x1, float z1, float x2, float z2, float x) {
		return ((z1 - z2) / (x1 - x2)) * (x - x1) + z1;
	}

	public boolean canRender() {
		return this.model != null && this.render;
	}

	public boolean contains(Vector3f pos) {
		return this.inside(this.position.x, this.position.y, this.position.x + SIZE, this.position.y + SIZE, pos);
	}

	public void setHeightMap(float[][] heightMap) {
		this.heightMap = heightMap;
//		this.printHeightMap();
		System.out.println("Pos height: " + this.calculateSmoothHeight(new Vector3f(1.0f, 0f, 1.0f)));
		System.out.println(new Vector3f(1.0f, -1.0f, 1.0f).lerp(new Vector3f(2.0f, -0.5f, 2.0f), 0.5f));
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

	private boolean between(int x, int x1, float p) {
		return x1 > p && p >= x;
	}

	private float getHeight(int x, int z) {
		if(this.heightMap == null) {
			return 0.0f;
		}
		return this.heightMap[x][z];
	}

	private void setPosition(Vector2i position) {
		this.position = position;
	}

	public void printHeightMap() {
		String line = "";
		for (int x = 0; x < this.heightMap.length; x++) {
			for (int y = 0; y < this.heightMap.length; y++) {
				line += this.heightMap[y][x] + "\t";
			}
			line += "\n";
		}
		line += "=====";
		System.out.println(line);
	}

	private boolean inside(int x, int z, int x1, int z1, Vector3f pos) {
		return this.between(x, x1, pos.x) && this.between(z, z1, pos.z);
	}

	private float interpolateHeight(Vector3f pA, Vector3f pB, Vector3f pC, float x, float z) {
		float a = (pB.y - pA.y) * (pC.z - pA.z) - (pC.y - pA.y) * (pB.z - pA.z);
		float b = (pB.z - pA.z) * (pC.x - pA.x) - (pC.z - pA.z) * (pB.x - pA.x);
		float c = (pB.x - pA.x) * (pC.y - pA.y) - (pC.x - pA.x) * (pB.y - pA.y);
		float d = a * pA.x + b * pA.y + c * pA.z;
		return (d - a * x - c * z) / b;
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
