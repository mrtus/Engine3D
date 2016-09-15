package be.mrtus.engine.domain.scene.entity.component;

import be.mrtus.engine.domain.scene.entity.Entity;

public class Model<T extends Entity> {

	protected T entity;
	protected Transform transform;

	public void setEntity(T entity) {
		this.entity = entity;
		this.setTransform(entity.getTransform());
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public void render(float alpha) {
	}

	public void update() {
	}
}
