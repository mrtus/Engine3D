package be.mrtus.engine.domain.scene.entity.component;

import be.mrtus.engine.domain.Util;
import be.mrtus.engine.domain.scene.entity.Entity;
import java.util.Random;
import org.joml.Vector3f;

public class MovableTransform extends Transform {

	protected final Vector3f acceleration;
	protected float maxSpeed;
	protected float slowdownDistance;
	protected final Vector3f velocity;
	private long nextDecisionTime;

	public MovableTransform(Builder builder) {
		super(builder);
		this.acceleration = builder.acceleration;
		this.maxSpeed = builder.maxSpeed;
		this.velocity = builder.velocity;
		this.slowdownDistance = builder.slowDownDistance;
	}

	public void applyForce(Vector3f force) {
		if(this.entity.getMass() != 0) {
			this.acceleration.add(force.div((float)this.entity.getMass(), new Vector3f()));
		}
		this.acceleration.add(force);
	}

	public void evade(Entity target) {
		this.applyForce(this.doEvade(target));
	}

	public void flee(Vector3f target) {
		this.applyForce(this.doFlee(target));
	}

	public void follow(Entity target) {
		this.applyForce(this.doFollow(target));
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
		if(distance < this.slowdownDistance) {
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

	public void pursuit(Entity target) {
		this.applyForce(this.doPursuit(target));
	}

	public void seek(Vector3f target) {
		this.applyForce(this.doSeek(target));
	}

	@Override
	public void update() {
		this.velocity.add(this.acceleration);
		this.position.add(this.velocity);
		this.acceleration.zero();
	}

	public void wander() {
		this.applyForce(this.doWander());
	}

	private Vector3f doEvade(Entity target) {
		Vector3f distance = target.getPosition().sub(this.position, new Vector3f());
		Vector3f futurePosition = target.getPosition();
		return this.doFlee(futurePosition);
	}

	private Vector3f doFlee(Vector3f target) {
		return null;
	}

	private Vector3f doFollow(Entity target) {
		if(target.getTransform() instanceof MovableTransform) {
			Vector3f distance = target.getPosition().sub(this.position, new Vector3f());
			float updatesNeeded = distance.length() / this.getMaxSpeed();
			Vector3f targetVelocity = ((MovableTransform)target.getTransform()).getVelocity().mul(updatesNeeded, new Vector3f());
			Vector3f targetFuturePosition = target.getPosition().add(targetVelocity, new Vector3f());
			return this.doSeek(targetFuturePosition);
		} else {
			return this.doSeek(target.getPosition());
		}
	}

	private Vector3f doPursuit(Entity target) {
		if(target.isMovable()) {
			MovableTransform targetTransform = (MovableTransform)target.getTransform();
			Vector3f distance = target.getPosition().sub(this.position, new Vector3f());
			Vector3f futurePosition = targetTransform.getPosition().add(targetTransform.getVelocity(), new Vector3f()).mul(distance.length() / targetTransform.getMaxSpeed());
			return this.doSeek(futurePosition);
		}
		return this.doFollow(target);
	}

	private Vector3f doSeek(Vector3f target) {
		Vector3f desired = target.sub(this.position, new Vector3f());
		float distance = desired.length();
		desired.normalize();
		if(distance <= this.slowdownDistance) {
			desired.mul(this.maxSpeed * distance / this.slowdownDistance);
		} else {
			desired.mul(this.maxSpeed);
		}
		return desired.sub(this.velocity);
	}

	private Vector3f doWander() {
		if(System.currentTimeMillis() >= this.nextDecisionTime) {
			this.nextDecisionTime = System.currentTimeMillis() + (long)(new Random().nextDouble() * 6000);
			Vector3f target = new Vector3f();
			return this.doSeek(target);
		}
		return new Vector3f();
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
