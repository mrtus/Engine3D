package be.mrtus.engine.domain.scene.entity.component;

import be.mrtus.engine.domain.render.Mesh;
import be.mrtus.engine.domain.scene.entity.Entity;

public class Model<T extends Entity> {

	protected T entity;
	protected Mesh mesh;
	protected Transform transform;

	public Model(Mesh mesh) {
		this.mesh = mesh;
	}

	public void setEntity(T entity) {
		this.entity = entity;
		this.setTransform(entity.getTransform());
	}

	public Mesh getMesh() {
		return this.mesh;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public void render(float alpha) {
	}

	public void update() {
	}
}
