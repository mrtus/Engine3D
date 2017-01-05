package be.mrtus.engine.domain.scene.entity;

import be.mrtus.engine.domain.render.Model;
import be.mrtus.engine.domain.scene.entity.component.EntityController;
import be.mrtus.engine.domain.scene.entity.component.MovableTransform;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.joml.Vector3f;

public class Entity {

	protected EntityController controller;
	protected double mass = 0.0;
	protected Model model;
	protected Transform transform;

	protected Entity() {
		this(new Entity.Builder());
	}

	protected Entity(Builder builder) {
		this.setController(builder.controller);
		this.setModel(builder.model);
		this.setTransform(builder.transform);
	}

	public void setController(EntityController controller) {
		if(controller != null) {
			this.controller = controller;
			this.controller.setEntity(this);
		}
	}

	public double getMass() {
		return this.mass;
	}

//	public boolean isMovable() {
//		return this.transform instanceof MovableTransform;
//	}
	public Model getModel() {
		return this.model;
	}

	public void setModel(Model model) {
		if(model != null) {
			this.model = model;
		}
	}

	public Vector3f getPosition() {
		return this.transform.getPosition();
	}

	public float getPositionX() {
		return this.getPosition().x;
	}

	public float getPositionY() {
		return this.getPosition().y;
	}

	public Transform getTransform() {
		return this.transform;
	}

	public void setTransform(Transform transform) {
		if(transform != null) {
			this.transform = transform;
			this.transform.setEntity(this);
		}
	}

	public boolean isMovable() {
		return this.transform instanceof MovableTransform;
	}

	public void update() {
		this.controller.update();
		this.transform.update();
	}

	public static class Builder<B extends Builder> {

		private EntityController controller = new EntityController();
		private Model model = new Model();
		private Transform transform;

		public Entity build() {
			return new Entity(this);
		}

		public B controller(EntityController controller) {
			this.controller = controller;
			return (B)this;
		}

		public B model(Model model) {
			this.model = model;
			return (B)this;
		}

		public B transform(Transform transform) {
			this.transform = transform;
			return (B)this;
		}
	}
}
