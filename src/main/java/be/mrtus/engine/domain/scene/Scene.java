package be.mrtus.engine.domain.scene;

import be.mrtus.engine.demo.domain.entity.component.SpinningEntityController;
import be.mrtus.engine.demo.domain.render.model.CubeModel;
import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

	private final List<Entity> entities = new ArrayList<>();
	private final Map<Model, List<Entity>> entityModels = new HashMap<>();

	public Scene() {
		this.addEntity(new Entity.Builder()
				.model(new CubeModel())
				.transform(new Transform.Builder().position(0f, 0f, -2.5f).build())
				.controller(new SpinningEntityController(5f))
				.build());
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
