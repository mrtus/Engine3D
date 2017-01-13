package be.mrtus.engine.domain.scene.entity.component;

import be.mrtus.engine.domain.scene.entity.Entity;
import org.joml.Vector3f;

public class Transform {

	protected Entity entity;
	protected Vector3f position;
	protected Vector3f rotation;
	protected Vector3f scale;

	public Transform(Builder builder) {
		this.position = builder.position;
		this.rotation = builder.rotation;
		this.scale = builder.scale;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Vector3f getPosition() {
		return this.position;
	}

	public float getPositionX() {
		return this.position.x;
	}

	public float getPositionY() {
		return this.position.y;
	}

	public float getPositionZ() {
		return this.position.z;
	}

	public Vector3f getRotation() {
		return this.rotation;
	}

	public Vector3f getScale() {
		return this.scale;
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public void update() {
	}

	public static class Builder<B extends Builder> {

		private Vector3f position = new Vector3f(0, 0, 0);
		private Vector3f rotation = new Vector3f(0, 0, 0);
		private Vector3f scale = new Vector3f(1, 1, 1);

		public Transform build() {
			return new Transform(this);
		}

		public B position(float x, float y) {
			this.position.set(x, y, 0);
			return (B)this;
		}

		public B position(float x, float y, float z) {
			this.position.set(x, y, z);
			return (B)this;
		}

		public B position(Vector3f position) {
			this.position = position;
			return (B)this;
		}

		public B rotation(Vector3f rotation) {
			this.rotation = rotation;
			return (B)this;
		}

		public B scale(Vector3f scale) {
			this.scale = scale;
			return (B)this;
		}
	}
}
