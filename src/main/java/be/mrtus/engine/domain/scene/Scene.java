package be.mrtus.engine.domain.scene;

import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.render.Texture;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

	private List<Entity> entities = new ArrayList<>();
	private final Entity entity;
	private final Map<Model, List<Entity>> entityModels = new HashMap<>();

	public Map<Model, List<Entity>> getEntityModels() {
		return this.entityModels;
	}

	public List<Entity> getEntities() {
		return this.entities;
	}

	public Scene() {
		float[] positions = new float[]{
			// V0
			-0.5f, 0.5f, 0.5f,
			// V1
			-0.5f, -0.5f, 0.5f,
			// V2
			0.5f, -0.5f, 0.5f,
			// V3
			0.5f, 0.5f, 0.5f,
			// V4
			-0.5f, 0.5f, -0.5f,
			// V5
			0.5f, 0.5f, -0.5f,
			// V6
			-0.5f, -0.5f, -0.5f,
			// V7
			0.5f, -0.5f, -0.5f,
			// For text coords in top face
			// V8: V4 repeated
			-0.5f, 0.5f, -0.5f,
			// V9: V5 repeated
			0.5f, 0.5f, -0.5f,
			// V10: V0 repeated
			-0.5f, 0.5f, 0.5f,
			// V11: V3 repeated
			0.5f, 0.5f, 0.5f,
			// For text coords in right face
			// V12: V3 repeated
			0.5f, 0.5f, 0.5f,
			// V13: V2 repeated
			0.5f, -0.5f, 0.5f,
			// For text coords in left face
			// V14: V0 repeated
			-0.5f, 0.5f, 0.5f,
			// V15: V1 repeated
			-0.5f, -0.5f, 0.5f,
			// For text coords in bottom face
			// V16: V6 repeated
			-0.5f, -0.5f, -0.5f,
			// V17: V7 repeated
			0.5f, -0.5f, -0.5f,
			// V18: V1 repeated
			-0.5f, -0.5f, 0.5f,
			// V19: V2 repeated
			0.5f, -0.5f, 0.5f,};
		float[] textCoords = new float[]{
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.0f,
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			// For text coords in top face
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 1.0f,
			0.5f, 1.0f,
			// For text coords in right face
			0.0f, 0.0f,
			0.0f, 0.5f,
			// For text coords in left face
			0.5f, 0.0f,
			0.5f, 0.5f,
			// For text coords in bottom face
			0.5f, 0.0f,
			1.0f, 0.0f,
			0.5f, 0.5f,
			1.0f, 0.5f,};
		int[] indices = new int[]{
			// Front face
			0, 1, 3, 3, 1, 2,
			// Top Face
			8, 10, 11, 9, 8, 11,
			// Right face
			12, 13, 7, 5, 12, 7,
			// Left face
			6, 15, 14, 6, 14, 4,
			// Bottom face
			19, 18, 16, 19, 16, 17,
			// Back face
			7, 6, 4, 7, 4, 5
		};
		this.entity = new Entity.Builder()
				.model(new Model(positions, textCoords, indices, new Texture()))
				.transform(new Transform.Builder().position(0f, 0f, -2.5f).build())
				.build();
		this.addEntity(this.entity);
	}

	public void addEntity(Entity entity) {
		List<Entity> ents = this.entityModels.get(entity.getModel());
		if(ents == null) {
			ents = new ArrayList<>();
			this.entityModels.put(entity.getModel(), ents);
		}
		ents.add(entity);
		this.entities.add(entity);
	}

	public void update() {
		Transform transform = this.entity.getTransform();
		float rotation = transform.getRotation().x - 1.0f;
		if(rotation > 360) {
			rotation = 0;
		}
		transform.getRotation().set(rotation, rotation, 0);
	}
}
