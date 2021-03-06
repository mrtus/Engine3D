package be.mrtus.engine.demo.domain;

import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.render.Texture;
import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.MovableTransform;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import be.mrtus.engine.domain.scene.terrain.TerrainChunk;
import be.mrtus.engine.domain.scene.terrain.TerrainChunk.TerrainBuilder;
import be.mrtus.engine.domain.scene.terrain.TerrainGenerator;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.math3.util.FastMath;
import org.joml.*;

public class Scene {

	private Camera camera;
	private final List<TerrainChunk> chunks = new ArrayList<>();
	private final Vector3f emptyVector = new Vector3f();
	private final List<Entity> entities = new ArrayList<>();
	private final Map<Model, List<Entity>> entityModels = new HashMap<>();
	private int generateTerrainCount = 0;
	private final TerrainGenerator terrainGenerator;
	private final Vector3f gravity = new Vector3f(0.0f, -9.81f, 0.0f);

	public Scene() {
		this.terrainGenerator = new TerrainGenerator("lalalall");
		float x = 0;
		float y = 0;
		Model cubeModel = new Model.ModelBuilder()
				.setPositions(new float[]{-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f}, new int[]{0, 1, 3, 3, 1, 2, 8, 10, 11, 9, 8, 11, 12, 13, 7, 5, 12, 7, 6, 15, 14, 6, 14, 4, 19, 18, 16, 19, 16, 17, 7, 6, 4, 7, 4, 5})
				.setTexture(new Texture("/grassblock.png"), new float[]{0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 1.0f, 0.5f})
				.build();
		int value = 2;
		double pow = FastMath.pow(value, 2);
		System.out.println("Creating " + pow + " entities!");
		for (int i = 0; i < pow; i++) {
			x = i % value;
			if(i % value == 0) {
				y += 1;
			}
			this.addEntity(new Entity.Builder()
					.model(cubeModel)
					.transform(new Transform.Builder()
							.position(x, 0f, y)
							.build())
					//					.controller(new SpinningEntityController(1f))
					.build());
		}
	}

	public void addEntity(Entity entity) {
		List<Entity> modelEntities = this.entityModels.get(entity.getModel());
		if(modelEntities == null) {
			modelEntities = new ArrayList<>();
			this.entityModels.put(entity.getModel(), modelEntities);
		}
		modelEntities.add(entity);
		this.entities.add(entity);
	}

	public float calculateTerrainHeight(Vector3f position) {
		TerrainChunk terrain = this.findTerrainChunkFor(position);
		if(terrain == null) {
			return 0;
		}
		return terrain.calculateSmoothHeight(position);
	}

	public void destroy() {
		this.terrainGenerator.destroy();
		this.entityModels.keySet().forEach(k -> k.destroy());
	}

	public List<TerrainChunk> findNearbyChunks(Vector3f position) {
		return this.findNearbyChunks(position, 409600);
//		return this.findNearbyChunks(position, 100000);
	}

	public List<TerrainChunk> findNearbyChunks(Vector3f position, int range) {
		return this.chunks.stream().filter(t -> t.getPosition().distanceSquared((int)position.x, (int)position.z) < range).collect(Collectors.toList());
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public List<TerrainChunk> getChunks() {
		return this.chunks;
	}

	public List<Entity> getEntities() {
		return this.entities;
	}

	public Map<Model, List<Entity>> getEntityModels() {
		return this.entityModels;
	}

	public void update() {
		((MovableTransform)this.camera.getTransform()).applyForce(this.gravity);
		this.entities.forEach(e -> {
			if(e.isMovable()) {
				MovableTransform tr = (MovableTransform)e.getTransform();
				tr.applyForce(this.gravity);
			}
			e.update();
		});
		if(this.generateTerrainCount++ % 10 == 0) {
			this.findChunksAroundPlayer();
			this.generateTerrainCount = 0;
		}
	}

	private TerrainChunk findTerrainChunkFor(Vector3f position) {
		return this.chunks.stream().filter(c -> c.contains(position)).findFirst().orElseGet(() -> {
			TerrainChunk chunk = new TerrainBuilder()
					.setPosition(TerrainChunk.calculateChunkPosition(position.x), TerrainChunk.calculateChunkPosition(position.z))
					.build();
			this.terrainGenerator.generateTerrain(chunk);
			this.chunks.add(chunk);
			return chunk;
		});
	}

	private void findChunksAroundPlayer() {
		int chunkRad = 16;
		IntStream.range(-chunkRad, chunkRad).forEach(x -> {
			IntStream.range(-chunkRad, chunkRad).forEach(y -> {
				Vector3f pos = this.camera.getPosition();
				pos.add(x * (TerrainChunk.SIZE), 0, y * (TerrainChunk.SIZE), this.emptyVector);
				this.findTerrainChunkFor(this.emptyVector);
			});
		});
	}
}
