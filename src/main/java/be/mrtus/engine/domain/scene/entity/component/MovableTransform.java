package be.mrtus.engine.domain.scene.entity.component;

import be.mrtus.engine.domain.Util;
import org.joml.Vector3f;

public class MovableTransform extends Transform {

	protected final Vector3f acceleration;
	protected float maxSpeed;
	protected float slowDownDistance;
	protected final Vector3f velocity;

	public MovableTransform(Builder builder) {
		super(builder);
		this.acceleration = builder.acceleration;
		this.maxSpeed = builder.maxSpeed;
		this.velocity = builder.velocity;
		this.slowDownDistance = builder.slowDownDistance;
	}

	public void applyForce(Vector3f force) {
		Vector3f forceCopy = new Vector3f(force);
		if(this.entity.getMass() != 0) {
			forceCopy.div((float)this.entity.getMass());
		}
		this.acceleration.add(forceCopy);
	}

	public Vector3f getAcceleration() {
		return this.acceleration;
	}

	public float getMaxSpeed() {
		return this.maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		if(maxSpeed >= 0) {
			this.maxSpeed = maxSpeed;
		}
	}

	public Vector3f getVelocity() {
		return this.velocity;
	}

	public void setVelocity(Vector3f vel) {
		this.velocity.set(vel);
	}

	public boolean isMoving() {
		return this.velocity.x > 0 || this.velocity.y < 0 || this.velocity.x < 0 || this.velocity.y > 0;
	}

	public void moveTo(Vector3f v) {
		Vector3f desiredVelocity = new Vector3f(v).sub(this.position);
		float distance = this.position.distance(v);
		if(distance < this.slowDownDistance) {
			float s = Util.map(desiredVelocity.length(), 0, 100, 0, this.maxSpeed);
			if(s < 0.05) {
				s = 0;
			}
			desiredVelocity.normalize();
			desiredVelocity.mul(s);
		} else {
			desiredVelocity.normalize();
			desiredVelocity.mul(this.maxSpeed);
		}
		Vector3f s = desiredVelocity.sub(this.velocity);
		this.applyForce(s);
	}

	@Override
	public void update() {
		this.velocity.add(this.acceleration);
		this.position.add(this.velocity);
		this.acceleration.zero();
	}

	public static class Builder<B extends Builder> extends Transform.Builder<B> {

		private Vector3f acceleration = new Vector3f(0, 0, 0);
		private float maxSpeed = 0;
		private float slowDownDistance = 0;
		private Vector3f velocity = new Vector3f(0, 0, 0);

		public B acceleration(Vector3f acceleration) {
			this.acceleration = acceleration;
			return (B)this;
		}

		@Override
		public MovableTransform build() {
			return new MovableTransform(this);
		}

		public B maxSpeed(float maxSpeed) {
			this.maxSpeed = maxSpeed;
			return (B)this;
		}

		public B slowDownDistance(float distance) {
			this.slowDownDistance = distance;
			return (B)this;
		}

		public B velocity(Vector3f velocity) {
			this.velocity = velocity;
			return (B)this;
		}
	}
}
