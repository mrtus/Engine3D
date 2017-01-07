package be.mrtus.engine.domain.scene;

import be.mrtus.engine.demo.domain.Scene;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.component.EntityController;
import org.apache.commons.math3.util.FastMath;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraController extends EntityController<Camera> {

	private final Keyboard keyboard;
	private final Mouse mouse;
	private final Vector3f move = new Vector3f();
	private boolean noclip = false;
	private final Scene scene;
	private final float speed = 0.2f;
	private int sprint = 1;

	public CameraController(Keyboard keyboard, Mouse mouse, Scene scene) {
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.scene = scene;
	}

	public void movePosition(float offsetX, float offsetY, float offsetZ) {
		Vector3f position = this.entity.getPosition();
		Vector3f rotation = this.entity.getTransform().getRotation();
		if(offsetZ != 0) {
			position.x += (float)FastMath.sin(FastMath.toRadians(rotation.y)) * -offsetZ;
			position.z += (float)FastMath.cos(FastMath.toRadians(rotation.y)) * offsetZ;
		}
		if(offsetX != 0) {
			position.x += (float)FastMath.sin(FastMath.toRadians(rotation.y - 90)) * -offsetX;
			position.z += (float)FastMath.cos(FastMath.toRadians(rotation.y - 90)) * offsetX;
		}
		if(this.noclip) {
			position.y += offsetY;
		} else if(offsetX != 0 || offsetZ != 0) {
			position.y = this.scene.calculateTerrainHeight(position) + 1.80f;
		}
	}

	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		Vector3f rotation = this.entity.getTransform().getRotation();
		rotation.y += offsetX * 0.1;
		rotation.x += offsetY * 0.1;
//		rotation.z += offsetZ * 0.1;
	}

	@Override
	public void update() {
		if(this.keyboard.isKeyPressed("reset_pos")) {
			this.entity.reset();
		}
		if(this.keyboard.isKeyPressed("sprint")) {
			this.sprint = 5;
		} else {
			this.sprint = 1;
		}
		if(this.keyboard.isKeyPressed("noclip")) {
			this.noclip = !this.noclip;
		}

		Vector2f deltaPos = this.mouse.getDeltaPos();
		this.moveRotation(deltaPos.x, deltaPos.y, 0);

		this.move.set(0, 0, 0);
		if(this.keyboard.isKeyPressed("forward")) {
			this.move.z = -1;
		} else if(this.keyboard.isKeyPressed("backward")) {
			this.move.z = 1;
		}
		if(this.keyboard.isKeyPressed("left")) {
			this.move.x = -1;
		} else if(this.keyboard.isKeyPressed("right")) {
			this.move.x = 1;
		}
		if(this.noclip) {
			if(this.keyboard.isKeyPressed("down")) {
				this.move.y = -1;
			} else if(this.keyboard.isKeyPressed("up")) {
				this.move.y = 1;
			}
		}
		float moveX = this.move.x * this.speed * this.sprint;
		float moveY = this.move.y * this.speed * this.sprint;
		float moveZ = this.move.z * this.speed * this.sprint;
		this.movePosition(moveX, moveY, moveZ);
	}
}
