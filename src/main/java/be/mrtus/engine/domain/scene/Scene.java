package be.mrtus.engine.domain.scene;

import be.mrtus.engine.domain.OBJLoader;
import be.mrtus.engine.domain.render.Material;
import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.render.Texture;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

	private final List<Entity> entities = new ArrayList<>();
	private final Map<Model, List<Entity>> entityModels = new HashMap<>();

	public Scene() throws Exception {
		float x;
		float y = 0;
//		Model cubeModel = new CubeModel();
		Model cubeModel = OBJLoader.loadModel("/models/cube.obj");
		Texture texture = new Texture("/grassblock.png");
		Material material = new Material(texture, 1f);
		cubeModel.setMaterial(material);
		int value = 1;
		double pow = Math.pow(value, 2);
		System.out.println("Creating " + pow + " entities!");
		for (int i = 0; i < pow; i++) {
			x = i % value * 2;
			if(i % value == 0) {
				y += 2;
			}
			this.addEntity(new Entity.Builder()
					.model(cubeModel)
					.transform(new Transform.Builder()
							.position(x, 0f, -y - 10f)
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

	public void destroy() {
		this.entityModels.forEach((model, entities) -> model.destroy());
	}

	public List<Entity> getEntities() {
		return this.entities;
	}

	public Map<Model, List<Entity>> getEntityModels() {
		return this.entityModels;
	}

	public void update() {
		this.entities.forEach(Entity::update);
	}
}
